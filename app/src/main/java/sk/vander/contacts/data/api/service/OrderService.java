package sk.vander.contacts.data.api.service;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import sk.vander.contacts.data.api.model.ResponseOrders;

/**
 * Created by arashid on 27/06/16.
 */
public interface OrderService {
  @GET("_ah/api/orderendpoint/v1/order/{id}") Observable<ResponseOrders> getOrders(@Path("id") String contactId);
}
