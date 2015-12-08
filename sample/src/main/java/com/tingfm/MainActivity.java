package com.tingfm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.List;
import com.tingfm.adapter.MainPagerAdapter;
import com.tingfm.fragment.AudiolistFragment;
import com.tingfm.fragment.DownloadingFragment;
import com.tingfm.tingfm.R;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private MainPagerAdapter pagerAdapter;
    private AudiolistFragment mAudioListFrament;
    private DownloadingFragment mDownloadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        viewPager= (ViewPager) findViewById(R.id.viewpager_mainactivity_content);
        List<android.support.v4.app.Fragment> list=new ArrayList<>();
        mAudioListFrament=new AudiolistFragment();
        mDownloadingFragment=new DownloadingFragment();
        list.add(mAudioListFrament);
        list.add(mDownloadingFragment);
        pagerAdapter=new MainPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }
}
