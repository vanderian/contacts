package sk.vander.contacts.data.provider;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoExpose;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import sk.vander.contacts.App;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.api.model.RequestContact;
import sk.vander.contacts.data.api.model.ResponseList;
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

  public Observable<List<Contact>> getContacts() {
    return contactService.getContacts().map(ResponseList::items).subscribeOn(Schedulers.io());
  }

//  public Observable<List<Order>> getOrders() {
//    return selectedContact.flatMap(c -> orderService.getOrders(c.id())).map(ResponseList::items).subscribeOn(Schedulers.io());
//  }

  public Observable<Contact> getSelectedContact() {
    return selectedContact.asObservable();
  }

  public Func1<RequestContact, Observable<Contact>> createContact() {
    return rc -> contactService.createContact(rc).subscribeOn(Schedulers.io());
  }
}
