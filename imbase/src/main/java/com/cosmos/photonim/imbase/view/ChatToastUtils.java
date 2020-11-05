package com.cosmos.photonim.imbase.view;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;

public class ChatToastUtils {
    private static Toast toast;

    public static void showChatVoice() {
        showChatToast(R.drawable.chat_toast_voice, ImBaseBridge.getInstance().getApplication().getResources().getString(R.string.chat_toast_voice));
    }

    public static void showChatTimeWarn() {
        showChatToast(R.drawable.chat_toast_warn, ImBaseBridge.getInstance().getApplication().getResources().getString(R.string.chat_toast_time_short));
    }

    public static void showChatExpireWarn() {
        showChatToast(R.drawable.chat_toast_warn, ImBaseBridge.getInstance().getApplication().getResources().getString(R.string.chat_toast_expire));
    }

    public static void showChatSendFailedWarn() {
        showChatToast(R.drawable.chat_toast_warn, ImBaseBridge.getInstance().getApplication().getResources().getString(R.string.chat_toast_send_failed));
    }

    private static void showChatToast(int resId, String content) {
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(ImBaseBridge.getInstance().getApplication());
        View view = View.inflate(ImBaseBridge.getInstance().getApplication(), R.layout.chat_toast, null);
        ((ImageView) view.findViewById(R.id.ivTip)).setImageResource(resId);
        ((TextView) view.findViewById(R.id.tvTip)).setText(content);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
