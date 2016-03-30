package com.helukable.quickwork.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseFragment;
import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBMateriel;
import com.helukable.quickwork.db.model.DBStock;
import com.helukable.quickwork.modle.Company;
import com.helukable.quickwork.modle.Customer;
import com.helukable.quickwork.modle.Materiel;
import com.helukable.quickwork.util.FileUtils;
import com.helukable.quickwork.util.Helper;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by zouyong on 2016/3/22.
 */
public class LoadXLSFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner mTableType;
    Spinner mLoadType;
    EditText mSheetEt, mStartEt;
    int sheetPos = 0;
    int loadStart = 0;
    private static final int FILE_SELECT_CODE = 10;
    int tabletype = 0;
    int loadtype = 0;
    String filePath;

    @Override
    protected int getLayoutId() {
        return R.layout.load_xls_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTableType = findViewById(R.id.spinner_table_type);
        mLoadType = findViewById(R.id.spinner_load_type);
        mSheetEt = findViewById(R.id.load_frist_sheet);
        mStartEt = findViewById(R.id.load_frist_num);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.table_type)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                int padding = Helper.dp2px(getActivity(), 10);
                text.setPadding(padding, padding, 0, padding);
                return text;
            }
        };
        mTableType.setAdapter(adapter);
        adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.load_type)) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                int padding = Helper.dp2px(getActivity(), 10);
                text.setPadding(padding, padding, 0, padding);
                return text;
            }
        };
        mLoadType.setAdapter(adapter);
        mTableType.setOnItemSelectedListener(this);
        mLoadType.setOnItemSelectedListener(this);
        findViewById(R.id.select_file_layout).setOnClickListener(this);
        findViewById(R.id.sumit).setOnClickListener(this);

    }

    /**
     * 调用文件选择软件来选择文件
     **/
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要加载的表格"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == FILE_SELECT_CODE) {
            Uri result = data == null ? null : data
                    .getData();
            String path = FileUtils.getPath(getActivity(), result);
            TextView tvPath = findViewById(R.id.select_file);
            filePath = path;
            try{

                tvPath.setText(""+new File(path).getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_file_layout:
                showFileChooser();
                break;
            case R.id.sumit:
                loadFile();

                break;
        }
    }

    private void loadFile() {
        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(getActivity(), "请选择表格文件", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String sheetStr = mSheetEt.getText().toString();
        String startStr = mStartEt.getText().toString();
        if (TextUtils.isEmpty(sheetStr) || TextUtils.isEmpty(startStr)) {
            Toast.makeText(getActivity(), "请输入sheet索引及起始位置", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        sheetPos = Integer.parseInt(sheetStr);
        loadStart = Integer.parseInt(startStr);
        File file = new File(filePath);
        if (!file.exists() || !filePath.endsWith(".xls")) {
            Toast.makeText(getActivity(), "只能读取xls格式文件", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        getBaseActivity().showProgress("加载中...");
        new loadDataTask(file).execute("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mTableType == parent) {
            tabletype = position;
        } else if (mLoadType == parent) {
            loadtype = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class loadDataTask extends AsyncTask<String,Integer,Boolean>{

        private File file;

        public loadDataTask(File file) {
            this.file = file;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Workbook wb = null;
            try {
                wb = Workbook.getWorkbook(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
            if(wb == null){
                return false;
            }
            Sheet sheet = wb.getSheet(sheetPos);
            int rows = sheet.getRows();
            switch (tabletype){
                case 0:
                    if(loadtype == 0){
                        DBMateriel.getInstance().deleteData(getBaseActivity());
                    }
                    break;
                case 1:
                    if(loadtype == 0){
                        DBCustomer.getInstance().deleteData(getBaseActivity());
                    }
                    break;
                case 2:
                    DBStock.getInstance().deleteData(getBaseActivity());
                    break;
                case 3:
                    if(loadtype == 0){
                        DBCompany.getInstance().deleteData(getBaseActivity());
                    }
                    break;
            }

            int lastprocess = 0;
            for (int i = loadStart; i < rows; i++) {
                if(i==loadStart){
                    publishProgress(0);
                }else{
                    int process = (i-loadStart)*100/(rows-loadStart);
                    if(process!=lastprocess){
                        lastprocess = process;
                        publishProgress(lastprocess);
                    }

                }
                switch (tabletype) {
                    case 0:
                        Materiel materiel = Materiel.creatBySheet(sheet,i);
                        DBMateriel.getInstance().insertMateriel(getBaseActivity(),materiel);
                        break;
                    case 1:
                        Customer customer = Customer.createBySheet(sheet,i);
                        DBCustomer.getInstance().insertCustomer(getBaseActivity(),customer);
                        break;
                    case 2:
                        DBStock.getInstance().insertStock(getBaseActivity(),Helper.getCellInt(sheet,i,0),Helper.getCellInt(sheet,i,2));
                        break;
                    case 3:
                        Company company = Company.createBySheet(sheet,i);
                        DBCompany.getInstance().insertCompany(getBaseActivity(),company);
                        break;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            getBaseActivity().showProgress("加载进度"+values[0]+"%");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            getBaseActivity().dismissProgress();
        }
    }
}
