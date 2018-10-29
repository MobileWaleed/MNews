package loovo.com.mnews;

import android.app.Application;

import loovo.com.mnews.di.component.AppComponent;
import loovo.com.mnews.di.component.DaggerAppComponent;
import loovo.com.mnews.di.module.AppContextModule;
import timber.log.Timber;


public class MNewsApp extends Application {
    private static MNewsApp app;

    public static MNewsApp getApp() {
        return app;
    }

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        app = this;
        initDataComponent();
    }

    private void initDataComponent() {
        appComponent = DaggerAppComponent.builder().appContextModule(new AppContextModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
