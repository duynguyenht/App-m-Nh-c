package com.example.music.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.R;
import com.example.music.databinding.ItemSongPlayingBinding;
import com.example.music.listener.IOnClickSongPlayingItemListener;
import com.example.music.model.Song;
import com.example.music.utils.GlideUtils;

import java.util.List;

public class SongPlayingAdapter extends RecyclerView.Adapter<SongPlayingAdapter.SongPlayingViewHolder> {

    private final List<Song> mListSongs;
    public final IOnClickSongPlayingItemListener iOnClickSongPlayingItemListener;

    // Constructor nhận danh sách bài hát và listener để xử lý sự kiện click
    public SongPlayingAdapter(List<Song> mListSongs, IOnClickSongPlayingItemListener iOnClickSongPlayingItemListener) {
        this.mListSongs = mListSongs;
        this.iOnClickSongPlayingItemListener = iOnClickSongPlayingItemListener;
    }

    @NonNull
    @Override
    public SongPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongPlayingBinding itemSongPlayingBinding = ItemSongPlayingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongPlayingViewHolder(itemSongPlayingBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongPlayingViewHolder holder, int position) {
        Song song = mListSongs.get(position);
        if (song == null) {
            return;
        }

        // Kiểm tra trạng thái đang phát nhạc để thay đổi giao diện
        if (song.isPlaying()) {
            holder.mItemSongPlayingBinding.layoutItem.setBackgroundResource(R.color.background_bottom);
            holder.mItemSongPlayingBinding.imgPlaying.setVisibility(View.VISIBLE);
        } else {
            holder.mItemSongPlayingBinding.layoutItem.setBackgroundResource(R.color.white);
            holder.mItemSongPlayingBinding.imgPlaying.setVisibility(View.GONE);
        }

        // Load ảnh bài hát bằng GlideUtils
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongPlayingBinding.imgSong);

        // Hiển thị tên bài hát và nghệ sĩ
        holder.mItemSongPlayingBinding.tvSongName.setText(song.getTitle());
        holder.mItemSongPlayingBinding.tvArtist.setText(song.getArtist());

        // Xử lý sự kiện click vào item bài hát
        holder.mItemSongPlayingBinding.layoutItem.setOnClickListener(v
                -> iOnClickSongPlayingItemListener.onClickItemSongPlaying(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return null == mListSongs ? 0 : mListSongs.size();
    }

    // ViewHolder lưu trữ view cho mỗi item trong danh sách
    public static class SongPlayingViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongPlayingBinding mItemSongPlayingBinding;

        public SongPlayingViewHolder(ItemSongPlayingBinding itemSongPlayingBinding) {
            super(itemSongPlayingBinding.getRoot());
            this.mItemSongPlayingBinding = itemSongPlayingBinding;
        }
    }
}