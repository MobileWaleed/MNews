package loovo.com.mnews.di.component;


import javax.inject.Singleton;

import dagger.Component;
import loovo.com.mnews.di.module.AppContextModule;
import loovo.com.mnews.di.module.AppModule;
import loovo.com.mnews.di.module.RoomModule;
import loovo.com.mnews.di.module.SharedModule;
import loovo.com.mnews.view.activities.NewsListActivity;
import loovo.com.mnews.view.fragments.FilterSearchResultDialogFragment;


@Singleton
@Component(modules = {
        AppModule.class,
        RoomModule.class,
        AppContextModule.class,
        SharedModule.class,

})
public interface AppComponent {

    void inject(NewsListActivity newsListActivity);

    void inject(FilterSearchResultDialogFragment filterFragment);
}
