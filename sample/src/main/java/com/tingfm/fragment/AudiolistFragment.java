package com.tingfm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tingfm.Asset;
import com.tingfm.tingfm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.tingfm.adapter.AudiolistAdapter;

/**
 * Created by david on 15/11/25.
 */
public class AudiolistFragment extends Fragment implements  AbsListView.OnScrollListener {
    private ListView listView;
    private List<Asset> audioList;
    private AudiolistAdapter mAdapter;
    private View footerView;
    private boolean loadMore=false;
    private boolean lastfinish=true;
    private int currentPageNum=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_audiolist,null);
        listView= (ListView) rootView.findViewById(R.id.listview_audiofragment_audiolist);
        listView.setOnScrollListener(this);
        footerView= inflater.inflate(R.layout.listitem_footview,null);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioList=new ArrayList<>();
        initData();
    }

    private void initData() {
        /**获取专辑音频列表
         * url:http://mobile.ximalaya.com/mobile/others/ca/album/track/321705/true/1/20?albumId=321705&pageSize=20&isAsc=true&position=3&device=android
         * **/
        String url="http://mobile.ximalaya.com/mobile/others/ca/album/track/321705/true/"+currentPageNum+"/20?albumId=321705&pageSize=20&isAsc=true&position=3&device=android";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("JSONResult", jsonObject.toString());
                JSONObject tracksObject=jsonObject.optJSONObject("tracks");
                JSONArray array=tracksObject.optJSONArray("list");
                for(int i=0;i<array.length();i++){
                    JSONObject audioObject=array.optJSONObject(i);
                    Asset mAsset=new Asset();
                    mAsset.trackId=audioObject.optInt("trackId");
                    mAsset.downloadUrl=audioObject.optString("downloadUrl");
                    mAsset.title=audioObject.optString("title");
                    mAsset.createdAt=audioObject.optLong("createdAt");
                    mAsset.playtimes=audioObject.optInt("playtimes");
                    mAsset.duration=audioObject.optInt("duration");
                    mAsset.comments=audioObject.optInt("comments");
                    mAsset.playUrl64=audioObject.optString("playUrl64");
                    audioList.add(mAsset);
                }
                int currentPage= tracksObject.optInt("pageId");
                int maxPageId=tracksObject.optInt("maxPageId");
                if(currentPage<maxPageId){
                    loadMore=true;
                    currentPageNum++;
                    listView.addFooterView(footerView);
                }else{
                    listView.removeFooterView(footerView);
                    loadMore=false;
                }
                if(mAdapter==null) {
                    mAdapter = new AudiolistAdapter(audioList, getActivity());
                    listView.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
                lastfinish=true;
            }
        },null);
        queue.add(request);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (loadMore == true&&lastfinish) {
                initData();
                lastfinish=false;
            }
        }
    }
}
