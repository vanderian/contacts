package sk.vander.contacts.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import sk.vander.contacts.base.view.BindableView;

/**
 * Created by vander on 5/15/15.
 */
public class ObservableAdapter<T> extends RecyclerView.Adapter<ObservableAdapter.ViewHolder<T>> {
    public static final int INVALID_POS = -1;
    private final PublishSubject<ViewHolder<T>> itemClicked = PublishSubject.create();
    private final PublishSubject<SelectionEvent> selectionChange = PublishSubject.create();
    private final PublishSubject<ObjectEvent<T>> itemEvent = PublishSubject.create();

    private Source<T> source;
    private SparseBooleanArray selectedItems;
    private SelectionMode selectionMode = SelectionMode.NONE;

    public ObservableAdapter(Source<T> items) {
        selectedItems = new SparseBooleanArray();
        setSource(items);
    }

    public void setSelectionMode(SelectionMode mode) {
        selectionMode = mode;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    @SuppressWarnings("unchecked")
    @Override public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final BindableView<T> view = (BindableView<T>) inflater.inflate(source.getLayoutRes(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder<T> holder, int position) {
        holder.bindTo(source.get(position), position, selectedItems.get(position, false));
    }

    @SuppressWarnings("unchecked")
    @Override public void onViewAttachedToWindow(ViewHolder<T> holder) {
        holder.subscriptions = new CompositeSubscription();
        holder.subscriptions.add(RxView.clicks(holder.itemView.getView())
                        .filter(ev -> selectionMode != SelectionMode.NONE)
                        .filter(ev -> selectionMode != SelectionMode.SINGLE_MANUAL)
                        .map(ev -> holder.pos)
                        .doOnNext(pos -> {
                            if (SelectionMode.SINGLE_TOGGLE.equals(selectionMode)) {
//                        removes previous selection
                                toggleSelection(getSelectedItemIndex());
                            }
                        })
//                        selects the new position, or unselect if exist
                        .subscribe(this::toggleSelection)
        );

        //need to be after selection is marked
        holder.subscriptions.add(RxView.clicks(holder.itemView.getView()).map(ev -> holder).subscribe(itemClicked));

        if (holder.itemView.getObjectObservable() != null) {
            holder.subscriptions.add(holder.itemView.getObjectObservable().map(ev -> new ObjectEvent<T>(holder, ev)).subscribe(itemEvent));
        }
    }

    @Override public void onViewDetachedFromWindow(ViewHolder<T> holder) {
        if (holder.subscriptions != null && holder.subscriptions.hasSubscriptions() && !holder.subscriptions.isUnsubscribed()) {
            holder.subscriptions.unsubscribe();
            holder.subscriptions = null;
        }
    }

    /**
     * toggles the selection at position
     */
    public void toggleSelection(int pos) {
        final boolean selected;
        if (pos != INVALID_POS) {
            if (selectedItems.get(pos, false)) {
                selectedItems.delete(pos);
                selected = false;
            } else {
                selectedItems.put(pos, true);
                selected = true;
            }
            selectionChange.onNext(new SelectionEvent(selected, pos));
            notifyItemChanged(pos);
        }
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItemIndexes() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public List<T> getSelectedItems() {
        List<T> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            int index = selectedItems.keyAt(i);
            items.add(source.get(index));
        }
        return items;
    }

    /**
     * selects/deselects the desired position based on value,
     * if not {@link SelectionMode#MULTI} also removes old selection
     */
    public void setSelectedItemIndex(int pos, boolean value) {
        if (pos == INVALID_POS || getSelectionMode().equals(SelectionMode.NONE)) {
            return;
        }

        if (!selectionMode.equals(SelectionMode.MULTI)) {
//            removes old selection
            final int idx = getSelectedItemIndex();
            toggleSelection(idx);
        }

        selectedItems.put(pos, value);

        if (!value)
            selectedItems.delete(pos);

        selectionChange.onNext(new SelectionEvent(value, pos, getSelectedItemCount()));
        notifyItemChanged(pos);
    }

    public int getSelectedItemIndex() {
        final int idx = selectedItems.indexOfValue(true);
        return idx != INVALID_POS ? selectedItems.keyAt(idx) : INVALID_POS;
    }

    @Override public int getItemCount() {
        return source == null ? 0 : source.getCount();
    }

    @Override public int getItemViewType(int position) {
        return source.getViewType(position);
    }

    @Override public long getItemId(int position) {
        return source.getItemId(position);
    }

    public void setSource(Source<T> items) {
        this.source = items;
        notifyDataSetChanged();
    }

    public Observable<ViewHolder<T>> onItemClicked() {
        return itemClicked.asObservable();
    }

    public Observable<ObjectEvent<T>> onItemEvent() {
        return itemEvent;
    }

    public Observable<SelectionEvent> onSelectionChange() {
        return selectionChange;
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public final BindableView<T> itemView;
        private T item;
        private int pos;
        private CompositeSubscription subscriptions;

        public ViewHolder(BindableView<T> itemView) {
            super(itemView.getView());
            this.itemView = itemView;
        }

        public void bindTo(T item, int pos, boolean selected) {
            this.item = item;
            this.pos = pos;
            itemView.onSelected(selected);
            itemView.bindTo(item);
        }

        public T getItem() {
            return item;
        }

        public int getPos() {
            return pos;
        }

        public CompositeSubscription getSubscriptions() {
            return subscriptions;
        }
    }

    public static class ObjectEvent<T> {
        private final ViewHolder<T> viewHolder;
        private final Object object;

        public ObjectEvent(ViewHolder<T> viewHolder, Object object) {

            this.viewHolder = viewHolder;
            this.object = object;
        }

        public ViewHolder<T> getViewHolder() {
            return viewHolder;
        }

        public Object getObject() {
            return object;
        }

        public boolean typeOf(Class<?> clazz) {
            return clazz.isInstance(object);
        }
    }

    public static class SelectionEvent {
        public int position;
        public int count;
        public boolean selected;

        public SelectionEvent(boolean selected, int position, int count) {
            this.position = position;
            this.count = count;
            this.selected = selected;
        }

        public SelectionEvent(boolean selected, int position) {
            this(selected, position, 1);
        }
    }

    public enum SelectionMode {
        NONE,
        SINGLE_TOGGLE,
        SINGLE_MANUAL,
        MULTI
    }
}
