package sk.vander.contacts.base.prefs;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectPreference<T> {
    private final SharedPreferences preferences;
    private final String key;
    private final T defaultValue;
    private final Class<T> clazz;
    private final Gson gson;

    public ObjectPreference(SharedPreferences preferences, String key, Class<T> clazz) {
        this(preferences, key, new GsonBuilder().create(), clazz, null);
    }

    public ObjectPreference(SharedPreferences preferences, String key, Class<T> clazz, T defaultValue) {
        this(preferences, key, new GsonBuilder().create(), clazz, defaultValue);
    }

    public ObjectPreference(SharedPreferences preferences, String key, Gson gson, Class<T> clazz) {
        this(preferences, key, gson, clazz, null);
    }

    public ObjectPreference(SharedPreferences preferences, String key, Gson gson, Class<T> clazz, T defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.gson = gson;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    public T get() {
        final String res = preferences.getString(key, defaultValue == null ? null : gson.toJson(defaultValue));

        if(res != null){
            return gson.fromJson(res, clazz);
        }

        return null;
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(T value) {
        preferences.edit().putString(key, gson.toJson(value)).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}
