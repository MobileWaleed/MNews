package loovo.com.mnews.di.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import loovo.com.mnews.repository.api.MNewsService;
import retrofit2.Retrofit;


@Module(includes = {
        ViewModelModule.class,
        NetworkModule.class
})
public class AppModule {

    @Provides
    @Singleton
    MNewsService provideNewsService(Retrofit retrofit) {
        return retrofit.create(MNewsService.class);
    }

}
