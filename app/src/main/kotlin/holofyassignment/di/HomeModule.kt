package holofyassignment.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import holofyassignment.adapters.HomeAdapter

@Module
@InstallIn(FragmentComponent::class)
object HomeModule {



    @Provides
    fun providesCallback(fragment: Fragment): HomeAdapter.Callback {
        return fragment as HomeAdapter.Callback
    }
}