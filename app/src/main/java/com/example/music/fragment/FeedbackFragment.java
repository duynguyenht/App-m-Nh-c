package com.example.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.music.MyApplication;
import com.example.music.R;
import com.example.music.activity.MainActivity;
import com.example.music.constant.GlobalFuntion;
import com.example.music.databinding.FragmentFeedbackBinding;
import com.example.music.model.Feedback;
import com.example.music.utils.StringUtil;

public class FeedbackFragment extends Fragment {

    private FragmentFeedbackBinding mFragmentFeedbackBinding; // Binding để liên kết với layout XML của fragment

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout fragment và lấy instance của binding
        mFragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false);

        // Thiết lập sự kiện click cho nút gửi phản hồi
        mFragmentFeedbackBinding.tvSendFeedback.setOnClickListener(v -> onClickSendFeedback());

        return mFragmentFeedbackBinding.getRoot(); // Trả về root view của fragment
    }

    private void onClickSendFeedback() {
        if (getActivity() == null) {
            return; // Nếu activity null, không thực hiện gì cả
        }
        MainActivity activity = (MainActivity) getActivity();

        // Lấy dữ liệu từ các ô nhập liệu
        String strName = mFragmentFeedbackBinding.edtName.getText().toString();
        String strPhone = mFragmentFeedbackBinding.edtPhone.getText().toString();
        String strEmail = mFragmentFeedbackBinding.edtEmail.getText().toString();
        String strComment = mFragmentFeedbackBinding.edtComment.getText().toString();

        // Kiểm tra các trường bắt buộc (tên & nội dung phản hồi)
        if (StringUtil.isEmpty(strName)) {
            GlobalFuntion.showToastMessage(activity, getString(R.string.name_require)); // Hiển thị thông báo lỗi nếu thiếu tên
        } else if (StringUtil.isEmpty(strComment)) {
            GlobalFuntion.showToastMessage(activity, getString(R.string.comment_require)); // Hiển thị thông báo lỗi nếu thiếu nội dung phản hồi
        } else {
            // Hiển thị hộp thoại loading khi đang gửi phản hồi
            activity.showProgressDialog(true);

            // Tạo một đối tượng Feedback chứa thông tin người dùng nhập
            Feedback feedback = new Feedback(strName, strPhone, strEmail, strComment);

            // Đẩy dữ liệu lên Firebase Realtime Database
            MyApplication.get(getActivity()).getFeedbackDatabaseReference()
                    .child(String.valueOf(System.currentTimeMillis())) // Dùng timestamp làm key để đảm bảo tính duy nhất
                    .setValue(feedback, (databaseError, databaseReference) -> {
                        activity.showProgressDialog(false); // Ẩn hộp thoại loading sau khi gửi xong
                        sendFeedbackSuccess(); // Gọi phương thức xử lý khi gửi thành công
                    });
        }
    }

    public void sendFeedbackSuccess() {
        // Ẩn bàn phím, hiển thị thông báo gửi thành công và xóa nội dung nhập liệu
        GlobalFuntion.hideSoftKeyboard(getActivity());
        GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_send_feedback_success));

        // Reset các ô nhập liệu về rỗng
        mFragmentFeedbackBinding.edtName.setText("");
        mFragmentFeedbackBinding.edtPhone.setText("");
        mFragmentFeedbackBinding.edtEmail.setText("");
        mFragmentFeedbackBinding.edtComment.setText("");
    }
}
