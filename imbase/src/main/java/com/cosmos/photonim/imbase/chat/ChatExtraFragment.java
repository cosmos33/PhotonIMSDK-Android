package com.cosmos.photonim.imbase.chat;

import android.content.Intent;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.emoji.EmojiContainerFragment;
import com.cosmos.photonim.imbase.chat.map.MapActivity;
import com.cosmos.photonim.imbase.chat.media.TakePhotoActivity;
import com.cosmos.photonim.imbase.utils.AtEditText;
import com.cosmos.photonim.imbase.utils.CheckAudioPermission;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.view.ChatToastUtils;
import com.cosmos.photonim.imbase.view.VoiceTextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatExtraFragment extends BaseFragment {
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int SEND_COUNT_LIMIT = 480;
    private static final int VOICE_MAX_LENGTH = 3 * 60 * 1000;
    @BindView(R2.id.llExtra)
    LinearLayout llExtra;
    @BindView(R2.id.ivVoice)
    ImageView ivVoice;
    @BindView(R2.id.etInput)
    AtEditText etInput;
    @BindView(R2.id.ivEmoji)
    ImageView ivEmoji;
    @BindView(R2.id.ivExtra)
    ImageView ivExtra;
    @BindView(R2.id.tvVoice)
    VoiceTextView tvVoice;
    @BindView(R2.id.tvSendMsg)
    TextView tvSendMsg;
    @BindView(R2.id.vsEmoji)
    ViewStub vsEmoji;

    private FrameLayout emojiRoot;
    private EmojiContainerFragment fragment;
    private File voiceFile;
    private OnVoiceEventListener onVoiceEventListener;
    private View rootView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_extra;
    }

    @Override
    protected void initView(View view) {
        rootView = view;
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etInput.getText().toString().trim().length() > 0) {
                    tvSendMsg.setVisibility(View.VISIBLE);
                    ivExtra.setVisibility(View.GONE);
                    llExtra.setVisibility(View.GONE);
                } else {
                    tvSendMsg.setVisibility(View.GONE);
                    ivExtra.setVisibility(View.VISIBLE);
                }
            }
        });

        etInput.setFilters(new InputFilter[]{new EditFilter()});

        etInput.setOnFocusChangeListener((v, hasFocus) -> {
            llExtra.setVisibility(View.GONE);
            if (emojiRoot != null) {
                emojiRoot.setVisibility(View.GONE);
            }
//            dismissExtraLayout();
        });

        etInput.setOnAtInputListener(new AtEditText.OnAtInputListener() {
            @Override
            public void onAtCharacterInput() {
                this.onAtCharacterInput();
            }
        });

        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(SEND_COUNT_LIMIT)});

        tvVoice.setTimeOut(VOICE_MAX_LENGTH);
        tvVoice.setOnEventUpListener(new VoiceTextView.OnEventUpListener() {
            @Override
            public boolean canHandle() {
                return CheckAudioPermission.isHasPermission(getContext());
            }

            @Override
            public void onEventDown() {
                ChatToastUtils.showChatVoice();
                if (onVoiceEventListener != null) {
                    voiceFile = onVoiceEventListener.onVoiceStart();
                }
                setControlEnable(false);
            }

            @Override
            public void onEventCancel() {
                if (onVoiceEventListener != null) {
                    onVoiceEventListener.onVoiceCancel();
                }
                setControlEnable(true);
            }

            @Override
            public void onEventUp() {
                if (onVoiceEventListener != null) {
                    onVoiceEventListener.onVoiceStop();
                }
                setControlEnable(true);
            }

            @Override
            public void onTimeout() {
                ToastUtils.showText("超时自动发送");
                if (onVoiceEventListener != null) {
                    onVoiceEventListener.onVoiceStop();
                }
                setControlEnable(true);
            }
        });
    }

    @OnClick(R2.id.ivVoice)
    public void onVoiceClick() {
        if (tvVoice.getVisibility() == View.VISIBLE) {
            tvVoice.setVisibility(View.GONE);
            etInput.setVisibility(View.VISIBLE);
        } else {
            tvVoice.setVisibility(View.VISIBLE);
            tvVoice.setText("按住说话");
            etInput.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.ivEmoji)
    public void onEmojiClick() {
        Utils.keyBoard(getContext(), etInput, false);
        llExtra.setVisibility(View.GONE);
        if (fragment == null) {
            vsEmoji.inflate();
            emojiRoot = rootView.findViewById(R.id.emojiRoot);
            fragment = (EmojiContainerFragment) getChildFragmentManager().findFragmentById(R.id.emojiContainerFragment);
            fragment.setOnDelListener(new EmojiContainerFragment.OnDelListener() {
                @Override
                public void onDelClick() {
                    String content = etInput.getText().toString();
                    if (TextUtils.isEmpty(content.trim())) {
                        return;
                    }
                    int contentLength = etInput.getText().toString().length();
                    etInput.setText(content.substring(0, contentLength - 1));
                }
            });

//            fragment.setOnSendListener(new EmojiContainerFragment.OnSendListener() {
//                @Override
//                public void onEmojiSend() {
//                    if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
//                        return;
//                    }
//                    chatPresenter.sendText(etInput.getText().toString().trim(), chatWith, MyApplication.getApplication().getUserId(), chatWith, myIcon);
//                }
//            });

            fragment.setOnEmojiClickListener(new EmojiContainerFragment.OnEmojiClickListener() {
                @Override
                public void onEmojiClick(String content) {
                    if (tvVoice.getVisibility() == View.GONE) {
                        etInput.append(content);
                    }
                }
            });
        } else {
            if (emojiRoot.getVisibility() == View.GONE) {
                emojiRoot.setVisibility(View.VISIBLE);
            } else {
                emojiRoot.setVisibility(View.GONE);
            }
        }

    }

    @OnClick(R2.id.llPic)
    public void onPicClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ChatBaseActivity.REQUEST_IMAGE_CODE);
    }

    @OnClick(R2.id.llTakePic)
    public void onTakePic() {
        TakePhotoActivity.start(getActivity());
    }

    @OnClick(R2.id.llPosition)
    public void onPositionClick() {
        MapActivity.start(getActivity());
    }

    private void setControlEnable(boolean enable) {
        ivVoice.setEnabled(enable);
        ivEmoji.setEnabled(enable);
        ivExtra.setEnabled(enable);
        tvSendMsg.setEnabled(enable);
    }

    public View getInput() {
        return etInput;
    }

    public String getContent() {
        return etInput.getText().toString();
    }

    public void clearInput() {
        etInput.setText("");
        etInput.clearAtStatus();
    }

    public ArrayList<AtEditText.Entity> getAtList() {
        return etInput.getAtList();
    }

    public void setLLExtraVisibility(int visibility) {
        llExtra.setVisibility(visibility);
    }

    public int getLLExtraVisibility() {
        return llExtra.getVisibility();
    }

    public void setEmojiRootVisibility(int visibility) {
        if (emojiRoot != null) {
            emojiRoot.setVisibility(visibility);
        }
    }

    public String getVideoFilePath() {
        return voiceFile.getAbsolutePath();
    }

    public void addAtContent(Object o, String atAllContent) {
        etInput.addAtContent(null, atAllContent);
    }

    private class EditFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return null;
        }
    }

    public interface OnVoiceEventListener {
        File onVoiceStart();

        void onVoiceCancel();

        void onVoiceStop();
    }

    public void setOnVoiceEventListener(OnVoiceEventListener onVoiceEventListener) {
        this.onVoiceEventListener = onVoiceEventListener;
    }
}
