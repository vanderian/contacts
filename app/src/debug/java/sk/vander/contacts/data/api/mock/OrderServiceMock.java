package sk.vander.contacts.data.api.mock;

import java.util.Arrays;

import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;
import rx.Observable;
import sk.vander.contacts.data.api.model.Order;
import sk.vander.contacts.data.api.model.response.OrdersResponse;
import sk.vander.contacts.data.api.service.OrderService;

/**
 * Created by arashid on 28/06/16.
 */
public class OrderServiceMock implements OrderService {
  private final BehaviorDelegate<OrderService> delegate;

  public OrderServiceMock(BehaviorDelegate<OrderService> delegate) {
    this.delegate = delegate;
  }

  @Override public Observable<OrdersResponse> getOrdersCached(@Path("id") String contactId) {
    return getOrders(contactId);
  }

  @Override public Observable<OrdersResponse> getOrders(@Path("id") String contactId) {
    final Order o1 = Order.create("HDD", 3, "tag");
    final Order o2 = Order.create("RAM", 30, "tag");
    final Order o3 = Order.create("Notebook", 123, "tag");
    final OrdersResponse ro = OrdersResponse.create("list", "tag", Arrays.asList(o1, o2, o3));
    return delegate.returningResponse(ro).getOrders(contactId);
  }
}
