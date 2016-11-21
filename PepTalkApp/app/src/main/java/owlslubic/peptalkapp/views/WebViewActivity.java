package owlslubic.peptalkapp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import owlslubic.peptalkapp.R;

import static android.support.v7.appcompat.R.styleable.Toolbar;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_webview);
        progressBar.setMax(100);
        WebView webView = (WebView) findViewById(R.id.webview);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
                    getSupportActionBar().setSubtitle(title);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.i("WEBVIEW", "onReceivedSslError: " + error.toString());
                Context context = view.getContext();
                Uri uri = Uri.parse(getIntent().getStringExtra("url"));
                launchSslWarningDialog(context, error.toString(), uri, handler);

            }
        });

        WebSettings settings = webView.getSettings();
        // By using this method together with the overridden method onReceivedSslError()
        // you will avoid the "WebView Blank Page" problem to appear
        settings.setDomStorageEnabled(true);


        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }

    public void launchSslWarningDialog(Context context, String warning, final Uri url, final SslErrorHandler handler) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("SSL error: " + warning)
                .setMessage("Please choose if you want to proceed anyway, or if you'd rather open leave this app and open the site in your browser")
                .setNegativeButton("proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //this will ignore the Ssl error and will go forward to your site
                        handler.proceed();
                    }
                })
                .setNeutralButton("open in browser", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, url));
                    }
                })
                .create();
        dialog.show();
    }
}
