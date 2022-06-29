package home.bthayes1.navigationbar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import home.bthayes1.navigationbar.BaseApplication
import home.bthayes1.navigationbar.repository.AuthenticationRepoImpl
import home.bthayes1.navigationbar.repository.AuthenticationRepository
import home.bthayes1.navigationbar.repository.FirestoreRepoImpl
import home.bthayes1.navigationbar.repository.FirestoreRepository
import javax.inject.Singleton


@Module
@InstallIn (SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository() : AuthenticationRepository{
        return AuthenticationRepoImpl()
    }

    @Singleton
    @Provides
    fun provideFirestoreRepository() : FirestoreRepository{
        return FirestoreRepoImpl()
    }
}