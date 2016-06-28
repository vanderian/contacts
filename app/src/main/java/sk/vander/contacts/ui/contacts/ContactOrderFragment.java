package sk.vander.contacts.ui.contacts;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.navigation.activity.ActivityScreen;
import sk.vander.contacts.base.navigation.activity.ActivityUriScreen;
import sk.vander.contacts.data.api.model.Order;
import sk.vander.contacts.data.provider.DataProvider;
import sk.vander.contacts.misc.RuntimePermissions;
import sk.vander.contacts.misc.SwipeRefreshObservable;
import sk.vander.contacts.ui.contacts.adapter.OrderSource;

/**
 * A placeholder fragment containing a simple view.
 */
@AutoInjector(ContactOrderActivity.class)
@LayoutId(R.layout.fragment_orders)
public class ContactOrderFragment extends BaseFragment {
  @Inject DataProvider dataProvider;
  private final ListSource<Order> source = new OrderSource();
  private final ObservableAdapter<Order> adapter = new ObservableAdapter<>(source);

  @BindView(R.id.refresh) SwipeRefreshLayout refreshLayout;
  @BindView(R.id.list) RecyclerView recyclerView;
  @BindView(R.id.frame_header) View header;
  @BindView(R.id.phone) TextView phone;

  public ContactOrderFragment() {
  }

  @Override protected void onInject() {
    DaggerService.<ContactOrderActivityComponent>getDaggerComponent(getContext()).inject(this);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ActivityScreen.setTransitionView(phone, "phone");

    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary)
    refreshLayout.setColorSchemeResources(R.color.red_500, R.color.yellow_500, R.color.green_500);
  }

  @Override public void onResume() {
    super.onResume();
    subscription.add(Observable.combineLatest(RxView.clicks(header),
        dataProvider.selectedContact().doOnNext(c -> phone.setText(c.phone())),
        (x, c) -> c)
        .filter(c -> RuntimePermissions.requestPermissions(getActivity(), Manifest.permission.CALL_PHONE))
        .flatMap(c -> snack(Snackbar.make(getView(), "Make call?", Snackbar.LENGTH_SHORT)).map(s -> c))
        .map(c -> ActivityUriScreen.newBuilder()
            .withAction(Intent.ACTION_CALL)
            .withUri(Uri.parse("tel://" + c.phone()))
//            .withTransitionView(((ContactListActivity) getActivity()).toolbar, "toolbar")
            .build())
        .subscribe(screenSwitcher::open, Throwable::printStackTrace));

    subscription.add(SwipeRefreshObservable.create(refreshLayout)
        .flatMap(x -> dataProvider.getOrders()
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(t -> onError(t).map(er -> Collections.emptyList())))
        .doOnNext(source::setList)
        .doOnEach(x -> refreshLayout.setRefreshing(false))
        .subscribe(x -> adapter.notifyDataSetChanged(), Throwable::printStackTrace));
  }

  private Observable<Snackbar> snack(Snackbar snackbar) {
    return Observable.create(subscriber -> {
      snackbar.setAction("Call", v -> {
        subscriber.onNext(snackbar);
        subscriber.onCompleted();
      }).show();
    });
  }
}
