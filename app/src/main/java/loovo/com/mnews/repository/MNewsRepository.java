package loovo.com.mnews.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import loovo.com.mnews.MNewsApp;
import loovo.com.mnews.db.MNewsDb;
import loovo.com.mnews.db.dao.NewsDao;
import loovo.com.mnews.db.entity.NewsItem;
import loovo.com.mnews.repository.api.MNewsService;
import loovo.com.mnews.repository.api.NewsResult;
import loovo.com.mnews.repository.model.ApiResponse;
import loovo.com.mnews.repository.model.SearchFilter;
import loovo.com.mnews.repository.util.AppExecutors;
import loovo.com.mnews.repository.util.NetworkBoundResource;
import loovo.com.mnews.utils.Resource;
import timber.log.Timber;

import static loovo.com.mnews.utils.Utils.convertSearchFilterToHashMap;

/**
 * This class helps manage communication from repository to ViewModels
 */

@Singleton
public class MNewsRepository {

    @Inject
    MNewsService mNewsService;
    @Inject
    MNewsDb mNewsDb;
    @Inject
    NewsDao mNewsDao;

    private final AppExecutors mAppExecutors;

    @Inject
    public MNewsRepository(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public LiveData<Resource<List<NewsItem>>> searchNews(SearchFilter query) {
        return new NetworkBoundResource<List<NewsItem>, NewsResult>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull NewsResult item) {
                mNewsDao.deleteAll();
                mNewsDao.insertNews(item.getResults());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<NewsItem> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<NewsItem>> loadFromDb() {
                return mNewsDao.searchNewsItemByTitle();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NewsResult>> createCall() {
                return mNewsService.searchNews(convertSearchFilterToHashMap(query));
            }
        }.asLiveData();
    }

}
