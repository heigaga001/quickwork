package com.helukable.quickwork.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseFragment;
import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBQuotation;
import com.helukable.quickwork.modle.Quotation;
import com.helukable.quickwork.modle.Variable;
import com.helukable.quickwork.util.Helper;

/**
 * Created by zouyong on 2016/3/25.
 */
public class QuotationFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    Variable mCoefficient;
    ListView list;
    QuotationAdapter adapter;
    String [] types;
    int selectType = 0;
    String where = null;
    @Override
    protected int getLayoutId() {
        return R.layout.quotation_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        types = getContext().getResources().getStringArray(R.array.quotation_type);
        list = findViewById(R.id.listview);
        adapter = new QuotationAdapter(getBaseActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)adapter.getItem(position);
                Quotation quotation = Quotation.createFromCursor(cursor);
                Intent intent = new Intent(getBaseActivity(),QuotationDetailsActivity.class);
                intent.putExtra(QuotationDetailsActivity.EXTEND_QUOTATION,quotation);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)adapter.getItem(position);
                int quotationId = cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.ID)));
                showDeleteDialog(quotationId);
                return false;
            }
        });
        getLoaderManager().initLoader(0,null,this);
    }

    public void showDeleteDialog(final int id){
        new AlertDialog.Builder(getBaseActivity())
                .setTitle("提示")
                .setMessage("你确认删除此条报价吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBQuotation.getInstance().deleteById(getBaseActivity(),id);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.quotation_type,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                startActivity(new Intent(getBaseActivity(), QuotationDetailsActivity.class));
                break;
            case R.id.action_0:
                where = DBQuotation.getColumn(DBQuotation.Columns.TYPE) +" = ? ";
                selectType = 0;
                getLoaderManager().restartLoader(0,null,this);
                break;
            case R.id.action_1:
                where = DBQuotation.getColumn(DBQuotation.Columns.TYPE) +" = ? ";
                selectType = 1;
                getLoaderManager().restartLoader(0,null,this);
                break;
            case R.id.action_2:
                where = DBQuotation.getColumn(DBQuotation.Columns.TYPE) +" = ? ";
                selectType = 2;
                getLoaderManager().restartLoader(0,null,this);
                break;
            case R.id.action_3:
                where = DBQuotation.getColumn(DBQuotation.Columns.TYPE) +" = ? ";
                selectType = 3;
                getLoaderManager().restartLoader(0,null,this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getBaseActivity(), DBQuotation.getInstance().getUri(DBQuotation.QUOTATION_CUSTOMER_ID, 0), DBQuotation.getColumns(DBQuotation.QUOTATION_CUSTOMER_ID), where,where==null?null:new String[]{String.valueOf(selectType)},  DBQuotation.getColumn(DBQuotation.Columns.CREATEAT)+" desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    class QuotationAdapter extends CursorAdapter{

        public QuotationAdapter(Context context) {
            super(context, null, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v =  LayoutInflater.from(getBaseActivity()).inflate(R.layout.quotation_item,null);
            Holder holder = new Holder();
            holder.content = (TextView)v.findViewById(R.id.content);
            holder.time = (TextView)v.findViewById(R.id.time);
            v.setTag(holder);
            return v ;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Holder holder =(Holder)view.getTag();
            StringBuffer sb = new StringBuffer();
            sb.append("姓名：");
            sb.append(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.NAME))));
            sb.append("\n");
            sb.append("系数：");
            sb.append(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.COEFFICIENT))));
            sb.append("\n");
            sb.append("类型：");
            int type= cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.TYPE)));
            sb.append(types[type]);
            sb.append("\n");
            sb.append("公司：");
            sb.append(cursor.getString(cursor.getColumnIndexOrThrow(DBCompany.getColumn(DBCompany.Columns.NAME))));
            sb.append("\n");
            holder.content.setText(sb.toString());
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.CREATEAT)));
            holder.time.setText(Helper.getData(time));
        }

        class Holder{
            TextView content;
            TextView time;
        }
    }

}
