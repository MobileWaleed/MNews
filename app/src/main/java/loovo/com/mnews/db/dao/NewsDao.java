package loovo.com.mnews.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import loovo.com.mnews.db.entity.NewsItem;


@Dao
public abstract class NewsDao {

    @Query("SELECT * FROM NewsItem")
    public abstract LiveData<List<NewsItem>> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(NewsItem... newsItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertNews(List<NewsItem> newsItemList);

    @Query("DELETE FROM NewsItem")
    public abstract void deleteAll();

    @Query("SELECT * FROM NewsItem")
    public abstract LiveData<List<NewsItem>> searchNewsItemByTitle();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createNewsItemIfNotExists(NewsItem newsItem);
}
