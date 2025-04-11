package com.example.music.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music.constant.AboutUsConfig;
import com.example.music.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen") // Bỏ qua cảnh báo về màn hình splash mặc định của Android 12+
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding mActivitySplashBinding; // Biến binding để liên kết với giao diện XML

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ẩn tiêu đề của cửa sổ
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Hiển thị màn hình toàn màn hình
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Khởi tạo View Binding
        mActivitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mActivitySplashBinding.getRoot());

        // Cấu hình giao diện splash
        initUi();
        // Chuyển sang MainActivity sau một khoảng thời gian nhất định
        startMainActivity();
    }

    private void initUi() {
        // Thiết lập tiêu đề và slogan từ cấu hình
        mActivitySplashBinding.tvAboutUsTitle.setText(AboutUsConfig.ABOUT_US_TITLE);
        mActivitySplashBinding.tvAboutUsSlogan.setText(AboutUsConfig.ABOUT_US_SLOGAN);
    }

    private void startMainActivity() {
        // Tạo một Handler để trì hoãn chuyển màn hình
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Khởi tạo Intent để chuyển sang MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            // Xóa các Activity trước đó khỏi ngăn xếp và bắt đầu một nhiệm vụ mới
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Đóng SplashActivity để không thể quay lại màn hình này
        }, 1500); // Trì hoãn 1.5 giây
    }
}
