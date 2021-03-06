package sk.vander.contacts.data.provider;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import autodagger.AutoExpose;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import sk.vander.contacts.App;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.api.model.Order;
import sk.vander.contacts.data.api.model.request.ContactRequest;
import sk.vander.contacts.data.api.model.response.ListResponse;
import sk.vander.contacts.data.api.service.ContactService;
import sk.vander.contacts.data.api.service.OrderService;

/**
 * Created by arashid on 27/06/16.
 */
@ApplicationScope
@AutoExpose(App.class)
public class DataProvider {
  private final ContactService contactService;
  private final OrderService orderService;
  private final BehaviorSubject<Contact> selectedContact = BehaviorSubject.create();

  @Inject public DataProvider(ContactService contactService, OrderService orderService) {
    this.contactService = contactService;
    this.orderService = orderService;
  }

//  note for edge case: need to apply delay, cached onNext gets consumed by onError in enclosing flatMap, testable with no internet
  @RxLogObservable
  public Observable<List<Contact>> getContacts() {
    return Observable.concat(
        contactService.getContactsCached().map(ListResponse::items).onErrorResumeNext(Observable.empty()),
        Observable.<List<Contact>>empty().delay(1, TimeUnit.SECONDS),
        contactService.getContacts().map(ListResponse::items))
        .subscribeOn(Schedulers.io());
  }

  @RxLogObservable
  public Observable<List<Order>> getOrders() {
    return selectedContact.flatMap(c -> Observable.concat(
        orderService.getOrdersCached(c.id()).map(ListResponse::items).onErrorResumeNext(Observable.empty()),
        Observable.<List<Order>>empty().delay(1, TimeUnit.SECONDS),
        orderService.getOrders(c.id()).map(ListResponse::items)))
        .subscribeOn(Schedulers.io());
  }

  public BehaviorSubject<Contact> selectedContact() {
    return selectedContact;
  }

  public Observable<Contact> createContact(ContactRequest rc) {
    return contactService.createContact(rc).subscribeOn(Schedulers.io());
  }
}
