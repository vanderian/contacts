package sk.vander.contacts.base.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vander on 5/15/15.
 */
public abstract class ListSource<T> implements Source<T> {
//    protected final PublishSubject<ChangeEvent> change = PublishSubject.create();
    protected List<T> list;

    public ListSource() {
        this.list = Collections.emptyList();
    }

    public ListSource(List<T> list) {
        this.list = list;
    }

    @Override public T get(int position) {
        return list.get(position);
    }

    @Override public int getCount() {
        return list.size();
    }

    public List<T> getList() {
        return list;
    }

    public void addItems(List<T> aList) {
        if (aList == null || aList.isEmpty()) return;

        final List<T> old = list;
        if (list.isEmpty()) {
            setList(aList);
            return;
        }

        list.addAll(aList);
    }

    /**
     * Similar to {@link #addItems(List)} but filters item duplicates before adding
     *
     * @param aList items to add
     */
    public void addItemsUnique(List<T> aList) {
        // protection against duplicates
        for (int i = aList.size() - 1; i >= 0; i--) {
            if (list.contains(aList.get(i))) {
                aList.remove(i);
            }
        }
        addItems(aList);
    }

    public void setList(List<T> aList) {
        final List<T> old = list;
        list = new ArrayList<>(aList);
    }
}
