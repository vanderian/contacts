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
}
