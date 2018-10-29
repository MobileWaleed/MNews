package loovo.com.mnews.repository.model;

import android.content.SharedPreferences;
import android.text.style.UpdateAppearance;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

public class MySharedPreferences {

    private SharedPreferences mSharedPreferences;
    public static String SEARCHFILTERKEY="search_filter_key";
    public static String LASTUPDATEKEY="last_update_key";
    @Inject
    Gson gson;
    @Inject
    public MySharedPreferences(SharedPreferences mSharedPreferences) {
        System.out.println("new my sharedprefrence created");
        this.mSharedPreferences = mSharedPreferences;
    }

    public void putSearchFilter(SearchFilter searchFilter) {
        System.out.println("new search filter added");
        String json = gson.toJson(searchFilter);
        mSharedPreferences.edit().putString(SEARCHFILTERKEY,json).apply();
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
    public boolean isUpdateDateAvailable()
    {
        Long date = mSharedPreferences.getLong(LASTUPDATEKEY, 0);
        if(date == 0)
        {
            return false;
        }else
        {
            return true;
        }
    }
}