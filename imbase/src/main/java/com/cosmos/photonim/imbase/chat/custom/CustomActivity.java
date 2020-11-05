package com.cosmos.photonim.imbase.chat.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.view.TitleBar;

import butterknife.BindView;

public class CustomActivity extends BaseActivity {

    public final static String INTENT_CUSTOM = "com.cosmos.photonim.imbase.chat.custom";
    @BindView(R2.id.titleBar1)
    TitleBar titleBar;

    @BindView(R2.id.customArg1)
    EditText customArg1;

    @BindView(R2.id.customArg2)
    EditText customArg2;

    @BindView(R2.id.customData)
    EditText customData;

    @BindView(R2.id.customDataSize)
    EditText customDataSize;


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CustomActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CUSTOM);
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.layout_chat_custom);




        initView();
    }


    private void initView(){
        titleBar.setTitle("自定消息");
        titleBar.setRightTextEvent("发送", 0xffffffff, R.drawable.drawable_map_send, v -> {
            CustomInfo customInfo = new CustomInfo();
            try {
                String arg1 = this.customArg1.getText().toString();
                String arg2 = this.customArg2.getText().toString();
                if(!TextUtils.isEmpty(arg1)){
                    customInfo.customArg1 = Integer.parseInt(arg1);
                }
                if(!TextUtils.isEmpty(arg2)){
                    customInfo.customArg2 = Integer.parseInt(arg2);
                }
                String data = this.customData.getText().toString();
                if(!TextUtils.isEmpty(data)){
                    customInfo.customData = data.getBytes();
                    customInfo.customDateSize = customInfo.customData.length;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.putExtra(INTENT_CUSTOM,customInfo);
            setResult(Activity.RESULT_OK,intent);
            CustomActivity.this.finish();

        });
    }
}
