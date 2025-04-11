package com.example.music.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.music.R;

/**
 * Lớp cơ sở cho tất cả các Activity trong ứng dụng.
 * Cung cấp các phương thức để hiển thị ProgressDialog và AlertDialog.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected MaterialDialog progressDialog, alertDialog; // Hộp thoại tiến trình và cảnh báo

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog(); // Tạo hộp thoại tiến trình
        createAlertDialog(); // Tạo hộp thoại cảnh báo
    }

    /**
     * Tạo hộp thoại tiến trình (ProgressDialog)
     */
    public void createProgressDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.msg_please_waiting) // Nội dung: "Vui lòng chờ"
                .progress(true, 0) // Hiển thị vòng quay loading
                .build();
    }

    /**
     * Hiển thị hoặc ẩn ProgressDialog
     * @param value true: hiển thị, false: ẩn
     */
    public void showProgressDialog(boolean value) {
        if (value) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show(); // Hiển thị hộp thoại
                progressDialog.setCancelable(false); // Không thể hủy
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss(); // Ẩn hộp thoại
            }
        }
    }

    /**
     * Ẩn ProgressDialog và AlertDialog nếu đang hiển thị
     */
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    /**
     * Tạo hộp thoại cảnh báo (AlertDialog)
     */
    public void createAlertDialog() {
        alertDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name) // Tiêu đề là tên ứng dụng
                .positiveText(R.string.action_ok) // Nút "OK"
                .cancelable(false) // Không thể tắt bằng cách nhấn ra ngoài
                .build();
    }

    /**
     * Hiển thị AlertDialog với nội dung tùy chỉnh
     * @param errorMessage Nội dung lỗi
     */
    public void showAlertDialog(String errorMessage) {
        alertDialog.setContent(errorMessage);
        alertDialog.show();
    }

    /**
     * Hiển thị AlertDialog với nội dung từ resource
     * @param resourceId ID của chuỗi trong strings.xml
     */
    public void showAlertDialog(@StringRes int resourceId) {
        alertDialog.setContent(resourceId);
        alertDialog.show();
    }

    /**
     * Thiết lập khả năng hủy của ProgressDialog
     * @param isCancel true: có thể hủy, false: không thể hủy
     */
    public void setCancelProgress(boolean isCancel) {
        if (progressDialog != null) {
            progressDialog.setCancelable(isCancel);
        }
    }

    @Override
    protected void onDestroy() {
        // Đảm bảo hộp thoại không còn hiển thị khi Activity bị hủy
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }
}
