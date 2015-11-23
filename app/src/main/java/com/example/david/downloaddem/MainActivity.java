package com.example.david.downloaddem;

import android.app.Activity;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar   ;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Activity mActivty=this;
    private EditText downloadpathText;
    private TextView resultView,totalSize,downloadSize,speedView;
    private ProgressBar progressBar;
    private DownLoadThread thread;
    private android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    progressBar.setMax(msg.getData().getInt("size"));
                    totalSize.setText("/"+Utils.revertBtoMB(msg.getData().getInt("size"))+"M");
                    break;
                case 2:
                    progressBar.setProgress(msg.getData().getInt("size"));
                    downloadSize.setText(Utils.revertBtoMB(msg.getData().getInt("size"))+"M");
                    speedView.setText(""+msg.getData().getString("speed"));
                    break;
                case 3:
                    Toast.makeText(mActivty,"服务器被火烧了。。。",Toast.LENGTH_LONG).show();
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
        speedView= (TextView) findViewById(R.id.speed);
        Button button = (Button) this.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = downloadpathText.getText().toString();
                thread = new DownLoadThread(path,mHandler,MainActivity.this);
                thread.start();
            }
        });
        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setStopFlag(true);
            }
        });

    }
}
