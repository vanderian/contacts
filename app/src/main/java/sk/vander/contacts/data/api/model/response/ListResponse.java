package sk.vander.contacts.data.api.model.response;

import java.util.List;

/**
 * Created by arashid on 24/06/16.
 */
public abstract class ListResponse<T> {
  public abstract List<T> items();
  public abstract String kind();
  public abstract String etag();
}
