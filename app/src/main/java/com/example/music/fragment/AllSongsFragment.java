package com.example.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.music.MyApplication;
import com.example.music.R;
import com.example.music.activity.MainActivity;
import com.example.music.activity.PlayMusicActivity;
import com.example.music.adapter.SongAdapter;
import com.example.music.constant.Constant;
import com.example.music.constant.GlobalFuntion;
import com.example.music.databinding.FragmentAllSongsBinding;
import com.example.music.model.Song;
import com.example.music.service.MusicService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllSongsFragment extends Fragment {

    private FragmentAllSongsBinding mFragmentAllSongsBinding;
    private List<Song> mListSong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAllSongsBinding = FragmentAllSongsBinding.inflate(inflater, container, false);

        getListAllSongs(); // Lấy danh sách bài hát từ Firebase
        initListener(); // Khởi tạo sự kiện lắng nghe

        return mFragmentAllSongsBinding.getRoot();
    }

    private void getListAllSongs() {
        if (getActivity() == null) {
            return;
        }
        // Lắng nghe dữ liệu từ Firebase Realtime Database
        MyApplication.get(getActivity()).getSongsDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListSong = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = dataSnapshot.getValue(Song.class);
                    if (song == null) {
                        return;
                    }
                    mListSong.add(0, song); // Thêm bài hát vào danh sách
                }
                displayListAllSongs(); // Hiển thị danh sách bài hát
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void displayListAllSongs() {
        if (getActivity() == null) {
            return;
        }
        // Thiết lập LayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAllSongsBinding.rcvData.setLayoutManager(linearLayoutManager);

        // Gán adapter cho RecyclerView
        SongAdapter songAdapter = new SongAdapter(mListSong, this::goToSongDetail);
        mFragmentAllSongsBinding.rcvData.setAdapter(songAdapter);
    }

    private void goToSongDetail(@NonNull Song song) {
        // Xóa danh sách bài hát hiện tại trong service
        MusicService.clearListSongPlaying();
        // Thêm bài hát được chọn vào danh sách phát
        MusicService.mListSongPlaying.add(song);
        MusicService.isPlaying = false;
        // Bắt đầu service phát nhạc
        GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0);
        // Mở màn hình phát nhạc
        GlobalFuntion.startActivity(getActivity(), PlayMusicActivity.class);
    }

    private void initListener() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }
        // Xử lý sự kiện khi nhấn vào "Play All"
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            MusicService.clearListSongPlaying();
            MusicService.mListSongPlaying.addAll(mListSong); // Thêm tất cả bài hát vào danh sách phát
            MusicService.isPlaying = false;
            GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0);
            GlobalFuntion.startActivity(getActivity(), PlayMusicActivity.class);
        });
    }
}
