package com.alanson.eproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    //variables
    private static final String URL = "https://www.lenovo.com/ng/en/laptops/c/Laptops#type=TYPE_ATTR1";
    private WebView webView;
    private ProgressDialog progressDialog;
    private RelativeLayout noConnection;
    private Button noConnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init to view
        webView = findViewById(R.id.webView);
        noConnection = findViewById(R.id.noConnection_Layout);
        noConnBtn = findViewById(R.id.btn_NoConnection);

        //progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait");

        //restore the state of webView if saveInstanceState is not null
        if (savedInstanceState != null){
            webView.restoreState(savedInstanceState);
        }else {
            setUpWebView();
        }

        setListener();
    }

    //setting webView
    private void setUpWebView() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //removing part of the page
                webView.loadUrl("javascript:(function(){" +
                        "document.getElementsByClassName('a2a_kit')[0].style.display=\"none\"; " +
                        "document.getElementsByClassName('ft-legal-cta-wrapper')[0].style.display=\"none\"; " +
                        "document.getElementsByClassName('footer-bar_3-logos')[0].style.display=\"none\"; " +
                        "document.getElementsByClassName('o-mainFooter')[0].style.display=\"none\"; " +
                        "document.getElementsByClassName('dockNav')[0].style.display=\"none\"; " +
                        "document.getElementsByClassName('ft-seo-content-wrapper')[0].style.display=\"none\"; " +
                        "})()");
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setTitle("loading...");
                progressDialog.show();
                if (newProgress == 100) {
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        //improve webView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getUserAgentString();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //webView.loadUrl(URL);

        checkConnection();

    }

    //setting event listener
    private void setListener(){
        //reload the page if internet is connected
        noConnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
    }

    // WebView to go back to previous screen.
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // checking for internet connection.
    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        //checking if wifi or mobile network isConnected
        if (wifi.isConnected() || mobileNetwork.isConnected()) {
            webView.loadUrl(URL);
            webView.setVisibility(View.VISIBLE);
            noConnection.setVisibility(View.GONE);
        } else {
            webView.setVisibility(View.GONE);
            noConnection.setVisibility(View.VISIBLE);
        }
    }

    //save instanceState when orientation
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        webView.saveState(outState);
    }
}