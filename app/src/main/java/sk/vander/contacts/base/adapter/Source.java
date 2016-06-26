package sk.vander.contacts.base.adapter;

import android.support.annotation.LayoutRes;
import rx.Observable;

/**
 * Created by vander on 5/15/15.
 */
public interface Source<T> {
    T get(int position);

    int getCount();

    int getViewType(int pos);

    @LayoutRes int getLayoutRes(int viewType);

    long getItemId(int position);

    Observable dataChangeObservable();
}
