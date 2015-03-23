package com.thathurleyguy.lightning;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
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
import butterknife.OnTouch;
import retrofit.client.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends Activity {
    private static final String FIREBASE_URL = "https://glaring-inferno-5664.firebaseio.com";
    private Vibrator vibrator;
    private Map<UUID, WemoButton> switches = new HashMap<UUID, WemoButton>();
    private InfraredDevice infraredDevice;

    @InjectView(R.id.wemo_dock) LinearLayout wemoDock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        this.vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

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

    @OnClick({R.id.btn_mute, R.id.btn_volume_down, R.id.btn_volume_up, R.id.btn_power, R.id.btn_game, R.id.btn_tv, R.id.btn_something, R.id.btn_movie})
    public void sendInfraredCommand(final ImageView button) {
        String command = null;
        switch (button.getId()) {
            case R.id.btn_power:
                command = Command.POWER;
                break;
            case R.id.btn_volume_up:
                command = Command.VOLUME_UP;
                break;
            case R.id.btn_volume_down:
                command = Command.VOLUME_DOWN;
                break;
            case R.id.btn_mute:
                // TODO
                command = Command.VOLUME_DOWN;
                break;
            case R.id.btn_game:
                command = Command.INPUT_GAME;
                break;
            case R.id.btn_movie:
                command = Command.INPUT_XBMC;
                break;
            default:
                command = "UNKOWN";
        }
        this.vibrator.vibrate(50);
        LightningService.getService()
                .sendIRCommand(infraredDevice.getId(), new Command(command))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
