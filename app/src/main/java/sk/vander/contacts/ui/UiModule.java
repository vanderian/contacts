package sk.vander.contacts.ui;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import sk.vander.contacts.base.BaseUiModule;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by arashid on 26/06/16.
 */
@Module(includes = BaseUiModule.class)
public class UiModule {

  @Provides @ApplicationScope Picasso providePicasso(@ApplicationScope Context ctx, OkHttpClient okHttpClient) {
    return new Picasso.Builder(ctx)
        .downloader(new OkHttp3Downloader(okHttpClient))
        .build();
  }
}
