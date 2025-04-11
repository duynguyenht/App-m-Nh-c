package com.example.music.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music.fragment.ListSongPlayingFragment;
import com.example.music.fragment.PlaySongFragment;

// Adapter cho ViewPager2, quản lý hai fragment: danh sách bài hát đang phát và trình phát nhạc
public class MusicViewPagerAdapter extends FragmentStateAdapter {

    // Constructor nhận vào FragmentActivity để quản lý vòng đời của các fragment
    public MusicViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Trả về fragment tương ứng với vị trí trong ViewPager
        if (position == 0) {
            return new ListSongPlayingFragment(); // Fragment hiển thị danh sách bài hát đang phát
        }
        return new PlaySongFragment(); // Fragment hiển thị trình phát nhạc
    }

    @Override
    public int getItemCount() {
        return 2; // ViewPager có hai trang
    }
}
