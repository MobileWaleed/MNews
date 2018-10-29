package loovo.com.mnews.repository.api;

import android.arch.lifecycle.LiveData;


import java.util.Map;

import loovo.com.mnews.repository.model.ApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface MNewsService {
    
    @GET("everything")
    LiveData<ApiResponse<NewsResult>> getAllNews();

    @GET("everything/")
    LiveData<ApiResponse<NewsResult>> searchNews(@QueryMap Map<String, String> options);

}
