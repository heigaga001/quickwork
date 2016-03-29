package com.helukable.quickwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zouyong on 2016/3/22.
 */
public abstract class BaseFragment extends Fragment {

    private View mMainView;
    BaseActivity baseActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getLayoutId()<=0){
            return null;
        }
        mMainView = inflater.inflate(getLayoutId(),container,false);
        return mMainView;
    }

    protected abstract int getLayoutId();

    protected <T extends View> T findViewById(int viewId) {
        return (T) mMainView.findViewById(viewId);
    }
    protected BaseActivity getBaseActivity() {
        if (baseActivity == null) {
            baseActivity = (BaseActivity) getActivity();
        }
        return baseActivity;
    }
}
