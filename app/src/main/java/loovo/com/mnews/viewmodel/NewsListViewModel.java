package loovo.com.mnews.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

import javax.inject.Inject;

import loovo.com.mnews.db.entity.NewsItem;
import loovo.com.mnews.helpers.MySharedPreferences;
import loovo.com.mnews.repository.MNewsRepository;
import loovo.com.mnews.repository.model.SearchFilter;
import loovo.com.mnews.utils.Resource;


public class NewsListViewModel extends ViewModel {

    private final MutableLiveData<SearchFilter> query = new MutableLiveData<>();
    private final LiveData<Resource<List<NewsItem>>> searchResults;
    @Inject
    MySharedPreferences mySharedPreferences;
    @Inject
    SearchFilter searchFilter;

    @Inject
    public NewsListViewModel(@NonNull MNewsRepository newsRepository) {
        searchResults = Transformations.switchMap(query, search -> {
            return newsRepository.searchNews(search);
        });
    }

    @VisibleForTesting
    public LiveData<Resource<List<NewsItem>>> getSearchResults() {
        return searchResults;
    }

    public void setSearchQuery(@NonNull SearchFilter searchFilter) {
        query.setValue(searchFilter);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            query.postValue(query.getValue());
        }
    };

    public void refresh() {
        query.postValue(query.getValue());
    }

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(MySharedPreferences.SEARCHFILTERKEY)) {
                SearchFilter prefSearchFilter = mySharedPreferences.getSearchFilter();
                searchFilter.setSortBy(prefSearchFilter.getSortBy());
                searchFilter.setLang(prefSearchFilter.getLang());
                setSearchQuery(prefSearchFilter);
            }
        }
    };

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public SharedPreferences.OnSharedPreferenceChangeListener getOnPrefrenceChangeListener() {
        return sharedPreferenceChangeListener;
    }

}
