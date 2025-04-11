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
import com.example.music.databinding.FragmentPopularSongsBinding;
import com.example.music.model.Song;
import com.example.music.service.MusicService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PopularSongsFragment extends Fragment {

    private FragmentPopularSongsBinding mFragmentPopularSongsBinding; // View binding cho Fragment
    private List<Song> mListSong; // Danh sách bài hát phổ biến

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ánh xạ layout thông qua View Binding
        mFragmentPopularSongsBinding = FragmentPopularSongsBinding.inflate(inflater, container, false);

        getListPopularSongs(); // Lấy danh sách bài hát phổ biến từ Firebase
        initListener(); // Thiết lập sự kiện click

        return mFragmentPopularSongsBinding.getRoot(); // Trả về giao diện của Fragment
    }

    // Lấy danh sách bài hát phổ biến từ Firebase
    private void getListPopularSongs() {
        if (getActivity() == null) {
            return;
        }

        // Lắng nghe sự thay đổi dữ liệu trên Firebase
        MyApplication.get(getActivity()).getSongsDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListSong = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Song song = dataSnapshot.getValue(Song.class);
                    if (song == null) {
                        return;
                    }
                    // Chỉ lấy những bài hát có lượt nghe lớn hơn 0
                    if (song.getCount() > 0) {
                        mListSong.add(song);
                    }
                }
                // Sắp xếp danh sách bài hát theo số lượt nghe giảm dần
                Collections.sort(mListSong, (song1, song2) -> song2.getCount() - song1.getCount());
                displayListPopularSongs(); // Hiển thị danh sách lên RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hiển thị thông báo lỗi khi không lấy được dữ liệu từ Firebase
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    // Hiển thị danh sách bài hát phổ biến trong RecyclerView
    private void displayListPopularSongs() {
        if (getActivity() == null) {
            return;
        }
        // Cài đặt LayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentPopularSongsBinding.rcvData.setLayoutManager(linearLayoutManager);

        // Khởi tạo Adapter và gán vào RecyclerView
        SongAdapter songAdapter = new SongAdapter(mListSong, this::goToSongDetail);
        mFragmentPopularSongsBinding.rcvData.setAdapter(songAdapter);
    }

    // Chuyển đến màn hình phát nhạc khi chọn một bài hát
    private void goToSongDetail(@NonNull Song song) {
        MusicService.clearListSongPlaying(); // Xóa danh sách bài hát đang phát
        MusicService.mListSongPlaying.add(song); // Thêm bài hát mới vào danh sách phát
        MusicService.isPlaying = false; // Đặt trạng thái bài hát chưa phát

        // Bắt đầu phát nhạc và mở màn hình PlayMusicActivity
        GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0);
        GlobalFuntion.startActivity(getActivity(), PlayMusicActivity.class);
    }

    // Thiết lập sự kiện click cho nút "Phát tất cả" trong header
    private void initListener() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }

        // Khi bấm vào "Phát tất cả", danh sách bài hát sẽ được phát từ đầu
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            MusicService.clearListSongPlaying(); // Xóa danh sách bài hát đang phát
            MusicService.mListSongPlaying.addAll(mListSong); // Thêm tất cả bài hát vào danh sách phát
            MusicService.isPlaying = false; // Đặt trạng thái bài hát chưa phát

            // Bắt đầu phát nhạc và mở màn hình PlayMusicActivity
            GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0);
            GlobalFuntion.startActivity(getActivity(), PlayMusicActivity.class);
        });
    }
}
