package sk.vander.contacts.data.api.model;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class RequestContact {
  public abstract String name();
  public abstract String phone();

  public static RequestContact create(String name, String phone) {
    return new AutoParcelGson_RequestContact(name, phone);
  }
}
