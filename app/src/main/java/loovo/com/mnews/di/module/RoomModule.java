package loovo.com.mnews.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import loovo.com.mnews.db.MNewsDb;
import loovo.com.mnews.db.dao.NewsDao;


@Module(includes = {
        AppContextModule.class
})
public class RoomModule {

    @Singleton
    @Provides
    MNewsDb providesRoomDatabase(Application app) {
        return Room.databaseBuilder(app, MNewsDb.class, "mnews_db").build();
    }

    @Singleton
    @Provides
    NewsDao provideNewsDao(MNewsDb db) {
        return db.newsDao();
    }

}
