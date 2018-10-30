package loovo.com.mnews.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import loovo.com.mnews.di.qualifires.ViewModelKey;
import loovo.com.mnews.viewmodel.NewsListViewModel;
import loovo.com.mnews.viewmodel.ProjectViewModelFactory;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsListViewModel.class)
    abstract ViewModel bindNewsListViewModel(NewsListViewModel newsListViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ProjectViewModelFactory projectViewModelFactory);
}
