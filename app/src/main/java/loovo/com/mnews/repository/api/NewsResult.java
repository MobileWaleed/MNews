package loovo.com.mnews.repository.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import loovo.com.mnews.db.entity.NewsItem;


public class NewsResult {
    @SerializedName(value = "status")
    private String status;
    @SerializedName(value = "articles")
    private List<NewsItem> results = new ArrayList<>();
    @SerializedName(value = "totalResults")
    private Integer totalResults;


    public List<NewsItem> getResults() {
        return results;
    }

    public void setResults(List<NewsItem> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

