package com.example.music.activity;

import android.os.Bundle;
import android.view.View;

import com.example.music.R;
import com.example.music.adapter.MusicViewPagerAdapter;
import com.example.music.databinding.ActivityPlayMusicBinding;

public class PlayMusicActivity extends BaseActivity {

    private ActivityPlayMusicBinding mActivityPlayMusicBinding; // Binding để truy cập giao diện UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPlayMusicBinding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(mActivityPlayMusicBinding.getRoot());

        initToolbar(); // Khởi tạo thanh công cụ (toolbar)
        initUI(); // Khởi tạo giao diện người dùng
    }

    // Thiết lập thanh công cụ với các nút điều khiển
    private void initToolbar() {
        mActivityPlayMusicBinding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white); // Đặt icon nút quay lại
        mActivityPlayMusicBinding.toolbar.tvTitle.setText(R.string.music_player); // Đặt tiêu đề thanh công cụ
        mActivityPlayMusicBinding.toolbar.layoutPlayAll.setVisibility(View.GONE); // Ẩn nút "Play All" nếu có

        // Gán sự kiện click cho nút quay lại
        mActivityPlayMusicBinding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed());
    }

    // Khởi tạo giao diện trình phát nhạc
    private void initUI() {
        MusicViewPagerAdapter musicViewPagerAdapter = new MusicViewPagerAdapter(this); // Tạo adapter cho ViewPager
        mActivityPlayMusicBinding.viewpager2.setAdapter(musicViewPagerAdapter); // Gán adapter cho ViewPager
        mActivityPlayMusicBinding.indicator3.setViewPager(mActivityPlayMusicBinding.viewpager2); // Thiết lập indicator
        mActivityPlayMusicBinding.viewpager2.setCurrentItem(1); // Đặt vị trí mặc định của ViewPager là trang thứ hai
    }
}
