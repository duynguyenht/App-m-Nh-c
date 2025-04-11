package com.example.music.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.music.adapter.SongPlayingAdapter;
import com.example.music.constant.Constant;
import com.example.music.constant.GlobalFuntion;
import com.example.music.databinding.FragmentListSongPlayingBinding;
import com.example.music.service.MusicService;

public class ListSongPlayingFragment extends Fragment {

    private FragmentListSongPlayingBinding mFragmentListSongPlayingBinding;
    private SongPlayingAdapter mSongPlayingAdapter;

    // BroadcastReceiver để nhận thông báo khi danh sách bài hát đang phát thay đổi
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStatusListSongPlaying(); // Cập nhật trạng thái danh sách bài hát
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentListSongPlayingBinding = FragmentListSongPlayingBinding.inflate(inflater, container, false);

        // Đăng ký BroadcastReceiver để lắng nghe thay đổi danh sách bài hát
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                    new IntentFilter(Constant.CHANGE_LISTENER));
        }

        displayListSongPlaying(); // Hiển thị danh sách bài hát đang phát

        return mFragmentListSongPlayingBinding.getRoot();
    }

    private void displayListSongPlaying() {
        if (getActivity() == null || MusicService.mListSongPlaying == null) {
            return;
        }
        // Thiết lập LayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentListSongPlayingBinding.rcvData.setLayoutManager(linearLayoutManager);

        // Gán adapter cho RecyclerView
        mSongPlayingAdapter = new SongPlayingAdapter(MusicService.mListSongPlaying, this::clickItemSongPlaying);
        mFragmentListSongPlayingBinding.rcvData.setAdapter(mSongPlayingAdapter);

        updateStatusListSongPlaying(); // Cập nhật trạng thái danh sách bài hát
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateStatusListSongPlaying() {
        if (getActivity() == null || MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
            return;
        }
        // Cập nhật trạng thái bài hát đang phát
        for (int i = 0; i < MusicService.mListSongPlaying.size(); i++) {
            MusicService.mListSongPlaying.get(i).setPlaying(i == MusicService.mSongPosition);
        }
        mSongPlayingAdapter.notifyDataSetChanged(); // Thông báo adapter cập nhật dữ liệu
    }

    private void clickItemSongPlaying(int position) {
        MusicService.isPlaying = false;
        GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver khi Fragment bị hủy
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }
}