package sk.vander.contacts.ui.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import sk.vander.contacts.R;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.adapter.ListSource;
import sk.vander.contacts.base.adapter.ObservableAdapter;
import sk.vander.contacts.data.api.error.RetrofitException;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.provider.DataProvider;
import sk.vander.contacts.misc.SwipeRefreshObservable;
import sk.vander.contacts.ui.contacts.adapter.ContactsSource;

/**
 * A placeholder fragment containing a simple view.
 */
@AutoInjector(ContactListActivity.class)
public class ContactListFragment extends Fragment {
  private final CompositeSubscription subscription = new CompositeSubscription();
  @Inject DataProvider dataProvider;
  private final ListSource<Contact> source = new ContactsSource();
  private final ObservableAdapter<Contact> adapter = new ObservableAdapter<>(source);

  @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
  @BindView(R.id.list) RecyclerView recyclerView;

  public ContactListFragment() {
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    DaggerService.<ContactListActivityComponent>getDaggerComponent(context).inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    refreshLayout.setColorSchemeResources(R.color.green_500, R.color.amber_500, R.color.indigo_500, R.color.red_500);
  }

  @Override public void onResume() {
    super.onResume();
    subscription.add(SwipeRefreshObservable.create(refreshLayout)
        .flatMap(x -> dataProvider.getContacts()
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(this::onError))
        .doOnNext(source::setList)
        .doOnEach(x ->refreshLayout.setRefreshing(false))
        .subscribe(x -> adapter.notifyDataSetChanged(), Throwable::printStackTrace));
  }

  private <T> Observable<List<T>> onError(Throwable throwable) {
    return Observable.just(throwable)
        .ofType(RetrofitException.class)
        .flatMap(RetrofitException::getAsErrorResponse)
        .doOnNext(er -> Snackbar.make(getView(), er.error().message(), Snackbar.LENGTH_SHORT).show())
        .map(x -> Collections.emptyList());
  }

  @Override public void onPause() {
    super.onPause();
    subscription.clear();
  }
}
