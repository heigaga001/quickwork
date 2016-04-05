package com.helukable.quickwork.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseActivity;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;

/**
 * Created by zouyong on 2016/4/5.
 */
public class PdfViewActivity extends BaseActivity{
    public static String EXTRA_INDEX = "index";
    PDFView pdfView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_layout);
        int index = getIntent().getIntExtra(EXTRA_INDEX,1);
        pdfView = (PDFView)findViewById(R.id.pdfview);
        String filePath = Environment.getExternalStorageDirectory() + "/quickwork/HELU Catalogue_2015.pdf";
        File file = new File(filePath);
        if(file.exists()){
            pdfView
                .fromFile(file)
                .defaultPage(30)
                .showMinimap(true)
                .enableSwipe(true)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                       setTitle(page+"/"+pageCount);
                    }
                })
                .load();
        }else{
            showToast("文件未找到"+filePath);
        }
    }
}
