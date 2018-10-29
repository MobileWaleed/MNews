package loovo.com.mnews.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import loovo.com.mnews.db.dao.NewsDao;
import loovo.com.mnews.db.entity.NewsItem;


/**
 * Main database description.
 */
@Database(entities = {NewsItem.class}, version = 1)
public abstract class MNewsDb extends RoomDatabase {

    abstract public NewsDao newsDao();

}
