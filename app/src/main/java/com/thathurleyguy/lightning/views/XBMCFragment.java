package com.thathurleyguy.lightning.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.thathurleyguy.lightning.LightningService;
import com.thathurleyguy.lightning.R;
import com.thathurleyguy.lightning.models.Command;
import com.thathurleyguy.lightning.models.XbmcDevice;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class XBMCFragment extends Fragment {
    private Vibrator vibrator;
    private XbmcDevice xbmcDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xbmc, container, false);
        ButterKnife.inject(this, view);
        getXbmcDevice();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getXbmcDevice() {
        LightningService.getService()
                .listXbmcDevices()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<XbmcDevice>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<XbmcDevice> xbmcDevices) {
                        xbmcDevice = xbmcDevices.get(0);
                    }
                });
    }

    @OnClick({R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right, R.id.btn_select})
    public void sendXbmcCommand(final ImageView button) {
        String command = null;
        switch (button.getId()) {
            case R.id.btn_down:
                command = Command.XBMC_DOWN;
                break;
            case R.id.btn_up:
                command = Command.XBMC_UP;
                break;
            case R.id.btn_left:
                command = Command.XBMC_LEFT;
                break;
            case R.id.btn_right:
                command = Command.XBMC_RIGHT;
                break;
            case R.id.btn_select:
                command = Command.XBMC_SELECT;
                break;
            default:
                command = "UNKOWN";
        }
        this.vibrator.vibrate(50);
        LightningService.getService()
                .sendXbmcCommand(xbmcDevice.getId(), new Command(command))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
