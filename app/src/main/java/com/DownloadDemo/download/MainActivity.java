package com.DownloadDemo.download;

import android.app.Activity;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.DownloadDemo.download.manager.DownloadInfo;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public android.os.Handler mHandler;
    private Activity mActivty=this;
    private ListView mListView;
    public AdapterContent mAdapter;
    private ArrayList<DownloadInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView();
        initData();
    }

    private void initData() {

        list=new ArrayList<>();
        DownloadInfo info1=new DownloadInfo(0,this,"百度云音乐","http://music.baidu.com/cms/BaiduMusic-pcwebdownpagetest.apk");
        list.add(info1);
        DownloadInfo info2=new DownloadInfo(1,this,"apple","http://xz.cr173.com/soft2/FileZilla-apple.zip");
        list.add(info2);
        DownloadInfo info3=new DownloadInfo(2,this,"qq","http://gdown.baidu.com/data/wisegame/4091da454312f1c1/QQ_288.apk" );
        list.add(info3);
        DownloadInfo info4=new DownloadInfo(3,this,"微信","http://gdown.baidu.com/data/wisegame/fee16fe11db48ff5/weixin_660.apk");
        list.add(info4);
        DownloadInfo info5=new DownloadInfo(4,this,"应用3","http://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=http%3A%2F%2Fp1.gexing.com%2Fshaitu%2F20130221%2F0636%2F51254ff02dbd6.jpg&thumburl=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D1304576972%2C1268846219%26fm%3D21%26gp%3D0.jpg");
        list.add(info5);
        DownloadInfo info6=new DownloadInfo(5,this,"应用4","http://imgs.cc6uu.com/meitu/201203241043086736.jpg");
        list.add(info6);
        mAdapter=new AdapterContent(mActivty,list);
        mListView.setAdapter(mAdapter);

        mHandler=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        DownloadInfo info=list.get(msg.getData().getInt("id"));
                        if(info!=null) {
                            info.totalSize = msg.getData().getInt("size");
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 2:
                        DownloadInfo info2=list.get(msg.getData().getInt("id"));
                        if(info2!=null) {
                            info2.downloadSize = msg.getData().getInt("downLoadsize");
                            info2.speed = msg.getData().getLong("speed");
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "服务器被火烧了。。。", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        mAdapter.notifyDataSetChanged();
                }
            }
        };
      mAdapter.setHandler(mHandler);
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
