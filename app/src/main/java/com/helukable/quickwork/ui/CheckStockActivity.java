package com.helukable.quickwork.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.helukable.quickwork.R;
import com.helukable.quickwork.base.BaseActivity;
import com.helukable.quickwork.util.ShareUtil;

/**
 * Created by Administrator on 2016/4/2.
 */
public class CheckStockActivity extends BaseActivity {
    public static String EXTRA_ID = "id";
    WebView webView;
    String url = "https://www.helukabel.de/helis/index.php?lang=en";
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        id = getIntent().getIntExtra(EXTRA_ID, 0);
        String url_all = ShareUtil.read(this, "url_all", null);
        setTitle("查询德国库存");
        if (url_all != null && id > 0) {
            webView.loadUrl(url_all + "&artnr=" + id);
        } else {
            webView.loadUrl(url);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("func=besta") && url.contains("&artnr=")) {
                    String s = url.substring(0, url.indexOf("&artnr="));
                    ShareUtil.save(CheckStockActivity.this, "url_all", s);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy_account:

                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(ClipData.newPlainText("text", "!HELUcn@2155"));
                showToast("复制成功");


                break;
            case R.id.action_copy_psw:
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(ClipData.newPlainText("text", "$Dt4Pt36"));
                showToast("复制成功");

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
