package com.helukable.quickwork.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseFragment;
import com.helukable.quickwork.modle.Variable;
import com.helukable.quickwork.util.ShareUtil;

/**
 * Created by zouyong on 2016/3/28.
 */
public class VariableFragment extends BaseFragment {
    EditText delCuValueEt,niValueEt,aluValueEt,messingBrassfixEt,copperMkEt;
    Variable mCoefficient;
    @Override
    protected int getLayoutId() {
        return R.layout.variable_layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        delCuValueEt = findViewById(R.id.del_cu_value);
        niValueEt = findViewById(R.id.ni_value);
        aluValueEt = findViewById(R.id.alu_value);
        messingBrassfixEt = findViewById(R.id.messing_brass_fix);
        copperMkEt = findViewById(R.id.del_copper_mk);
        mCoefficient = Variable.getInstance(getBaseActivity());
        delCuValueEt.setText(String.valueOf(mCoefficient.getDelCuValue()));
        niValueEt.setText(String.valueOf(mCoefficient.getNiValue()));
        aluValueEt.setText(String.valueOf(mCoefficient.getAluValue()));
        messingBrassfixEt.setText(String.valueOf(mCoefficient.getMessingBrassfix()));
        copperMkEt.setText(String.valueOf(mCoefficient.getCopperMk()));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("保存修改").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveVariable(delCuValueEt);
                saveVariable(niValueEt);
                saveVariable(aluValueEt);
                saveVariable(messingBrassfixEt);
                saveVariable(copperMkEt);
                return true;
            }
        });
    }

    private void saveVariable(EditText et){
        String str = et.getText().toString();
        if(TextUtils.isEmpty(str)){
            return ;
        }
        Float f = Float.parseFloat(str);
        if(f==0f){
            return;
        }
        switch(et.getId()){
            case R.id.del_cu_value:
                ShareUtil.save(getBaseActivity(), Variable.SDELCUVALUE,f);
                mCoefficient.setDelCuValue(f);
                break;
            case R.id.ni_value:
                ShareUtil.save(getBaseActivity(), Variable.SNIVALUE,f);
                mCoefficient.setNiValue(f);
                break;
            case R.id.alu_value:
                ShareUtil.save(getBaseActivity(), Variable.SALUVALUE,f);
                mCoefficient.setAluValue(f);
                break;
            case R.id.messing_brass_fix:
                ShareUtil.save(getBaseActivity(), Variable.SMESSINGBRASSFIX,f);
                mCoefficient.setMessingBrassfix(f);
                break;
            case R.id.del_copper_mk:
                ShareUtil.save(getBaseActivity(), Variable.SCOPPERMK,f);
                mCoefficient.setCopperMk(f);
                break;



        }
    }

}
