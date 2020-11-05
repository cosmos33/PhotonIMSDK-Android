package com.cosmos.photonim.imbase.utils;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.cosmos.photonim.imbase.R;

import java.util.ArrayList;
import java.util.List;

public class AtEditText extends AppCompatEditText {
    ArrayList<Entity> atList;
    private Context context;
    private OnAtInputListener mOnAtInputListener;

    public AtEditText(Context context) {
        super(context);
        initView(context);
    }

    public AtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        atList = new ArrayList<>();
        setOnKeyListener(new MyOnKeyListener(this));
        addTextChangedListener(new MentionTextWatcher());
    }


    /**
     * 添加@的内容
     *
     * @param parm 最多传4个 ,分别对应 id,nickName,parm1,parm2
     */
    public void addAtContent(String... parm) {
        atList.add(new Entity(parm[0], parm[1]));
        // 光标位置之前的字符是否是 @ , 如果不是 加上一个@
        int selectionStart = getSelectionStart();
        // 获取当前内容
        String sss = getText().toString();
        // 获取光标前以为字符
        String s = selectionStart != 0 ? sss.toCharArray()[selectionStart - 1] + "" : "";
        // 将内容插入 , 改变文字颜色
        setText(changeTextColor(sss.substring(0, selectionStart) + (!s.equals("@") ? "@" : "") + parm[1] + sss.substring(selectionStart, sss.length()))); //字符串替换，删掉符合条件的字符串
        // 设置光标位置
        setSelection((sss.substring(0, selectionStart) + (!s.equals("@") ? "@" : "") + parm[1]).length());
    }

    private SpannableString changeTextColor(String sText) {
        int startIndex = 0;
        List<Integer> spanIndexes = getSpanIndexes(sText);

        SpannableString spanText = new SpannableString(sText);

        if (spanIndexes != null && spanIndexes.size() != 0) {
            for (int i = 0; i < spanIndexes.size(); i++) {
                if (i % 2 == 0) {  // 开始位置
                    startIndex = spanIndexes.get(i);
                } else {  // 结束位置
                    spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), startIndex, spanIndexes.get(i), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spanText;
//        return changeChatEmoji(sText, spanText);
    }

    public List<Integer> getSpanIndexes(String sText) {
        int endIndex = 0;
        int startIndex = 0;
        List<Integer> spanIndexes = new ArrayList<Integer>();
        for (int i = 0; i < atList.size(); i++) {
            String tempname = "@" + atList.get(i).getName();
            if ((startIndex = sText.indexOf(tempname, endIndex)) != -1) {
                endIndex = startIndex + tempname.length();
                spanIndexes.add(startIndex);//nickName 的开始索引，键值为偶数，从0开始
                spanIndexes.add(startIndex + tempname.length()); //nickName 的结束索引，键值为奇数，从1开始
            }
        }
        return spanIndexes;
    }

    public void clearAtStatus() {
        if (atList != null) {
            atList.clear();
        }
    }

    private class MyOnKeyListener implements OnKeyListener {
        private EditText editText;

        public MyOnKeyListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) { //当为删除键并且是按下动作时执行
                return myDelete();
            }
            return false;
        }

        /**
         * 删除方法
         *
         * @return
         */
        private boolean myDelete() {
            String content = editText.getText().toString();
            int selectionStart = editText.getSelectionStart();
            int endIndex = 0;
            int startIndex;
            int deleteNum = 0;
            List<Integer> spanIndexes = new ArrayList<Integer>();
            for (int i = 0; i < atList.size(); i++) {
                String name = "@" + atList.get(i).getName();
                if ((startIndex = content.indexOf(name, endIndex)) != -1) {
                    if (startIndex > selectionStart) break; // 如果开始索引值,大于光标位置,那么退出遍历
                    endIndex = startIndex + name.length();
                    deleteNum = i;
                    spanIndexes.add(startIndex);//nickName 的开始索引，键值为偶数，从0开始
                    spanIndexes.add(startIndex + name.length()); //nickName 的结束索引，键值为奇数，从1开始
                    if (endIndex > selectionStart) break; // 如果结束索引值,大于光标位置,那么退出遍历
                }
            }
            // spanIndexes 必须大于0 且 光标位置不能大于 结束索引位置
            if (spanIndexes.size() > 0 && spanIndexes.get(spanIndexes.size() - 2) < selectionStart && spanIndexes.get(spanIndexes.size() - 1) >= selectionStart) {
                editText.setText(changeTextColor(content.substring(0, spanIndexes.get(spanIndexes.size() - 2)) + content.substring(spanIndexes.get(spanIndexes.size() - 1)))); //字符串替换，删掉符合条件的字符串
                editText.setSelection(spanIndexes.get(spanIndexes.size() - 2));  // 设置光标位置
                atList.remove(deleteNum);
                return true;
            } else {
                return false;
            }
        }

    }


    private class MentionTextWatcher implements TextWatcher {
        //若从整串string中间插入字符，需要将插入位置后面的range相应地挪位
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int index, int i1, int count) {
            if (mOnAtInputListener != null) {
                if (count == 1 && !TextUtils.isEmpty(charSequence)) {
                    char mentionChar = charSequence.toString().charAt(index);
                    if ('@' == mentionChar) {
                        mOnAtInputListener.onAtCharacterInput();
                    }
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

//    /**
//     * 刷新@列表
//     */
//    public void refreshAtList(String... parm) {
//        atList.add(new Entity(parm[0], parm[1], parm[2]));
//        setText(changeTextColor(getText().toString()));
//    }

    public ArrayList<Entity> getAtList() {
        String content = getEditableText().toString();
        int endIndex = 0;
        int startIndex = 0;
        for (int i = 0; i < atList.size(); i++) {
            String tempname = "@" + atList.get(i).getName();
            if ((startIndex = content.indexOf(tempname, endIndex)) != -1) {
                endIndex = startIndex + tempname.length();
            } else {
                atList.remove(i);
                i--;
            }
        }
        return atList;
    }

    public void setOnAtInputListener(OnAtInputListener OnAtInputListener) {
        mOnAtInputListener = OnAtInputListener;
    }

    public interface OnAtInputListener {
        void onAtCharacterInput();
    }


    /**
     * @*** 格式的实体类
     */
    static public class Entity {
        String id;
        String name;
        String parm1;
        String parm2;

        /**
         * @param parm 最多传4个 ,分别对应 id,nickName,parm1,parm2
         */
        public Entity(String... parm) {
            if (parm.length >= 1)
                this.id = parm[0];
            if (parm.length >= 2)
                this.name = parm[1];
            if (parm.length >= 3)
                this.parm1 = parm[2];
            if (parm.length >= 4)
                this.parm2 = parm[3];
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParm1() {
            return parm1;
        }

        public void setParm1(String parm1) {
            this.parm1 = parm1;
        }

        public String getParm2() {
            return parm2;
        }

        public void setParm2(String parm2) {
            this.parm2 = parm2;
        }

        public void containIndex(int index) {

        }
    }
}
