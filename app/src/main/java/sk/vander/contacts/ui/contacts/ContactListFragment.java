package sk.vander.contacts.ui.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Collections;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseFragment;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.adapter.ListSource;
import sk.vander.contacts.base.adapter.ObservableAdapter;
import sk.vander.contacts.base.navigation.activity.ActivityUriScreen;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.provider.DataProvider;
import sk.vander.contacts.misc.SwipeRefreshObservable;
import sk.vander.contacts.ui.contacts.adapter.ContactsSource;

/**
 * A placeholder fragment containing a simple view.
 */
@AutoInjector(ContactListActivity.class)
public class ContactListFragment extends BaseFragment {
  @Inject DataProvider dataProvider;
  private final ListSource<Contact> source = new ContactsSource();
  private final ObservableAdapter<Contact> adapter = new ObservableAdapter<>(source);

  @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
  @BindView(R.id.list) RecyclerView recyclerView;
  @BindView(R.id.fab) FloatingActionButton fab;

  public ContactListFragment() {
  }

  @Override protected void onInject() {
    DaggerService.<ContactListActivityComponent>getDaggerComponent(getContext()).inject(this);
  }

  @Override protected int layoutId() {
    return R.layout.fragment_contacts;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    refreshLayout.setColorSchemeResources(R.color.green_500, R.color.amber_500, R.color.indigo_500, R.color.red_500);
  }

  @Override public void onResume() {
    super.onResume();
    subscription.add(RxView.clicks(fab)
        .map(x -> ActivityUriScreen.withUri(ContactAddActivity.URI))
        .subscribe(screenSwitcher::open));

    subscription.add(SwipeRefreshObservable.create(refreshLayout)
        .flatMap(x -> dataProvider.getContacts()
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(t -> onError(t).map(er -> Collections.emptyList())))
        .doOnNext(source::setList)
        .doOnEach(x -> refreshLayout.setRefreshing(false))
        .subscribe(x -> adapter.notifyDataSetChanged(), Throwable::printStackTrace));
  }
}
