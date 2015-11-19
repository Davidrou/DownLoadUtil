package com.example.david.downloaddem;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar   ;
import android.widget.TextView;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private EditText downloadpathText;
    private TextView resultView,totalSize,downloadSize;
    private ProgressBar progressBar;
    private android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    progressBar.setMax(msg.getData().getInt("size"));
                    totalSize.setText("/"+revertBtoMB(msg.getData().getInt("size"))+"M");
                    break;
                case 2:
                    progressBar.setProgress(msg.getData().getInt("size"));
                    downloadSize.setText(revertBtoMB(msg.getData().getInt("size"))+"M");

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        downloadpathText = (EditText) this.findViewById(R.id.path);
        progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
        resultView = (TextView) this.findViewById(R.id.resultView);
        totalSize= (TextView) this.findViewById(R.id.total_size);
        downloadSize= (TextView) findViewById(R.id.finfish_size);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = downloadpathText.getText().toString();
                new DownLoadThread(path,mHandler).start();
            }
        });
    }

    private String revertBtoMB(int B){
        return String.format("%.2f",B/1024.0/1024.0);
    }

}
