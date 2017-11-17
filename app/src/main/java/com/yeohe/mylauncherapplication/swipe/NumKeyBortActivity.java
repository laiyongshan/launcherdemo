package com.yeohe.mylauncherapplication.swipe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yeohe.mylauncherapplication.R;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/10/20.
 */

public class NumKeyBortActivity extends Activity {

    private StringBuffer mInputString = new StringBuffer("");

    @BindViews({R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button_clear,
            R.id.button7, R.id.button8, R.id.button9, R.id.btnSure,
            R.id.button0, R.id.button_dot, R.id.button00,})
    Button[] btns;

    @BindView(R.id.button_back)
    ImageButton button_back;

    @BindView(R.id.tvInputView)
    TextView tvInputView;


    public void beforeInitView() {
        setContentView(R.layout.activity_numkeyboard);
        ButterKnife.bind(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beforeInitView();

        initViews();
    }

    /*
    * 初始化控件
    * */
    private void initViews() {

        View.OnClickListener btnNumOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (v.getId() != button_back.getId()) {
                        Button b = (Button) v;
                        NumKeyBortActivity.this.onNumberClicked(b.getText().toString(), b.getId());
                    } else {
                        ImageButton b = (ImageButton) v;
                        NumKeyBortActivity.this.onNumberClicked("", b.getId());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };


        for (Button btn : btns) {
            btn.setOnClickListener(btnNumOnClickListener);
        }

        button_back.setOnClickListener(btnNumOnClickListener);
    }


    private void onNumberClicked(String str, int viewId) {
        if (viewId == R.id.button_back) {//回退一位
            int len = mInputString.length();
            if (len > 0) {
                mInputString.delete(mInputString.length() - 1, mInputString.length());
                tvInputView.setText(mInputString.toString());
            }
        } else if (viewId == R.id.button_clear) {//清除所有
            mInputString.delete(0, mInputString.length());
            tvInputView.setText("");
        } else if (viewId == R.id.btnSure) {//确认

        } else {
            int index = mInputString.indexOf(".");
            if (index < 0) {
                if (mInputString.length() < 10) {
                    mInputString.append(str);
                }
                tvInputView.setText(mInputString);
            } else {
                if (index + 1 == mInputString.length() || mInputString.length() == index + 2) {
                    if (!str.equalsIgnoreCase(".")) {
                        mInputString.append(str);
                        tvInputView.setText(mInputString);
                    }
                }
            }
        }
    }

}
