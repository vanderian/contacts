package sk.vander.contacts.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Collections;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseFragment;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.adapter.ListSource;
import sk.vander.contacts.base.adapter.ObservableAdapter;
import sk.vander.contacts.base.navigation.activity.ActivityUriScreen;
import sk.vander.contacts.data.api.model.Order;
import sk.vander.contacts.data.provider.DataProvider;
import sk.vander.contacts.misc.SwipeRefreshObservable;
import sk.vander.contacts.ui.contacts.adapter.OrderSource;

/**
 * A placeholder fragment containing a simple view.
 */
@AutoInjector(ContactOrderActivity.class)
public class ContactOrderFragment extends BaseFragment {
  @Inject DataProvider dataProvider;
  private final ListSource<Order> source = new OrderSource();
  private final ObservableAdapter<Order> adapter = new ObservableAdapter<>(source);

  @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
  @BindView(R.id.list) RecyclerView recyclerView;
  @BindView(R.id.frame_header) View header;

  public ContactOrderFragment() {
  }

  @Override protected void onInject() {
    DaggerService.<ContactOrderActivityComponent>getDaggerComponent(getContext()).inject(this);
  }

  @Override protected int layoutId() {
    return R.layout.fragment_orders;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    refreshLayout.setColorSchemeResources(R.color.green_500, R.color.amber_500, R.color.indigo_500, R.color.red_500);
  }

  @Override public void onResume() {
    super.onResume();
    subscription.add(Observable.combineLatest(RxView.clicks(header), dataProvider.getSelectedContact(), (x, c) -> c)
        .map(c -> ActivityUriScreen.newBuilder()
            .withAction(Intent.ACTION_CALL)
            .withUri(Uri.parse("tel://" + c.phone()))
//            .withTransitionView(((ContactListActivity) getActivity()).toolbar, "toolbar")
            .build())
        .subscribe(screenSwitcher::open));

    subscription.add(SwipeRefreshObservable.create(refreshLayout)
        .flatMap(x -> dataProvider.getOrders()
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(t -> onError(t).map(er -> Collections.emptyList())))
        .doOnNext(source::setList)
        .doOnEach(x -> refreshLayout.setRefreshing(false))
        .subscribe(x -> adapter.notifyDataSetChanged(), Throwable::printStackTrace));
  }
}
