package sk.vander.contacts.ui.contacts.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import sk.vander.contacts.R;
import sk.vander.contacts.base.view.BindableView;
import sk.vander.contacts.data.api.ApiModule;
import sk.vander.contacts.data.api.model.Contact;

/**
 * Created by arashid on 27/06/16.
 */
public class ContactItemView extends RelativeLayout implements BindableView<Contact> {
  @BindView(R.id.name) TextView name;
  @BindView(R.id.phone) TextView phone;
  @BindView(R.id.photo) ImageView photo;

  public ContactItemView(Context context) {
    super(context);
  }

  public ContactItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ContactItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override public void onSelected(boolean selected) {

  }

  @Override public void bindTo(Contact item) {
    Picasso.with(getContext()).cancelRequest(photo);
    name.setText(item.name());
    phone.setText(item.phone());
    if (item.pictureUrl() != null) {
      Picasso.with(getContext())
          .load(ApiModule.HOST + item.pictureUrl())
          .into(photo);
    }
  }

  @Override public View getView() {
    return this;
  }

  @Override public Observable<Object> getObjectObservable() {
    return null;
  }
}
