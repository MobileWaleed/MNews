package loovo.com.mnews.view.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import loovo.com.mnews.MNewsApp;
import loovo.com.mnews.R;
import loovo.com.mnews.db.entity.NewsItem;
import loovo.com.mnews.repository.model.MySharedPreferences;
import loovo.com.mnews.repository.model.SearchFilter;
import loovo.com.mnews.utils.Resource;
import loovo.com.mnews.utils.Utils;
import loovo.com.mnews.view.adapters.NewsListAdapter;
import loovo.com.mnews.view.callback.NewsCallback;
import loovo.com.mnews.view.decorations.DividerItemDecoration;
import loovo.com.mnews.view.fragments.FilterSearchResultDialogFragment;
import loovo.com.mnews.viewmodel.NewsListViewModel;

public class NewsListActivity extends AppCompatActivity implements NewsCallback {
    @BindView(R.id.search_results)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tvError)
    TextView errorTextView;
    @BindView(R.id.btn_retry)
    Button retry;
    @BindView(R.id.main_container)
    FrameLayout mainContainer;
    private NewsListViewModel mNewsListViewModel;
    private List<NewsItem> news = new ArrayList<>();

    @Inject
    MySharedPreferences mySharedPreferences;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SearchFilter searchFilter;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private NewsListAdapter newsAdapter;
    private String currentUpdateString = "0 seconds ago";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);
        MNewsApp.getApp().getAppComponent().inject(this);
        setSupportActionBar(mToolbar);
        observeNewsViewModel();
        initializeSwipeToRefreshView();
        setupSharedPrefrence();
        setupRecyclerView();
        mNewsListViewModel.setSearchQuery(searchFilter);
        prepareRetry();
        updateSubtitle();

    }

    private void updateSubtitle() {
        String lastUpdatedRelativeTime = Utils.getRelativeTimeFromDate(mySharedPreferences.getDate());
        if (mySharedPreferences.isUpdateDateAvailable()) {
            if (lastUpdatedRelativeTime.equalsIgnoreCase(currentUpdateString)) {
                mToolbar.setSubtitle(getResources().getString(R.string.last_updated) + " " + getResources().getString(R.string.just_now));
            } else {
                mToolbar.setSubtitle(getResources().getString(R.string.last_updated) + " " + lastUpdatedRelativeTime);
            }

        }

    }

    private void prepareRetry() {
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewsListViewModel.refresh();
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        newsAdapter = new NewsListAdapter(this);

        mRecyclerView.setAdapter(newsAdapter);
    }

    private void observeNewsViewModel() {
        mNewsListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewsListViewModel.class);
        observeViewModel();
    }

    private void setupSharedPrefrence() {
        getUpdatedSearchFilter();
        sharedPreferences.registerOnSharedPreferenceChangeListener(mNewsListViewModel.getOnPrefrenceChangeListener());
    }

    private void getUpdatedSearchFilter() {
        if (mySharedPreferences.getSearchFilter() != null) {
            searchFilter = mySharedPreferences.getSearchFilter();
        }
    }

    private void observeViewModel() {
        mNewsListViewModel.getSearchResults()
                .observe(this, this::handleResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //Setup SearchView
        MenuItem item = menu.findItem(R.id.action_search);
        setupSearchView(item);
        return true;
    }

    private void setupSearchView(MenuItem item) {
        searchView.setMenuItem(item);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getUpdatedSearchFilter();
                searchFilter.setKeyWord(query);
                mySharedPreferences.putSearchFilter(searchFilter);
                mNewsListViewModel.setSearchQuery(searchFilter);
                dismissKeyboard(searchView.getWindowToken());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void dismissKeyboard(IBinder windowToken) {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    private void handleResponse(Resource<List<NewsItem>> listResource) {
        if (listResource != null) {
            switch (listResource.status) {
                case ERROR:
                    showOfflineSnackBar();
                    handleData(listResource);
                    break;
                case LOADING:
                    showLoadingDialog();
                    break;
                case SUCCESS:
                    saveLastUpdate();
                    handleData(listResource);
                    break;
                default:
                    showErrorMsg();
                    errorTextView.setText(getResources().getString(R.string.error_no_results));
                    break;
            }
        }

        stopSwipeToRefreshLayout();
    }

    private void showErrorMsg() {
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void showLoadingDialog() {
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    private void showOfflineSnackBar() {
        Snackbar snackbar = Snackbar
                .make(mainContainer, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    private void handleData(Resource<List<NewsItem>> listResource) {
        hideAllViews();
        populateList(listResource);
    }

    private void hideAllViews() {
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    private void populateList(Resource<List<NewsItem>> listResource) {
        if (listResource.data != null && listResource.data.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);
            newsAdapter.setData(listResource.data);
            newsAdapter.notifyDataSetChanged();
        } else {
            errorTextView.setText(getResources().getString(R.string.error_no_results));
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void saveLastUpdate() {
        Date date = new Date(System.currentTimeMillis());
        mySharedPreferences.putDate(date);
        updateSubtitle();
    }

    private void stopSwipeToRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    private void initializeSwipeToRefreshView() {
        refreshLayout.setOnRefreshListener(mNewsListViewModel.getOnRefreshListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterSearchResultDialogFragment editNameDialogFragment = FilterSearchResultDialogFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onClick(NewsItem newsItem) {
        Intent i = new Intent(NewsListActivity.this, NewsDetailsActivity.class);
        i.putExtra("url", newsItem.url);
        startActivity(i);


    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            // Close the search on the back button press.
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
