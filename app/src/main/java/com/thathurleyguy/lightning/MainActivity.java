package com.thathurleyguy.lightning;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
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
import com.thathurleyguy.lightning.views.WemoButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.client.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;


public class MainActivity extends Activity {
    private static final String FIREBASE_URL = "https://glaring-inferno-5664.firebaseio.com";
    @InjectView(R.id.wemo_dock) LinearLayout wemoDock;

    private Map<UUID, WemoButton> switches = new HashMap<UUID, WemoButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase(FIREBASE_URL);
        myFirebaseRef.child("wemo_devices").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, HashMap> data = (HashMap) snapshot.getValue();
                for (String idString : data.keySet()) {
                    HashMap stuff = data.get(idString);
                    UUID id = UUID.fromString(idString);

                    WemoButton button = switches.get(id);
                    if (button == null)
                        continue;
                    button.setChecked((Boolean) stuff.get("powered_on"));
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        LightningService.getService().listWemoDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WemoDevice>>() {
                    @Override
                    public void onNext(List<WemoDevice> wemoDevices) {
                        for (WemoDevice device : wemoDevices) {
                            System.out.println(device.getName() + " - " + device.getId() + " = " + device.isPoweredOn());
                            WemoButton button = addSwitch(device);
                            switches.put(device.getId(), button);
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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

    private WemoButton addSwitch(final WemoDevice device) {
        WemoButton button = new WemoButton(MainActivity.this, device);
        wemoDock.addView(button);
        return button;
    }
}
