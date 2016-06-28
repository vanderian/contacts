package sk.vander.contacts.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import sk.vander.contacts.base.navigation.activity.ActivityScreenSwitcher;
import sk.vander.contacts.data.api.error.ResponseError;
import sk.vander.contacts.data.api.error.RetrofitException;

public abstract class BaseFragment extends Fragment {
  protected final CompositeSubscription subscription = new CompositeSubscription();
  @Inject protected ActivityScreenSwitcher screenSwitcher;

  public BaseFragment() {
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    onInject();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(layoutId(), container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  protected Observable<ResponseError> onError(Throwable throwable) {
    return Observable.just(throwable)
        .ofType(RetrofitException.class)
        .flatMap(RetrofitException::getAsErrorResponse)
//        .doOnNext(er -> Snackbar.make(getView(), er.error().message(), Snackbar.LENGTH_SHORT).show())
        .doOnNext(er -> Toast.makeText(getContext(), er.error().message(), Toast.LENGTH_SHORT).show());
  }

  @Override public void onPause() {
    super.onPause();
    subscription.clear();
  }

  protected abstract void onInject();
  @LayoutRes protected abstract int layoutId();
}