package com.example.music.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.databinding.ItemSongBinding;
import com.example.music.listener.IOnClickSongItemListener;
import com.example.music.model.Song;
import com.example.music.utils.GlideUtils;

import java.util.List;

// Adapter cho RecyclerView hiển thị danh sách bài hát
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> mListSongs; // Danh sách bài hát
    public final IOnClickSongItemListener iOnClickSongItemListener; // Listener xử lý sự kiện click

    // Constructor khởi tạo adapter với danh sách bài hát và listener
    public SongAdapter(List<Song> mListSongs, IOnClickSongItemListener iOnClickSongItemListener) {
        this.mListSongs = mListSongs;
        this.iOnClickSongItemListener = iOnClickSongItemListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng item trong danh sách bài hát
        ItemSongBinding itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongViewHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mListSongs.get(position);
        if (song == null) {
            return; // Tránh lỗi NullPointerException nếu bài hát không tồn tại
        }
        // Load ảnh bài hát bằng GlideUtils
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongBinding.imgSong);

        // Thiết lập thông tin bài hát
        holder.mItemSongBinding.tvSongName.setText(song.getTitle());
        holder.mItemSongBinding.tvArtist.setText(song.getArtist());
        holder.mItemSongBinding.tvCountView.setText(String.valueOf(song.getCount()));

        // Xử lý sự kiện click vào item bài hát
        holder.mItemSongBinding.layoutItem.setOnClickListener(v -> iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        return null == mListSongs ? 0 : mListSongs.size(); // Trả về số lượng bài hát
    }

    // ViewHolder để giữ tham chiếu đến các view trong item layout
    public static class SongViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongBinding mItemSongBinding; // Binding của item

        public SongViewHolder(ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot()); // Gọi constructor của ViewHolder
            this.mItemSongBinding = itemSongBinding;
        }
    }
}
