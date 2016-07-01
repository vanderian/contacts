package sk.vander.contacts.ui.contacts;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseFragment;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.adapter.ListSource;
import sk.vander.contacts.base.adapter.ObservableAdapter;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.navigation.activity.ActivityUriScreen;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.provider.DataProvider;
import sk.vander.contacts.misc.SwipeRefreshObservable;
import sk.vander.contacts.ui.contacts.adapter.ContactItemView;
import sk.vander.contacts.ui.contacts.adapter.ContactsSource;

/**
 * A placeholder fragment containing a simple view.
 */
@AutoInjector(ContactListActivity.class)
@LayoutId(R.layout.fragment_contacts)
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

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter.setHasStableIds(true);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    refreshLayout.setColorSchemeResources(R.color.red_500, R.color.yellow_500, R.color.green_500);
  }

  @Override public void onResume() {
    super.onResume();
    subscription.add(RxView.clicks(fab)
        .map(x -> ActivityUriScreen.newBuilder().withUri(Uri.parse(getString(R.string.nav_contact_add)))
//            .withTransitionView(((ContactListActivity) getActivity()).appBar, "toolbar")
            .build())
        .subscribe(screenSwitcher::open));
//        .subscribe(x -> getBaseActivity().showProgress(true)));

    final ConnectableObservable<List<Contact>> o = SwipeRefreshObservable.create(refreshLayout)
        .startWith(new Object())
        .flatMap(x -> dataProvider.getContacts()
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(t -> onError(t).map(er -> Collections.emptyList())))
        .doOnEach(x -> refreshLayout.setRefreshing(false))
        .filter(l -> !l.isEmpty())
        .doOnNext(source::setList)
        .publish();
    subscription.add(o.subscribe(x -> adapter.notifyDataSetChanged(), Throwable::printStackTrace));
    refreshLayout.post(() -> {
      refreshLayout.setRefreshing(true);
      o.connect();
    });

    subscription.add(adapter.onItemClicked()
//        .map(ObservableAdapter.ViewHolder::getItem)
        .doOnNext(vh -> dataProvider.selectedContact().onNext(vh.getItem()))
        .map(vh -> ActivityUriScreen.newBuilder()
            .withTransitionView(((ContactItemView) vh.itemView).phone, "phone")
            .withTransitionView(getBaseActivity().getAppBar(), "appBar")
            .withUri(Uri.parse(getString(R.string.nav_contact_order)))
            .build())
        .subscribe(screenSwitcher::open));
  }
}
