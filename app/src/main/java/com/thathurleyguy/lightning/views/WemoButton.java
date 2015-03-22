package com.thathurleyguy.lightning.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.thathurleyguy.lightning.LightningService;
import com.thathurleyguy.lightning.R;
import com.thathurleyguy.lightning.models.WemoDevice;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WemoButton extends LinearLayout {
    @InjectView(R.id.switch_button) public Switch toggleSwitch;
    @InjectView(R.id.text_label) public TextView label;

    public WemoButton(Context context, final WemoDevice device) {
        super(context);
        inflate(context, R.layout.wemo_button, this);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        ButterKnife.inject(this);

        label.setText(device.getName());
        toggleSwitch.setChecked(device.isPoweredOn());

        toggleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LightningService.getService()
                        .toggleDevice(device.getId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });
    }

    public void setChecked(boolean checked) {
        toggleSwitch.setChecked(checked);
    }
}
