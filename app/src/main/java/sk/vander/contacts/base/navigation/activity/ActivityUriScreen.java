package sk.vander.contacts.base.navigation.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ActivityUriScreen extends ActivityScreen {
    private final String action;
    private final List<String> category;
    private final Uri uri;
    private final Bundle extras;
    private final int flags;

    private ActivityUriScreen(Builder builder) {
        action = builder.action;
        uri = builder.uri;
        category = builder.category;
        extras = builder.extras;
        pairs = builder.pairs;
        flags = builder.flags;
    }

    public static ActivityUriScreen withUri(Uri uri) {
        return newBuilder()
                .withAction(Intent.ACTION_VIEW)
                .withUri(uri)
                .build();
    }

    public static ActivityUriScreen withUriExtras(Uri uri, Bundle extras) {
        return newBuilder()
                .withAction(Intent.ACTION_VIEW)
                .withUri(uri)
                .withExtras(extras)
                .build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override protected void configureIntent(@NonNull Intent intent) {
        intent.setAction(action)
                .addFlags(flags)
                .putExtras(extras)
                .setData(uri);
        Observable.from(category)
                .subscribe(intent::addCategory);
    }

    @Override protected Class<? extends Activity> activityClass() {
        return null;
    }

    public static final class Builder {
        private String action;
        private List<String> category = new ArrayList<>();
        private Uri uri;
        private Bundle extras = new Bundle();
        private List<Pair<View, String>> pairs = new ArrayList<>();
        private int flags;

        private Builder() {
        }

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

        public Builder withUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder addCategory(String category) {
            this.category.add(category);
            return this;
        }

        public Builder withExtras(Bundle bundle) {
            this.extras = bundle;
            return this;
        }

        public Builder withFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder withTransitionView(View view, String name) {
            this.pairs.add(Pair.create(view, name));
            return this;
        }

        public ActivityUriScreen build() {
            return new ActivityUriScreen(this);
        }
    }
}
