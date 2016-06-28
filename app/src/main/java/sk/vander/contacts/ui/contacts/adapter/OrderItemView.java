package sk.vander.contacts.ui.contacts.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import sk.vander.contacts.R;
import sk.vander.contacts.base.view.BindableView;
import sk.vander.contacts.data.api.model.Order;

/**
 * Created by arashid on 27/06/16.
 */
public class OrderItemView extends RelativeLayout implements BindableView<Order> {
  @BindView(R.id.name) TextView name;
  @BindView(R.id.count) TextView count;

  public OrderItemView(Context context) {
    super(context);
  }

  public OrderItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public OrderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override public void onSelected(boolean selected) {

  }

  @Override public void bindTo(Order item) {
    name.setText(item.name());
    count.setText(item.count());
  }

  @Override public View getView() {
    return this;
  }

  @Override public Observable<Object> getObjectObservable() {
    return null;
  }
}
