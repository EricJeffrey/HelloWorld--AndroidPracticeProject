package com.example.ericjeffrey.helloworld.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ericjeffrey.helloworld.tools.AudioRecorder;
import com.example.ericjeffrey.helloworld.R;

public class AudioRecordTestActivity extends AppCompatActivity {
    private static final String TAG = "AudioRecordTestActivity";

    private Drawable drawable;

    private boolean recording = false;
    private Button button;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case AudioRecorder.VOICE_VOLUME:
                    drawable.setLevel(msg.arg1);
                    break;
                case AudioRecorder.RECORD_STOP:
                    drawable.setLevel(0);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record_test);

        button = findViewById(R.id.button);
        ImageView imageView = findViewById(R.id.img);
        drawable = imageView.getDrawable();

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AudioRecordTestActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AudioRecordTestActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    return;
                }
                if (!recording)
                    startRecorder();
                else
                    stopRecorder();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
                    Toast.makeText(this, "你就是头猪！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "不能录音玩个鬼？！！！", Toast.LENGTH_SHORT).show();
            }
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startRecorder();
            }
        }
    }

    private void startRecorder(){
        Toast.makeText(AudioRecordTestActivity.this, "正在录音", Toast.LENGTH_SHORT).show();
        button.setText("停止录音");
        AudioRecorder.getInstance().start(handler);
        recording = !recording;
    }
    private void stopRecorder(){
        Toast.makeText(AudioRecordTestActivity.this, "已停止录音", Toast.LENGTH_SHORT).show();
        button.setText("开始录音");
        AudioRecorder.getInstance().stop();
        recording = !recording;
    }
}
