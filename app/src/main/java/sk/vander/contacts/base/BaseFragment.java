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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.navigation.activity.ActivityScreenSwitcher;
import sk.vander.contacts.data.api.error.RetrofitException;
import sk.vander.contacts.misc.Utils;
import timber.log.Timber;

public abstract class BaseFragment extends Fragment {
  private static final Map<String, Integer> INT_CACHE = new LinkedHashMap<>();

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

  protected Observable<String> onError(Throwable throwable) {
    return Observable.just(throwable)
        .doOnNext(t -> Timber.d(t, "BaseFragment: Api Error"))
        .ofType(RetrofitException.class)
        .flatMap(ex -> {
          if (RetrofitException.Kind.NETWORK.equals(ex.getKind())) {
            return Observable.just(ex.getMessage());
          }
          if (RetrofitException.Kind.HTTP.equals(ex.getKind())) {
            return ex.getAsErrorResponse().map(er -> er.error().message()).onErrorResumeNext(Observable.just(ex.getMessage()));
          }
//          Unexpected error -> fail
          return Observable.error(ex);
        })
//        .doOnNext(er -> Snackbar.make(getView(), er.error().message(), Snackbar.LENGTH_SHORT).show())
        .doOnError(t -> Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show())
        .doOnNext(er -> Toast.makeText(getContext(), er, Toast.LENGTH_SHORT).show());
  }

  @Override public void onPause() {
    super.onPause();
    subscription.clear();
  }

  @LayoutRes protected int layoutId() {
    return Utils.getAnnotationValue(getClass(), INT_CACHE, LayoutId.class);
  }

  public BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }

  protected abstract void onInject();
}
