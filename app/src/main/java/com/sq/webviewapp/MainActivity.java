package com.sq.webviewapp;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // پیدا کردن WebView از فایل layout
        webView = findViewById(R.id.webView);

        // فعال کردن جاوااسکریپت (برای پخش ویدیو و عملکرد سایت)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // برای ذخیره‌سازی داده‌های سایت
        webSettings.setMediaPlaybackRequiresUserGesture(false); // اجازه پخش خودکار ویدیوها

        // تنظیم WebViewClient برای کنترل رفتار WebView
        webView.setWebViewClient(new WebViewClient());

        // تنظیم WebChromeClient برای مدیریت بهتر محتوای چندرسانه‌ای (مثل ویدیو)
        webView.setWebChromeClient(new WebChromeClient());

        // تنظیم DownloadListener برای دانلود فایل‌ها (اگر لینک وجود داشت)
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "MyketFile");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "در حال دانلود...", Toast.LENGTH_LONG).show();
            }
        });

        // درخواست اجازه دسترسی به حافظه برای دانلود
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        // بارگذاری سایت مایکت
        webView.loadUrl("https://myket.ir");
    }

    // مدیریت دکمه بازگشت (برای رفتن به صفحه قبلی در WebView)
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
