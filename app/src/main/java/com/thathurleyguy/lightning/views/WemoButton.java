package com.thathurleyguy.lightning.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.thathurleyguy.lightning.LightningService;
import com.thathurleyguy.lightning.models.WemoDevice;

import retrofit.client.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WemoButton extends LinearLayout {
    private Switch toggleSwitch;

    public WemoButton(Context context, final WemoDevice device) {
        super(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.gravity = Gravity.CENTER;
        this.setLayoutParams(params);
        this.setLayoutParams(params);
        this.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerParams.gravity = Gravity.CENTER;
        TextView label = new TextView(context);
        label.setText(device.getName());
        label.setLayoutParams(innerParams);
        label.setTextSize(20);
        this.toggleSwitch = new Switch(context);
        toggleSwitch.setChecked(device.isPoweredOn());
        toggleSwitch.setLayoutParams(innerParams);

        this.addView(label);
        this.addView(toggleSwitch);

        System.out.println("STUFF");
        toggleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LightningService.getService()
                        .toggleDevice(device.getId())
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
        });
    }

    public void setChecked(boolean checked) {
        toggleSwitch.setChecked(checked);
    }
}
