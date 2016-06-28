package sk.vander.contacts.data.api.model;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class Order {
  public abstract String name();
  public abstract int count();
  public abstract String kind();

  public static Order create(String name, int count, String kind) {
    return new AutoParcelGson_Order(name, count, kind);
  }
}
