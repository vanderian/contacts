package sk.vander.contacts.data.api.service;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.api.model.RequestContact;
import sk.vander.contacts.data.api.model.ResponseContacts;

/**
 * Created by arashid on 27/06/16.
 */
public interface ContactService {
  @GET("contactendpoint/v1/contact") Observable<ResponseContacts> getContacts();

  @Headers("application/json")
  @POST("contactendpoint/v1/contact") Observable<Contact> createContact(@Body RequestContact contact);
}
