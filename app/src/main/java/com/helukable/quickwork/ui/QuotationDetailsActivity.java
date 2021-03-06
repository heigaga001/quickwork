package com.helukable.quickwork.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.helukable.quickwork.Const;
import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseActivity;
import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBMateriel;
import com.helukable.quickwork.db.model.DBQuotation;
import com.helukable.quickwork.db.model.DBQuotationDetails;
import com.helukable.quickwork.modle.Materiel;
import com.helukable.quickwork.modle.Quotation;
import com.helukable.quickwork.modle.Variable;
import com.helukable.quickwork.util.BuildFileUtil;
import com.helukable.quickwork.util.Helper;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.zip.Inflater;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.PasswordException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by zouyong on 2016/3/29.
 */
public class QuotationDetailsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static String EXTEND_QUOTATION = "quotation";
    AutoCompleteTextView company;
    CustomerAdapter mCustomerAdapter;
    QuotationDetailAdapter mQuotationDetailAdapter;
    Quotation mQuotation;
    EditText saleEt,tip;
    TextView phone1,telephone,fax;
    Spinner type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_details_layout);
        mQuotation = (Quotation)getIntent().getParcelableExtra(EXTEND_QUOTATION);
        phone1 = (TextView)findViewById(R.id.phone1);
        telephone = (TextView)findViewById(R.id.telephone);
        fax = (TextView)findViewById(R.id.fax);
        tip = (EditText)findViewById(R.id.quotation_tip);
        type = (Spinner)findViewById(R.id.quotation_type);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.quotation_type)) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                int padding = Helper.dp2px(QuotationDetailsActivity.this, 5);
                text.setPadding(padding, 0, 0, padding);
                return text;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                int padding = Helper.dp2px(QuotationDetailsActivity.this, 5);
                text.setPadding(padding, 0, 0, padding);
                return text;
            }
        };
        type.setAdapter(adapter);
        if(mQuotation == null){
            mQuotation = new Quotation();
            mQuotation.setCoefficient(16f);
            Variable variable = Variable.getInstance(this);
            mQuotation.setDelCuValue(variable.getDelCuValue());
            mQuotation.setAluValue(variable.getAluValue());
            mQuotation.setCopperMk(variable.getCopperMk());
            mQuotation.setMessingBrassfix(variable.getMessingBrassfix());
            mQuotation.setNiValue(variable.getNiValue());
            mQuotation.setCreateAt(""+System.currentTimeMillis());
            mQuotation.setType(0);
        }
        company = (AutoCompleteTextView) findViewById(R.id.auto_company);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.phone1:
                        if(!TextUtils.isEmpty(mQuotation.getPhone1())){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mQuotation.getPhone1()));
                            startActivity(intent);
                        }
                        break;
                    case R.id.telephone:
                        if(!TextUtils.isEmpty(mQuotation.getTelephone())){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mQuotation.getTelephone()));
                            startActivity(intent);
                        }
                        break;
                    case R.id.fax:
                        break;
                }
            }
        };
        phone1.setOnClickListener(onClickListener);
        telephone.setOnClickListener(onClickListener);
        if(mQuotation.getId()>0){
            company.setText(mQuotation.getName());
            company.setEnabled(false);
            company.setBackgroundColor(Color.TRANSPARENT);
            setTitle("修改报价单");
            phone1.setText("电话\n"+mQuotation.getPhone1());
            telephone.setText("手机\n"+mQuotation.getTelephone());
            fax.setText("传真\n"+mQuotation.getFax());
        }else{
            setTitle("新增报价单");
        }
        type.setSelection(mQuotation.getType());
        tip.setText(mQuotation.getTip()==null?"":mQuotation.getTip());

        mCustomerAdapter = new CustomerAdapter(this);
        mQuotationDetailAdapter = new QuotationDetailAdapter(this);
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(mQuotationDetailAdapter);
        mCustomerAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                Cursor cursor = getContentResolver().query(DBCustomer.getInstance().getUri(DBCustomer.CUSTOMER_COMPANY_ID, 0), DBCustomer.getColumns(DBCustomer.CUSTOMER_COMPANY_ID), DBCustomer.getColumn(DBCustomer.Columns.NAME) + " like ? or " + DBCompany.getColumn(DBCompany.Columns.NAME) + " like ? ", new String[]{"%" + String.valueOf(constraint) + "%", "%" + String.valueOf(constraint) + "%"}, null);
                return cursor;
            }
        });
        company.setAdapter(mCustomerAdapter);
        company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)mCustomerAdapter.getItem(position);
                String customerId = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.ID)));

                mQuotation.setCustormerID(customerId);
                int companyId = cursor.getInt(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.COMPANYID)));
                mQuotation.setCompanyId(companyId);
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.PHONE1)));
                String mobilepgone = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.MOBILE_PHONE)));
                String faxStr = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.FAX)));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.EMAIL)));
                phone1.setText("电话\n"+phone);
                telephone.setText("手机\n"+mobilepgone);
                fax.setText("传真\n"+faxStr);
                mQuotation.setEmail(email);
                mQuotation.setCompanyName(cursor.getString(cursor.getColumnIndexOrThrow(DBCompany.getColumn(DBCompany.Columns.NAME))));
            }
        });
        TextView text = (TextView)findViewById(R.id.cu_value);
        text.setText("DEL: "+mQuotation.getDelCuValue());
        text = (TextView)findViewById(R.id.time);
        text.setText(Helper.getData(mQuotation.getCreateAt()));
        saleEt = (EditText)findViewById(R.id.sale_coefficient);
        saleEt.setText(""+mQuotation.getCoefficient());
        saleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float f = Float.parseFloat(s.toString());
                    mQuotation.setCoefficient(f);
                    saveQuotation();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        initLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quotation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if(TextUtils.isEmpty(mQuotation.getCustormerID())){
                    showToast("未选择联系人");
                    return true;
                }
                if(mQuotation.getId()==0){
                    saveQuotation();
                }
                System.out.println("=="+mQuotation.getId());
                showAddMateriel();
                break;
            case R.id.action_save:
                if(TextUtils.isEmpty(mQuotation.getCustormerID())){
                    showToast("未选择联系人");
                    return true;
                }
                saveQuotation();
                break;
            case R.id.action_send_sea:
            case R.id.action_send_sky:
            case R.id.action_send_all:
                String filePath = Environment.getExternalStorageDirectory() + "/quickwork/%s和柔报价-%s.xls";
                filePath = String.format(filePath,Helper.getTodayData(),mQuotation.getCompanyName());
                File file = new File(filePath);
                Cursor cursor = mQuotationDetailAdapter.getCursor();
                String [][] data = null;
                if(item.getItemId() == R.id.action_send_all){
                    data = new String[cursor.getCount()+1][];
                    data[0] = new String[]{"订货号","品名","规格","数量(M)","海运单价(元/米)","空运单价(元/米)"};
                }else{
                    data = new String[cursor.getCount()+1][5];
                    data[0] = new String[]{"订货号","品名","规格","数量(M)","单价(元/米)"};
                }
                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToPosition(i);
                    Materiel materiel = Materiel.createByCursor(cursor);
                    materiel.initPrice(mQuotation.getCoefficient(),mQuotation);
                    if(item.getItemId()==R.id.action_send_sea){
                        data[i+1]=new String[]{""+materiel.getId(),""+materiel.getType(),""+materiel.getSize(),""+materiel.getNum(),""+materiel.getFreightSer()};
                    }else if(item.getItemId()==R.id.action_send_sky){
                        data[i+1]= new String[]{""+materiel.getId(),""+materiel.getType(),""+materiel.getSize(),""+materiel.getNum(),""+materiel.getFreightSky()};
                    }else{
                        data[i+1]=new String[]{""+materiel.getId(),""+materiel.getType(),""+materiel.getSize(),""+materiel.getNum(),""+materiel.getFreightSer(),""+materiel.getFreightSky()};
                    }
                }
                try{
                    boolean create = BuildFileUtil.createXLSFile(file,data);
                    if(!create){
                        showToast("生成文件失败");
                        return true;
                    }
                    Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
                    String[] tos = { mQuotation.getEmail() };
                    intent.putExtra(Intent.EXTRA_EMAIL,tos);
//                    intent.setData(Uri.parse("mailto:"+mQuotation.getEmail()));
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "报价 - 来自和柔电缆");
                    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<div>您好！<br/><br/>感谢询价，报价详见附件。<br/><br/>此价格含税含运费，货期X周。<br/><br/>谢谢！</div>"));
                    ArrayList<Uri> uris = new ArrayList<Uri>();
                    uris.add(Uri.parse("file://"+filePath));
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//                    Html.fromHtml()
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveQuotation(){
        boolean needLoad = false;
        if(TextUtils.isEmpty(mQuotation.getCustormerID())){
            return;
        }
        if(mQuotation.getId()==0){
            needLoad = true;
        }
        mQuotation.setType(type.getSelectedItemPosition());
        mQuotation.setTip(tip.getText().toString());
        DBQuotation.getInstance().insert(this,mQuotation);
        if(needLoad){
            initLoader();
        }
    }

    private void initLoader(){
        if(mQuotation.getId()==0){
            return;
        }
        getSupportLoaderManager().initLoader(mQuotation.getId(),null,this);

    }

    private void showAddMateriel(){
        final View content = LayoutInflater.from(this).inflate(R.layout.add_materiel_layout,null);
        final Spinner spinner = (Spinner)content.findViewById(R.id.type);
        final EditText materielEt = (EditText)content.findViewById(R.id.materiel_id);
        final EditText numEt = (EditText)content.findViewById(R.id.materiel_num);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.materiel_type)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                int padding = Helper.dp2px(QuotationDetailsActivity.this, 10);
                text.setPadding(padding, padding, 0, padding);
                return text;
            }
        };
        spinner.setAdapter(adapter);
        new AlertDialog.Builder(this).setTitle("请设置电缆型号")
                .setView(content)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int select = spinner.getSelectedItemPosition();
                        String id = materielEt.getText().toString();
                        if(TextUtils.isEmpty(id)){
                            showToast("物料id不能为空");
                            return;
                        }
                        String where = "";
                        if(select == 0){
                            where = DBMateriel.Columns.ID +" = ? ";
                        }else{
                            where = DBMateriel.Columns.LANPUID + " = ? ";
                        }
                        Cursor cursor = getContentResolver().query(DBMateriel.getInstance().getUri(DBMateriel.MATERIEL,0),new String[] { DBMateriel.Columns.ID },where,new String[]{id},null);
                        if(cursor!=null){
                            if(cursor.getCount() == 1){
                                cursor.moveToPosition(0);
                                int materielId = cursor.getInt(cursor.getColumnIndexOrThrow(DBMateriel.Columns.ID));
                                if(materielId>0){
                                    int num= 0;
                                    try {
                                        num = Integer.parseInt(numEt.getText().toString());
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    DBQuotationDetails.getInstance().insert(QuotationDetailsActivity.this,mQuotation.getId(),materielId,num);
                                    getSupportLoaderManager().restartLoader(mQuotation.getId(),null,QuotationDetailsActivity.this);
                                }
                            }else{
                                showToast("您所查找的物料不存在");
                            }
                            cursor.close();
                        }else{
                            showToast("您所查找的物料不存在");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,DBQuotationDetails.getInstance().getUri(DBQuotationDetails.QUOTATIONDETAIL_MATERIEL_STOCKS_ID,0),DBQuotationDetails.getColumns(DBQuotationDetails.QUOTATIONDETAIL_MATERIEL_STOCKS_ID),DBQuotationDetails.getColumn(DBQuotationDetails.Columns.QUOTATIONID)+" = ? ",new String[]{String.valueOf(id)},DBQuotationDetails.getColumn(DBQuotationDetails.Columns.ID));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mQuotationDetailAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuotationDetailAdapter.swapCursor(null);
    }

    class QuotationDetailAdapter extends CursorAdapter{

        public QuotationDetailAdapter(Context context) {
            super(context, null, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.quotation_detail_item,null);
            Holder holder = new Holder(view);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Holder holder = (Holder)view.getTag();
            Materiel materiel = Materiel.createByCursor(cursor);
            materiel.initPrice(mQuotation.getCoefficient(),mQuotation);
            holder.id.setText(String.valueOf(materiel.getId()));
            holder.type.setText(materiel.getType()+"\n"+materiel.getSize());
            holder.price.setText(""+materiel.getFreightSer());
            holder.priceSky.setText(""+materiel.getFreightSky());
            holder.stock.setText("("+materiel.getStock()+"/"+materiel.getStockImport()+")");
            holder.num.setText(""+materiel.getNum());
            holder.num.setTag(cursor.getPosition());
            holder.id.setTag(cursor.getPosition());
            holder.type.setTag(cursor.getPosition());
            holder.price.setTag(cursor.getPosition());
        }
        class Holder implements View.OnFocusChangeListener,View.OnClickListener{
            EditText id;
            TextView type;
            TextView price;
            TextView stock;
            TextView priceSky;
            EditText num;

            public Holder(View view){
                id = (EditText)view.findViewById(R.id.materiel_id);
                type = (TextView)view.findViewById(R.id.materiel_type);
                price = (TextView)view.findViewById(R.id.materiel_price);
                priceSky = (TextView)view.findViewById(R.id.materiel_price_sky);
                stock = (TextView)view.findViewById(R.id.stock);
                num = (EditText)view.findViewById(R.id.num);
                num.setOnFocusChangeListener(this);
                id.setOnFocusChangeListener(this);
                type.setOnClickListener(this);
                price.setOnClickListener(this);
                view.setTag(this);
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    int pos = (Integer)v.getTag();
                    if(v == id){
                        Cursor cursor = (Cursor) getItem(pos);
                        int oldId = cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.MATERIELID)));
                        int detailId = cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.ID)));
                        try{
                            int currentId = Integer.parseInt(id.getText().toString());
                            if(currentId == oldId){
                                return;
                            }
                            Cursor cursor1 = getContentResolver().query(DBMateriel.getInstance().getUri(DBMateriel.MATERIEL,0),new String[] { DBMateriel.Columns.ID },DBMateriel.Columns.ID +" = ? ",new String[]{""+currentId},null);
                            if(cursor1!=null){
                                if(cursor1.getCount() == 1){
                                    DBQuotationDetails.getInstance().updateMarteriel(QuotationDetailsActivity.this,detailId,currentId);
                                    getSupportLoaderManager().restartLoader(mQuotation.getId(),null,QuotationDetailsActivity.this);
                                    return;
                                }else{
                                    showToast("您所查找的物料不存在,原数据将删除");
                                }
                                cursor1.close();
                            }else{
                                showToast("您所查找的物料不存在");
                            }
                        }catch (Exception e){

                        }
                        DBQuotationDetails.getInstance().delete(QuotationDetailsActivity.this,detailId);
                        getSupportLoaderManager().restartLoader(mQuotation.getId(),null,QuotationDetailsActivity.this);
                    }else{
                        Cursor cursor = (Cursor) getItem(pos);
                        int oldNum= cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.NUM)));
                        int detailId = cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.ID)));
                        try {
                            int currentNum = Integer.parseInt(num.getText().toString());
                            if (currentNum == oldNum) {
                                return;
                            }
                            DBQuotationDetails.getInstance().updateNum(QuotationDetailsActivity.this,detailId,currentNum);
                            getSupportLoaderManager().restartLoader(mQuotation.getId(),null,QuotationDetailsActivity.this);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onClick(View v) {
                int pos = (Integer)v.getTag();
                Cursor cursor = (Cursor) getItem(pos);
                if(v == type){
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.MATERIELID)));
                    Intent intent = new Intent(QuotationDetailsActivity.this,CheckStockActivity.class);
                    intent.putExtra(CheckStockActivity.EXTRA_ID,id);
                    startActivity(intent);
                }else if(v == price){
                    Intent intent = new Intent(QuotationDetailsActivity.this,PdfViewActivity.class);
                    int startPage = cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBMateriel.Columns.STARTPAGE)));
//                    ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//                    myClipboard.setPrimaryClip(ClipData.newPlainText("text", String.valueOf(startPage)));
//                    showToast("复制成功："+startPage);
                    intent.putExtra(PdfViewActivity.EXTRA_INDEX,startPage);
                    startActivity(intent);
                }
            }
        }
    }

    class CustomerAdapter extends CursorAdapter {

        public CustomerAdapter(Context context) {
            super(context, null, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            TextView text = (TextView) LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, null);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            int padding = Helper.dp2px(context, 10);
            text.setPadding(padding, padding, 0, padding);
            return text;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(convertToString(cursor));
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            String customerName = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.NAME)));
            String companyName = cursor.getString(cursor.getColumnIndexOrThrow(DBCompany.getColumn(DBCompany.Columns.NAME)));
            return customerName + "(" + companyName+")";
        }
    }
}
