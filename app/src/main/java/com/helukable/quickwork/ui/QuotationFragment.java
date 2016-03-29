package com.helukable.quickwork.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseFragment;
import com.helukable.quickwork.modle.Variable;

/**
 * Created by zouyong on 2016/3/25.
 */
public class QuotationFragment extends BaseFragment{
    Variable mCoefficient;

    @Override
    protected int getLayoutId() {
        return R.layout.quotation_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mCoefficient = Variable.getInstance(getBaseActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("新增物料").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });
    }

    private void showAddMateriel(){

    }
}
