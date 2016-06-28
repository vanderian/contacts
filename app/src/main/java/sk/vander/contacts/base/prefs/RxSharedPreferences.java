package sk.vander.contacts.base.prefs;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.StringRes;

import com.jakewharton.rxbinding.internal.Preconditions;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public final class RxSharedPreferences {
  public static RxSharedPreferences create(Resources resources, SharedPreferences sharedPreferences) {
    return new RxSharedPreferences(resources, sharedPreferences);
  }

  private final SharedPreferences sharedPreferences;
  private final Observable<String> changedKeys;
  private final Resources resources;

  private RxSharedPreferences(Resources resources, final SharedPreferences sharedPreferences) {
    this.resources = resources;
    this.sharedPreferences = Preconditions.checkNotNull(sharedPreferences, "sharedPreferences == null");
    this.changedKeys = Observable.<String>create(
        subscriber -> {
          final OnSharedPreferenceChangeListener listener = (sharedPreferences1, key) -> subscriber.onNext(key);

          Subscription subscription = Subscriptions.create(() ->
              sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));
          subscriber.add(subscription);

          sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        }
    ).share();
  }

  public Observable<String> getString(@StringRes int key) {
    return getString(key, null);
  }

  public Observable<String> getString(@StringRes int key, String defaultValue) {
    return getString(resources.getString(key), defaultValue);
  }

  public Observable<String> getString(String key) {
    return getString(key, null);
  }

  public Observable<String> getString(String key, final String defaultValue) {
    return changedKeys.filter(matchesKey(key))
        .startWith(key)
        .map(changedKey -> sharedPreferences.getString(changedKey, defaultValue));
  }

  public Action1<String> setString(final String key) {
    return value -> sharedPreferences.edit().putString(key, value).apply();
  }

  public Observable<Boolean> getBoolean(@StringRes int key) {
    return getBoolean(key, null);
  }

  public Observable<Boolean> getBoolean(@StringRes int key, Boolean defaultValue) {
    return getBoolean(resources.getString(key), defaultValue);
  }

  public Observable<Boolean> getBoolean(String key) {
    return getBoolean(key, null);
  }

  public Observable<Boolean> getBoolean(String key, final Boolean defaultValue) {
    return changedKeys.filter(matchesKey(key))
        .startWith(key)
        .map(changedKey -> sharedPreferences.getBoolean(changedKey, defaultValue));
  }

  public Action1<Boolean> setBoolean(final String key) {
    return value -> sharedPreferences.edit().putBoolean(key, value).apply();
  }

  public Observable<Integer> getInt(@StringRes int key) {
    return getInt(key, null);
  }

  public Observable<Integer> getInt(@StringRes int key, Integer defaultValue) {
    return getInt(resources.getString(key), defaultValue);
  }

  public Observable<Integer> getInt(String key) {
    return getInt(key, null);
  }

  public Observable<Integer> getInt(String key, final Integer defaultValue) {
    return changedKeys.filter(matchesKey(key))
        .startWith(key)
        .map(changedKey -> sharedPreferences.getInt(changedKey, defaultValue));
  }

  public Observable<String> observeChangedFromResouces(Integer... keys) {
    return observeChangedFromResouces(Arrays.asList(keys));
  }

  public Observable<String> observeChangedFromResouces(List<Integer> keys) {
    final List<String> strings = Observable.from(keys).map(resources::getString).toList().toBlocking().first();
    return changedKeys.filter(strings::contains);
  }

  public Observable<String> observeChanged(List<String> keys) {
    return changedKeys.filter(keys::contains);
  }

  private static Func1<String, Boolean> matchesKey(final String key) {
    return value -> key.equals(value);
  }
}
