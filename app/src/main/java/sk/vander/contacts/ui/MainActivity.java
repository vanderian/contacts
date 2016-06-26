package sk.vander.contacts.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import sk.vander.contacts.App;
import sk.vander.contacts.R;
import sk.vander.contacts.base.AppContainer;
import sk.vander.contacts.base.DaggerScope;
import sk.vander.contacts.base.DaggerService;

@AutoComponent(
    dependencies = App.class
)
@AutoInjector
@DaggerScope(MainActivity.class)
public class MainActivity extends AppCompatActivity {
  @Inject AppContainer appContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final MainActivityComponent component = DaggerMainActivityComponent.builder()
        .appComponent(DaggerService.getDaggerComponent(getApplicationContext()))
        .build();
    component.inject(this);

    setContentView(R.layout.activity_main);
    appContainer.get(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }

}
