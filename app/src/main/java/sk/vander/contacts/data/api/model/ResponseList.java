package sk.vander.contacts.data.api.model;

import java.util.List;

/**
 * Created by arashid on 24/06/16.
 */
public abstract class ResponseList<T> {
  public abstract List<T> items();
  public abstract String kind();
  public abstract String etag();
}
