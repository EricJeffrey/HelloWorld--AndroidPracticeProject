package com.example.ericjeffrey.helloworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ericjeffrey.helloworld.R;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity {
    private static final String packageName = "com.example.ericjeffrey.helloworld.activities.";
    private ArrayList<Pair<String, String>> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        activities.add(new Pair<>("AudioRecordTestActivity", "录音机"));
        activities.add(new Pair<>("LocationTestActivity", "定位地图"));
        activities.add(new Pair<>("TodoListActivity", "待办"));
        activities.add(new Pair<>("SectorUsageActivity", "扇形进度条"));

        addButton();
    }

    private void addButton() {
        for (final Pair<String, String> tmp : activities) {
            Button button = new Button(this);
            button.setText(tmp.second);
            button.setAllCaps(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(LauncherActivity.this,
                                Class.forName(packageName + tmp.first)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            ((ViewGroup) findViewById(R.id.holder)).addView(button);
        }
    }
}
