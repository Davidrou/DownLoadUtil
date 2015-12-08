package com.tingfm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.tingfm.download.DownLoadManager;
import com.tingfm.tingfm.R;

import com.tingfm.adapter.DownloadFramentAdapter;

/**
 * Created by david on 15/11/25.
 */
public class DownloadingFragment extends Fragment  implements DownLoadManager.DownloadingChangeListener {
    private ListView listview;
    private DownloadFramentAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DownLoadManager.with().setDownloadingListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_downloading,null);
        listview= (ListView) rootView.findViewById(R.id.listview_downloadingfragment_content);
        mAdapter=new DownloadFramentAdapter(getActivity(),DownLoadManager.with().getDownloadingList());
        listview.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void refreshData() {
        if(getActivity()==null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onHasDownloadingWarn() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),"正在下载哦！",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownLoadManager.with().saveDownloadingList();
        DownLoadManager.with().closeAllTask();
    }
}
