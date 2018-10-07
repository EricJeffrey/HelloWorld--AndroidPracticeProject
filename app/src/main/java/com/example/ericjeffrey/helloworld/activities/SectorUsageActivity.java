package com.example.ericjeffrey.helloworld.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.ericjeffrey.helloworld.R;
import com.example.ericjeffrey.helloworld.views.SectorProcessView;

import static com.example.ericjeffrey.helloworld.views.SectorProcessView.DEFAULT_ANIMATION_DURATION;

public class SectorUsageActivity extends AppCompatActivity {
    private SectorProcessView spvView;
    private SeekBar seekBar;

    private int mTextColor;
    private int mSectorColor;
    private int mBGOvalColor;

    private int mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector_usage);

        mTextColor = Color.BLACK;
        mSectorColor = getResources().getColor(R.color.colorAzureHalfTran);
        mBGOvalColor = getResources().getColor(R.color.colorAzure);

        mDuration = DEFAULT_ANIMATION_DURATION;

        //联动
        seekBar = findViewById(R.id.sk_bar_sector_usage);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    spvView.drawRate((float) (progress / 100.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //联动
        spvView = findViewById(R.id.spv_sector_usage);
        spvView.setOnRateChangeListener(new SectorProcessView.OnRateChangeListener() {
            @Override
            public void onRateChange(View view, float rate) {
                seekBar.setProgress((int) (rate * 100));
            }
        });

        //启动按钮
        findViewById(R.id.btn_sector_usage_to_draw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spvView.startAnimate(0, (float) Math.random(), mDuration);
            }
        });

        //文字颜色
        final View pickTextColor = findViewById(R.id.view_sector_usage_pick_text_color);
        pickTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startColorPicker(pickTextColor, 0);
            }
        });

        //扇形颜色
        final View pickSectorColor = findViewById(R.id.view_sector_usage_pick_sector_color);
        pickSectorColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startColorPicker(pickSectorColor, 1);
            }
        });

        //背景圆颜色
        final View pickOvalColor = findViewById(R.id.view_sector_usage_pick_oval_color);
        pickOvalColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startColorPicker(pickOvalColor, 2);
            }
        });

        //文字大小
        SeekBar seekBarTextSize = findViewById(R.id.sk_bar_sector_text_size);
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    spvView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //设置文字可见性
        CheckBox checkBoxShowText = findViewById(R.id.checkbox_sector_usage_show_text);
        checkBoxShowText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spvView.setShowText(isChecked);
            }
        });

        //设置背景可见性
        CheckBox checkBoxShowOval = findViewById(R.id.checkbox_sector_usage_show_oval);
        checkBoxShowOval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spvView.setShowBGOval(isChecked);
            }
        });

        //动画时长
        SeekBar seekBarAnimateDura = findViewById(R.id.sk_bar_sector_duration);
        seekBarAnimateDura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mDuration = progress * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * 自制颜色选择器
     *
     * @param showColorView 设置颜色的view
     * @param whichToSet         指定设置的是什么颜色
     *                      0 文字
     *                      1 扇形
     *                      2 背景圆
     */
    private void startColorPicker(final View showColorView, final int whichToSet) {
        LinearLayout linearLayout = new LinearLayout(this);
        View view = LayoutInflater.from(this).inflate(R.layout.ll_color_picker_dialog_view, linearLayout);

        final SeekBar red = view.findViewById(R.id.sk_bar_dialog_view_red);
        final SeekBar green = view.findViewById(R.id.sk_bar_dialog_view_green);
        final SeekBar blue = view.findViewById(R.id.sk_bar_dialog_view_blue);
        final SeekBar alpha = view.findViewById(R.id.sk_bar_dialog_view_alpha);

        final View bgView = view.findViewById(R.id.view_dialog_view_bg);


        switch (whichToSet) {
            case 0:
                alpha.setProgress((mTextColor >> 24) & 0xFF);
                red.setProgress((mTextColor >> 16) & 0xFF);
                green.setProgress((mTextColor >> 8) & 0xFF);
                blue.setProgress(mTextColor & 0xFF);
                bgView.setBackgroundColor(mTextColor);
                break;
            case 1:
                alpha.setProgress((mSectorColor >> 24) & 0xFF);
                red.setProgress((mSectorColor >> 16) & 0xFF);
                green.setProgress((mSectorColor >> 8) & 0xFF);
                blue.setProgress(mSectorColor & 0xFF);
                bgView.setBackgroundColor(mSectorColor);
                break;
            case 2:
                alpha.setProgress((mBGOvalColor >> 24) & 0xFF);
                red.setProgress((mBGOvalColor >> 16) & 0xFF);
                green.setProgress((mBGOvalColor >> 8) & 0xFF);
                blue.setProgress(mBGOvalColor & 0xFF);
                bgView.setBackgroundColor(mBGOvalColor);
                break;
        }
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    bgView.setBackgroundColor(Color.argb(alpha.getProgress(), red.getProgress(), green.getProgress(), blue.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        red.setOnSeekBarChangeListener(seekBarChangeListener);
        green.setOnSeekBarChangeListener(seekBarChangeListener);
        blue.setOnSeekBarChangeListener(seekBarChangeListener);
        alpha.setOnSeekBarChangeListener(seekBarChangeListener);

        new AlertDialog.Builder(this)
                .setView(linearLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int c = Color.argb(alpha.getProgress(), red.getProgress(), green.getProgress(), blue.getProgress());
                        showColorView.setBackgroundColor(c);
                        switch (whichToSet) {
                            case 0:
                                mTextColor = c;
                                spvView.setTextColor(c);
                                break;
                            case 1:
                                mSectorColor = c;
                                spvView.setSectorColor(c);
                                break;
                            case 2:
                                mBGOvalColor = c;
                                spvView.setBGOvalColor(c);
                                break;
                        }
                    }
                }).show();
    }
}
