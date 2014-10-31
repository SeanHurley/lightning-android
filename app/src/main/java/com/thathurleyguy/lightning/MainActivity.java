package com.thathurleyguy.lightning;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;


public class MainActivity extends Activity {
    private static final String FIREBASE_URL = "https://glaring-inferno-5664.firebaseio.com";
    @InjectView(R.id.layout_buttons)
    LinearLayout buttonLayout;
    private Map<Long, Switch> switches = new HashMap<Long, Switch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase(FIREBASE_URL);
        myFirebaseRef.child("lights").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<HashMap> list = (List) snapshot.getValue();
                for (HashMap obj : list) {
                    if (obj == null)
                        continue;
                    Switch toggleSwitch = switches.get(obj.get("id"));
                    if (toggleSwitch == null)
                        continue;
                    toggleSwitch.setChecked((Boolean) obj.get("powered_on"));
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.99.249:3000")
                .setConverter(new GsonConverter(gson))
                .build();

        final LightningService service = restAdapter.create(LightningService.class);
        service.listWemoDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WemoDevice>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<WemoDevice> wemoDevices) {
                        for (WemoDevice device : wemoDevices) {
                            System.out.println(device.getName() + " - " + device.getId() + " = " + device.isPoweredOn());
                            Switch toggleSwitch = addSwitch(device);
                            switches.put(device.getId(), toggleSwitch);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Switch addSwitch(final WemoDevice device) {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView label = new TextView(MainActivity.this);
        label.setText(device.getName());
        Switch toggleSwitch = new Switch(MainActivity.this);
        toggleSwitch.setChecked(device.isPoweredOn());

        layout.addView(label);
        layout.addView(toggleSwitch);
        buttonLayout.addView(layout);

        toggleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .registerTypeAdapter(Date.class, new DateTypeAdapter())
                        .create();

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("http://192.168.99.249:3000")
                        .setConverter(new GsonConverter(gson))
                        .build();

                final LightningService service = restAdapter.create(LightningService.class);
                service.toggleDevice(device.getId())
                        .subscribe();
            }
        });
        return toggleSwitch;
    }
}
