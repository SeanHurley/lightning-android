package com.thathurleyguy.lightning;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.thathurleyguy.lightning.models.Command;
import com.thathurleyguy.lightning.models.InfraredDevice;
import com.thathurleyguy.lightning.models.WemoDevice;
import com.thathurleyguy.lightning.views.WemoButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends Activity {
    private static final String FIREBASE_URL = "https://glaring-inferno-5664.firebaseio.com";
    @InjectView(R.id.wemo_dock) LinearLayout wemoDock;

    private Map<UUID, WemoButton> switches = new HashMap<UUID, WemoButton>();
    private InfraredDevice infraredDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        startFirebaseListener();
        getWemoDevices();
        getInfraredDevice();
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

    private void getWemoDevices() {
        LightningService.getService().listWemoDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
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

    private void startFirebaseListener() {
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
    }

    public void getInfraredDevice() {
        LightningService.getService()
                .listInfraredDevices()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InfraredDevice>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(List<InfraredDevice> infraredDevices) {
                infraredDevice = infraredDevices.get(0);
            }
        });
    }

    @OnClick(R.id.btn_power)
    public void powerClick() {
        sendInfraredCommand(Command.SOUND_POWER);
        sendInfraredCommand(Command.TV_POWER);
    }

    @OnClick(R.id.btn_volume_down)
    public void volumeDownClick() {
        sendInfraredCommand(Command.VOLUME_DOWN);
    }

    @OnClick(R.id.btn_volume_up)
    public void volumeUpClick() {
        sendInfraredCommand(Command.VOLUME_UP);
    }

    private void sendInfraredCommand(String command) {
        LightningService.getService()
                .sendIRCommand(infraredDevice.getId(), new Command(command))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Response response) {

            }
        });
    }
}
