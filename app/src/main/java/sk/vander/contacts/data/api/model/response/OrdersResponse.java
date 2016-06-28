package sk.vander.contacts.data.api.model.response;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import sk.vander.contacts.data.api.model.Order;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class OrdersResponse extends ListResponse<Order> {
  public abstract List<Order> items();

  public static OrdersResponse create(String kind, String etag, List<Order> items) {
    return new AutoParcelGson_OrdersResponse(kind, etag, items);
  }
}
