package sk.vander.contacts.base;

import android.app.Activity;
import android.content.Context;

import dagger.Provides;
import sk.vander.contacts.base.annotation.ActivityScope;

/**
 * Created by arashid on 26/06/16.
 */
@dagger.Module
@ActivityScope
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @ActivityScope Context provideContext() {
    return activity;
  }
}
