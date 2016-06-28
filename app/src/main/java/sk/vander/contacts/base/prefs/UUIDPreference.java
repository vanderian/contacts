package sk.vander.contacts.base.prefs;

import android.content.SharedPreferences;

import java.util.UUID;

import rx.functions.Func0;

public class UUIDPreference {
  private final SharedPreferences preferences;
  private final String key;
  private final Func0<UUID> factory;

  public UUIDPreference(SharedPreferences preferences, String key) {
    this(preferences, key, UUID::randomUUID);
  }

  public UUIDPreference(SharedPreferences preferences, String key, Func0<UUID> factory) {
    this.preferences = preferences;
    this.key = key;
    this.factory = factory;
  }

  public UUID get() {
    return UUID.fromString(preferences.getString(key, factory.call().toString()));
  }

  public boolean isSet() {
    return preferences.contains(key);
  }

  public void set(UUID value) {
    preferences.edit().putString(key, value.toString()).apply();
  }

  public void delete() {
    preferences.edit().remove(key).apply();
  }
}
