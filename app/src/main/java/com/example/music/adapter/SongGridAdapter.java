package com.example.music.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.databinding.ItemSongGridBinding;
import com.example.music.listener.IOnClickSongItemListener;
import com.example.music.model.Song;
import com.example.music.utils.GlideUtils;

import java.util.List;

// Adapter cho RecyclerView hiển thị danh sách bài hát dưới dạng lưới
public class SongGridAdapter extends RecyclerView.Adapter<SongGridAdapter.SongGridViewHolder> {

    private final List<Song> mListSongs; // Danh sách bài hát
    public final IOnClickSongItemListener iOnClickSongItemListener; // Listener xử lý sự kiện click

    // Constructor khởi tạo adapter với danh sách bài hát và listener
    public SongGridAdapter(List<Song> mListSongs, IOnClickSongItemListener iOnClickSongItemListener) {
        this.mListSongs = mListSongs;
        this.iOnClickSongItemListener = iOnClickSongItemListener;
    }

    @NonNull
    @Override
    public SongGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng item trong danh sách bài hát dạng lưới
        ItemSongGridBinding itemSongGridBinding = ItemSongGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongGridViewHolder(itemSongGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongGridViewHolder holder, int position) {
        Song song = mListSongs.get(position);
        if (song == null) {
            return; // Tránh lỗi NullPointerException nếu bài hát không tồn tại
        }
        // Load ảnh bài hát bằng GlideUtils
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongGridBinding.imgSong);

        // Thiết lập thông tin bài hát
        holder.mItemSongGridBinding.tvSongName.setText(song.getTitle());
        holder.mItemSongGridBinding.tvArtist.setText(song.getArtist());

        // Xử lý sự kiện click vào item bài hát
        holder.mItemSongGridBinding.layoutItem.setOnClickListener(v -> iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        return null == mListSongs ? 0 : mListSongs.size(); // Trả về số lượng bài hát
    }

    // ViewHolder để giữ tham chiếu đến các view trong item layout
    public static class SongGridViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongGridBinding mItemSongGridBinding; // Binding của item

        public SongGridViewHolder(ItemSongGridBinding itemSongGridBinding) {
            super(itemSongGridBinding.getRoot()); // Gọi constructor của ViewHolder
            this.mItemSongGridBinding = itemSongGridBinding;
        }
    }
}
