package loovo.com.mnews.helpers;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Date;

import javax.inject.Inject;

import loovo.com.mnews.repository.model.SearchFilter;

public class MySharedPreferences {

    private SharedPreferences mSharedPreferences;
    public static String SEARCHFILTERKEY = "search_filter_key";
    public static String LASTUPDATEKEY = "last_update_key";
    @Inject
    Gson gson;

    @Inject
    public MySharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void putSearchFilter(SearchFilter searchFilter) {
        String json = gson.toJson(searchFilter);
        mSharedPreferences.edit().putString(SEARCHFILTERKEY, json).apply();
    }

    public SearchFilter getSearchFilter() {
        String json = mSharedPreferences.getString(SEARCHFILTERKEY, "");
        SearchFilter obj = gson.fromJson(json, SearchFilter.class);
        return obj;
    }

    public Date getDate() {
        Date myDate = new Date(mSharedPreferences.getLong(LASTUPDATEKEY, 0));
        return myDate;
    }

    public void putDate(Date date) {
        mSharedPreferences.edit().putLong(LASTUPDATEKEY, date.getTime()).apply();
    }

    public boolean isUpdateDateAvailable() {
        Long date = mSharedPreferences.getLong(LASTUPDATEKEY, 0);
        if (date == 0) {
            return false;
        } else {
            return true;
        }
    }
}