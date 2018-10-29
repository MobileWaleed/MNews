package loovo.com.mnews.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        AppContextModule.class
})
public class SharedModule {
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("PrefName", Context.MODE_PRIVATE);
    }

}