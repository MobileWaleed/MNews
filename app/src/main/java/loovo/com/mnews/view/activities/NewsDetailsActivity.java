package loovo.com.mnews.view.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import loovo.com.mnews.R;

public class NewsDetailsActivity extends AppCompatActivity {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.try_again)
    Button retry;
    @BindView(R.id.loading_failed_text)
    TextView LoadingFailedText;
    @BindView(R.id.content_unavailable)
    LinearLayout contentUnAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);
        setupArticleUnavailableView();
        setupWebView();
        setupSwipeRefreshLayout();
        loadArticle();
    }

    private void setupArticleUnavailableView() {
        LoadingFailedText.setText(R.string.error_unableToLoadArticle);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadArticle();
                refreshLayout.setRefreshing(true);
            }
        });
    }


    private String getArticlUrl() {
        return getIntent().getExtras().getString("url");
    }

    private WebView setupWebView() {

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                hideArticleUnavailable();
                refreshLayout.setRefreshing(true);
            }

            public void onPageFinished(WebView view, String url) {
                refreshLayout.setRefreshing(false);
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                refreshLayout.setRefreshing(false);
                showArticleUnavailable();
            }
        });

        return webView;
    }

    private void hideArticleUnavailable() {
        webView.setVisibility(View.VISIBLE);
        contentUnAvailable.findViewById(R.id.content_unavailable).setVisibility(View.GONE);
    }

    private void showArticleUnavailable() {
        webView.setVisibility(View.GONE);
        contentUnAvailable.findViewById(R.id.content_unavailable).setVisibility(View.VISIBLE);
    }

    private void setupSwipeRefreshLayout() {
        refreshLayout.setEnabled(false);
    }

    private void loadArticle() {
        if (getArticlUrl() != null && !TextUtils.isEmpty(getArticlUrl())) {
            webView.loadUrl(getArticlUrl());
        } else {
            showArticleUnavailable();
            refreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
