package sk.vander.contacts.data;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.base.prefs.BooleanPreference;
import sk.vander.contacts.data.api.DebugApiModule;
import sk.vander.contacts.data.api.MockMode;

/**
 * Created by arashid on 27/06/16.
 */
@Module(includes = {DataModule.class, DebugApiModule.class})
public class DebugDataModule {

  @Provides @ApplicationScope @MockMode BooleanPreference provideMockPreference(SharedPreferences sp) {
    return new BooleanPreference(sp, "pref_key_mock_mod", true);
  }
}
