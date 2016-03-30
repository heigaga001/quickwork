package com.helukable.quickwork.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseActivity;
import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.modle.Quotation;
import com.helukable.quickwork.modle.Variable;
import com.helukable.quickwork.util.Helper;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

/**
 * Created by zouyong on 2016/3/29.
 */
public class QuotationDetailsActivity extends BaseActivity {
    public static String EXTEND_QUOTATION = "quotation";
    AutoCompleteTextView company;
    CustomerAdapter mCustomerAdapter;
    Quotation mQuotation;
    EditText saleEt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_details_layout);
        mQuotation = (Quotation)getIntent().getParcelableExtra(EXTEND_QUOTATION);
        if(mQuotation == null){
            mQuotation = new Quotation();
            mQuotation.setCoefficient(16f);
            Variable variable = Variable.getInstance(this);
            mQuotation.setDelCuValue(variable.getDelCuValue());
            mQuotation.setAluValue(variable.getAluValue());
            mQuotation.setCopperMk(variable.getCopperMk());
            mQuotation.setMessingBrassfix(variable.getMessingBrassfix());
            mQuotation.setNiValue(variable.getNiValue());
        }
        company = (AutoCompleteTextView) findViewById(R.id.auto_company);
        mCustomerAdapter = new CustomerAdapter(this);
        mCustomerAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                Cursor cursor = getContentResolver().query(DBCustomer.getInstance().getUri(DBCustomer.CUSTOMER_COMPANY_ID, 0), DBCustomer.getColumns(DBCustomer.CUSTOMER_COMPANY_ID), DBCustomer.getColumn(DBCustomer.Columns.NAME) + " like ? or " + DBCompany.getColumn(DBCompany.Columns.NAME) + " like ? ", new String[]{"%" + String.valueOf(constraint) + "%", "%" + String.valueOf(constraint) + "%"}, null);
                return cursor;
            }
        });
        company.setAdapter(mCustomerAdapter);
        company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)mCustomerAdapter.getItem(position);
                String customerId = cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.ID)));
                mQuotation.setCustormerID(customerId);
                int companyId = cursor.getInt(cursor.getColumnIndexOrThrow(DBCompany.getColumn(DBCompany.Columns.ID)));
                mQuotation.setCompanyId(companyId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView text = (TextView)findViewById(R.id.cu_value);
        text.setText("CU VALUE:"+mQuotation.getDelCuValue());
        text = (TextView)findViewById(R.id.ni_value);
        text.setText("NI VALUE:"+mQuotation.getNiValue());
        text = (TextView)findViewById(R.id.alu_value);
        text.setText("ALU VALUE:"+mQuotation.getAluValue());
        text = (TextView)findViewById(R.id.copper_mk);
        text.setText("COPPER MK:"+mQuotation.getCopperMk());
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
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
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
