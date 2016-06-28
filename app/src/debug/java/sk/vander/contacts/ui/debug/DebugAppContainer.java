package sk.vander.contacts.ui.debug;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.actions.Action;
import io.palaima.debugdrawer.actions.ActionsModule;
import io.palaima.debugdrawer.actions.SwitchAction;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.okhttp3.OkHttp3Module;
import io.palaima.debugdrawer.picasso.PicassoModule;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import io.palaima.debugdrawer.timber.TimberModule;
import okhttp3.OkHttpClient;
import sk.vander.contacts.R;
import sk.vander.contacts.base.AppContainer;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.base.prefs.BooleanPreference;
import sk.vander.contacts.data.api.MockMode;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

@ApplicationScope
public final class DebugAppContainer implements AppContainer {

  private final BooleanPreference mockPref;
  private final OkHttpClient okHttpClient;
  private final Picasso picasso;

  @Inject DebugAppContainer(@MockMode BooleanPreference mockPref,
                            OkHttpClient okHttpClient,
                            Picasso picasso) {
    this.mockPref = mockPref;
    this.okHttpClient = okHttpClient;
    this.picasso = picasso;
  }

  @Override
  public ViewGroup get(final Activity activity) {
    activity.setContentView(R.layout.container_debug);

    final SwitchPref mock = new SwitchPref("Mock mode", mockPref, b -> ProcessPhoenix.triggerRebirth(activity));

    new DebugDrawer.Builder(activity)
        .modules(new ActionsModule(mock),
            new ScalpelModule(activity),
            new TimberModule(),
            new OkHttp3Module(okHttpClient),
            new PicassoModule(picasso),
            new DeviceModule(activity),
            new BuildModule(activity))
        .build();

    riseAndShine(activity);
    return ButterKnife.findById(activity, R.id.container_debug);
  }

  /**
   * Show the activity over the lock-screen and wake up the device. If you launched the app manually
   * both of these conditions are already true. If you deployed from the IDE, however, this will
   * save you from hundreds of power button presses and pattern swiping per day!
   */
  public static void riseAndShine(Activity activity) {
    activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

    PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
    PowerManager.WakeLock lock =
        power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
    lock.acquire();
    lock.release();
  }

  private static class SwitchPref implements Action {
    private final String name;
    private final BooleanPreference pref;
    private final SwitchAction.Listener listener;
    private Switch switchButton;

    public SwitchPref(String name, BooleanPreference pref, @Nullable SwitchAction.Listener listener) {
      this.name = name;
      this.listener = listener;
      this.pref = pref;
    }

    @Override public View getView(LinearLayout linearLayout) {
      final Context context = linearLayout.getContext();
      Resources resources = context.getResources();
      LinearLayout.LayoutParams viewGroupLayoutParams = new LinearLayout.LayoutParams(-1, -1);
      viewGroupLayoutParams.topMargin = resources.getDimensionPixelOffset(io.palaima.debugdrawer.actions.R.dimen.dd_padding_small);
      LinearLayout viewGroup = new LinearLayout(context);
      viewGroup.setLayoutParams(viewGroupLayoutParams);
      viewGroup.setOrientation(LinearLayout.HORIZONTAL);
      LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      textViewLayoutParams.rightMargin = resources.getDimensionPixelSize(io.palaima.debugdrawer.actions.R.dimen.dd_spacing_big);
      TextView textView = new TextView(context);
      textView.setLayoutParams(textViewLayoutParams);
      textView.setText(this.name);
      textView.setTextSize(0, resources.getDimension(io.palaima.debugdrawer.actions.R.dimen.dd_font_normal));
      textView.setGravity(16);
      this.switchButton = new Switch(context);
      onStart();
      viewGroup.addView(textView);
      viewGroup.addView(this.switchButton);
      return viewGroup;
    }

    @Override public void onOpened() {
    }

    @Override public void onClosed() {
    }

    @Override public void onResume() {

    }

    @Override public void onPause() {

    }

    @Override public void onStart() {
      this.switchButton.setOnCheckedChangeListener(null);
      this.switchButton.setChecked(pref.get());
      this.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
        pref.set(isChecked);
        if (listener != null) {
          listener.onCheckedChanged(isChecked);
        }
      });
    }

    @Override public void onStop() {
    }
  }
}
