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
import com.example.music.databinding.FragmentFeaturedSongsBinding;
import com.example.music.model.Song;
import com.example.music.service.MusicService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeaturedSongsFragment extends Fragment {

    private FragmentFeaturedSongsBinding mFragmentFeaturedSongsBinding;
    private List<Song> mListSong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFeaturedSongsBinding = FragmentFeaturedSongsBinding.inflate(inflater, container, false);

        getListFeaturedSongs(); // Lấy danh sách bài hát nổi bật từ Firebase
        initListener(); // Khởi tạo listener cho sự kiện UI

        return mFragmentFeaturedSongsBinding.getRoot(); // Trả về View của Fragment
    }

    private void getListFeaturedSongs() {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getSongsDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListSong = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = dataSnapshot.getValue(Song.class);
                    if (song == null) {
                        return;
                    }
                    if (song.isFeatured()) { // Kiểm tra bài hát có phải là nổi bật không
                        mListSong.add(0, song); // Thêm bài hát vào danh sách
                    }
                }
                displayListFeaturedSongs(); // Hiển thị danh sách bài hát nổi bật
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error)); // Hiển thị lỗi nếu có
            }
        });
    }

    private void displayListFeaturedSongs() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentFeaturedSongsBinding.rcvData.setLayoutManager(linearLayoutManager);

        SongAdapter songAdapter = new SongAdapter(mListSong, this::goToSongDetail);
        mFragmentFeaturedSongsBinding.rcvData.setAdapter(songAdapter); // Gán adapter cho RecyclerView
    }

    private void goToSongDetail(@NonNull Song song) {
        MusicService.clearListSongPlaying(); // Xóa danh sách phát hiện tại
        MusicService.mListSongPlaying.add(song); // Thêm bài hát vào danh sách phát
        MusicService.isPlaying = false; // Đặt trạng thái không phát nhạc
        GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0); // Bắt đầu dịch vụ phát nhạc
        GlobalFuntion.startActivity(getActivity(), PlayMusicActivity.class); // Mở màn hình phát nhạc
    }

    private void initListener() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            MusicService.clearListSongPlaying(); // Xóa danh sách phát hiện tại
            MusicService.mListSongPlaying.addAll(mListSong); // Thêm tất cả bài hát nổi bật vào danh sách phát
            MusicService.isPlaying = false; // Đặt trạng thái không phát nhạc
            GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0); // Bắt đầu dịch vụ phát nhạc
            GlobalFuntion.startActivity(getActivity(), PlayMusicActivity.class); // Mở màn hình phát nhạc
        });
    }
}