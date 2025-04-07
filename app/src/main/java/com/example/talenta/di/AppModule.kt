package com.example.talenta.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.talenta.data.Utilities
import com.example.talenta.data.repository.ArtistRepository
import com.example.talenta.data.repository.ExpertScreenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_data")
        }
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()


    @Named("users")
    @Provides
    @Singleton
    fun provideUserCollection(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("users")
    }

    @Provides
    @Singleton
    fun provideUtilities(context: Context, auth: FirebaseAuth): Utilities {
        return Utilities(context, auth)
    }

    @Provides
    @Singleton
    fun provideArtistRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        utilities: Utilities
    ): ArtistRepository {
        return ArtistRepository(firestore, storage, utilities)
    }

    @Provides
    @Singleton
    fun provideExpertRepository(
        firestore: FirebaseFirestore,
    ): ExpertScreenRepository {
        return ExpertScreenRepository(firestore)
    }


}