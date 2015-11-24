package com.example.david.downloaddem;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.download.manager.DownloadInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Activity mActivty=this;
    private ListView mListView;
    public AdapterContent mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView();
        initData();
    }

    private void initData() {
        ArrayList<DownloadInfo> list=new ArrayList<>();
        DownloadInfo info1=new DownloadInfo(this,"百度云音乐","http://music.baidu.com/cms/BaiduMusic-pcwebdownpagetest.apk");
        list.add(info1);
        DownloadInfo info2=new DownloadInfo(this,"apple","http://xz.cr173.com/soft2/FileZilla-apple.zip");
        list.add(info2);
        DownloadInfo info3=new DownloadInfo(this,"qq","http://gdown.baidu.com/data/wisegame/4091da454312f1c1/QQ_288.apk" );
        list.add(info3);
        DownloadInfo info4=new DownloadInfo(this,"微信","http://gdown.baidu.com/data/wisegame/fee16fe11db48ff5/weixin_660.apk");
        list.add(info4);
        DownloadInfo info5=new DownloadInfo(this,"应用3","http://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=http%3A%2F%2Fp1.gexing.com%2Fshaitu%2F20130221%2F0636%2F51254ff02dbd6.jpg&thumburl=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D1304576972%2C1268846219%26fm%3D21%26gp%3D0.jpg");
        list.add(info5);
        DownloadInfo info6=new DownloadInfo(this,"应用4","http://imgs.cc6uu.com/meitu/201203241043086736.jpg");
        list.add(info6);
        mAdapter=new AdapterContent(mActivty,list);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.listview_content);
    }


    //    private EditText downloadpathText;
//    private TextView resultView,totalSize,downloadSize,speedView;
//    private ProgressBar progressBar;
//    private DownLoadThread thread;
//    private android.os.Handler mHandler=new android.os.Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    progressBar.setMax(msg.getData().getInt("size"));
//                    totalSize.setText("/"+Utils.revertBtoMB(msg.getData().getInt("size"))+"M");
//                    break;
//                case 2:
//                    progressBar.setProgress(msg.getData().getInt("size"));
//                    downloadSize.setText(Utils.revertBtoMB(msg.getData().getInt("size"))+"M");
//                    speedView.setText(""+msg.getData().getString("speed"));
//                    break;
//                case 3:
//                    Toast.makeText(mActivty,"服务器被火烧了。。。",Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.listview_item);
//        initView();
//    }
//
//    private void initView() {
//        downloadpathText = (EditText) this.findViewById(R.id.path);
//        progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
//        resultView = (TextView) this.findViewById(R.id.resultView);
//        totalSize= (TextView) this.findViewById(R.id.total_size);
//        downloadSize= (TextView) findViewById(R.id.finfish_size);
//        speedView= (TextView) findViewById(R.id.speed);
//        Button button = (Button) this.findViewById(R.id.button);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String path = downloadpathText.getText().toString();
//                thread = new DownLoadThread(path,mHandler,MainActivity.this);
//                thread.start();
//            }
//        });
//        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                thread.setStopFlag(true);
//            }
//        });
//
//    }
}
