package com.helukable.quickwork.ui;

import android.content.Context;
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
    @Override
    protected int getLayoutId() {
        return R.layout.quotation_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("新增报价单").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(getBaseActivity(), QuotationDetailsActivity.class));
                return true;
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getBaseActivity(), DBQuotation.getInstance().getUri(DBQuotation.QUOTATION_CUSTOMER_ID, 0), DBQuotation.getColumns(DBQuotation.QUOTATION_CUSTOMER_ID), null, null,  DBQuotation.getColumn(DBQuotation.Columns.CREATEAT)+" desc");
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
