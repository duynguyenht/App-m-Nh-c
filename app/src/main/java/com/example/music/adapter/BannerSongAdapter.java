package com.example.music.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.databinding.ItemBannerSongBinding;
import com.example.music.listener.IOnClickSongItemListener;
import com.example.music.model.Song;
import com.example.music.utils.GlideUtils;

import java.util.List;

// Adapter cho RecyclerView để hiển thị danh sách các bài hát dưới dạng banner
public class BannerSongAdapter extends RecyclerView.Adapter<BannerSongAdapter.BannerSongViewHolder> {

    private final List<Song> mListSongs; // Danh sách bài hát
    public final IOnClickSongItemListener iOnClickSongItemListener; // Listener xử lý sự kiện click

    // Constructor để khởi tạo adapter với danh sách bài hát và listener
    public BannerSongAdapter(List<Song> mListSongs, IOnClickSongItemListener iOnClickSongItemListener) {
        this.mListSongs = mListSongs;
        this.iOnClickSongItemListener = iOnClickSongItemListener;
    }

    @NonNull
    @Override
    public BannerSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong danh sách
        ItemBannerSongBinding itemBannerSongBinding = ItemBannerSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BannerSongViewHolder(itemBannerSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerSongViewHolder holder, int position) {
        Song song = mListSongs.get(position);
        if (song == null) {
            return; // Tránh lỗi NullPointerException nếu bài hát không tồn tại
        }
        // Load ảnh của bài hát vào ImageView bằng GlideUtils
        GlideUtils.loadUrlBanner(song.getImage(), holder.mItemBannerSongBinding.imageBanner);

        // Xử lý sự kiện click vào item
        holder.mItemBannerSongBinding.layoutItem.setOnClickListener(v -> iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        if (mListSongs != null) {
            return mListSongs.size(); // Trả về số lượng bài hát trong danh sách
        }
        return 0;
    }

    // ViewHolder để giữ tham chiếu đến view trong item layout
    public static class BannerSongViewHolder extends RecyclerView.ViewHolder {

        private final ItemBannerSongBinding mItemBannerSongBinding; // Binding của item

        public BannerSongViewHolder(@NonNull ItemBannerSongBinding itemBannerSongBinding) {
            super(itemBannerSongBinding.getRoot()); // Gọi constructor của ViewHolder
            this.mItemBannerSongBinding = itemBannerSongBinding;
        }
    }
}
