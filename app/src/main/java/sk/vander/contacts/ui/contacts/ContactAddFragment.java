package sk.vander.contacts.ui.contacts;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseFragment;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.data.api.model.request.ContactRequest;
import sk.vander.contacts.data.provider.DataProvider;

/**
 * A placeholder fragment containing a simple view.
 */
@AutoInjector(ContactAddActivity.class)
@LayoutId(R.layout.fragment_add_contact)
public class ContactAddFragment extends BaseFragment {
  @Inject DataProvider dataProvider;

  @BindView(R.id.input_name) EditText name;
  @BindView(R.id.input_layout_name) TextInputLayout nameLayout;
  @BindView(R.id.input_phone) EditText phone;
  @BindView(R.id.input_layout_phone) TextInputLayout phoneLayout;
  @BindView(R.id.btn_add) Button add;

  public ContactAddFragment() {
  }

  @Override protected void onInject() {
    DaggerService.<ContactAddActivityComponent>getDaggerComponent(getContext()).inject(this);
  }

  @Override public void onResume() {
    super.onResume();
    subscription.add(Observable.combineLatest(
        RxTextView.afterTextChangeEvents(name).skip(1).map(ev -> validate(ev.editable().toString(), nameLayout)),
        RxTextView.afterTextChangeEvents(phone).skip(1).map(ev -> validate(ev.editable().toString(), phoneLayout)),
        (f, s) -> f && s)
        .startWith(false)
        .subscribe(add::setEnabled));

    subscription.add(RxView.clicks(add)
        .map(x -> ContactRequest.create(name.getText().toString(), phone.getText().toString()))
        .doOnNext(x -> getBaseActivity().showProgress(true))
        .flatMap(rc -> dataProvider.createContact(rc)
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(t -> onError(t).map(x -> null)))
        // TODO: 28/06/16 refresh data
        .doOnEach(x -> getBaseActivity().showProgress(false))
        .subscribe(x -> screenSwitcher.goBack(), Throwable::printStackTrace)
    );
  }

  private boolean validate(String t, TextInputLayout til) {
    if (TextUtils.isEmpty(t)) {
      til.setError(getString(R.string.validation_non_empty));
      return false;
    } else if (t.length() < 5) {
      til.setError(getString(R.string.validation_min_char));
      return false;
    } else {
      til.setError("");
      til.setErrorEnabled(false);
    }
    return true;
  }
}
