package com.abeljoo.android.monetaryedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by abel on 15-7-2.
 */
public class MonetaryEditText extends EditText {

    private static final String DEFAULT_LARGE = "999999.99";

    private static final String DEFAULT_SMALL = "0";

    private static final int DEFAULT_DECIMAL_NUMBER = 2;

    public interface OnAbnormalListener {
        void onAbnormal(AbnormalType abnType);
    }

    public enum AbnormalType {
        /**
         * 数值过大
         */
        TooLarge,
        /**
         * 数值过小
         */
        TooSmall,
        /**
         * 小数位过多
         */
        TooManyDecimal,
    }

    private String mLarge;

    private String mSmall;

    private int mDecimalNumber;

    private String mOldText;

    private OnAbnormalListener mOnAbnormalListener;

    public MonetaryEditText(Context context) {
        super(context);
        init(context, null);
    }

    public MonetaryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MonetaryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonetaryEditText);

        mLarge = a.getString(R.styleable.MonetaryEditText_large);
        mLarge = TextUtils.isEmpty(mLarge) ? DEFAULT_LARGE : mLarge;

        mSmall = a.getString(R.styleable.MonetaryEditText_small);
        mSmall = TextUtils.isEmpty(mSmall) ? DEFAULT_SMALL : mSmall;

        mDecimalNumber = a.getInt(R.styleable.MonetaryEditText_decimalNumber,
                DEFAULT_DECIMAL_NUMBER);

        a.recycle();

        setKeyListener(DigitsKeyListener.getInstance(false, true));

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        /**
         * ***********************************************
         * 空字符串处理
         * ***********************************************
         */
        if (TextUtils.isEmpty(text)) {
            mOldText = text.toString();
            return;
        }

        /**
         * ***********************************************
         * 第0个字符是一个“.”的情况：为用户在第0个字符前插入一个“0”
         * ***********************************************
         */
        if (text.toString().toCharArray()[0] == '.') {
            setText("0" + text.toString());
            mOldText = getText().toString();
            return;
        }

        /**
         * ***********************************************
         * 校验是否合法数字。唯有是严格的合法数字时，才可进行业务校验
         * ***********************************************
         */
        try {
            Double.valueOf(text.toString());
        } catch (Exception e) {
            return;
        }

        /************************** 业务校验 **************************/

        if (isTooManyDecimal(text.toString())) {
            setText(mOldText);
            if (mOnAbnormalListener != null) {
                mOnAbnormalListener.onAbnormal(AbnormalType.TooManyDecimal);
            }
            return;
        }

        if (isTooLarge(text.toString())) {
            setText(mOldText);
            if (mOnAbnormalListener != null) {
                mOnAbnormalListener.onAbnormal(AbnormalType.TooLarge);
            }
            return;
        }

        if (isTooSmall(text.toString())) {
            setText(mOldText);
            if (mOnAbnormalListener != null) {
                mOnAbnormalListener.onAbnormal(AbnormalType.TooSmall);
            }
            return;
        }

        mOldText = text.toString();
        setSelection(mOldText.length());
    }

    private boolean isTooLarge(String text) {
        return Double.valueOf(text) > Double.valueOf(mLarge);
    }

    private boolean isTooSmall(String text) {
        return Double.valueOf(text) < Double.valueOf(mSmall);
    }

    private boolean isTooManyDecimal(String text) {
        String[] numberSet = text.split("\\.");
        return numberSet.length > 1 && numberSet[1].length() > mDecimalNumber;
    }

    public void setOnAbnormalListener(OnAbnormalListener l) {
        mOnAbnormalListener = l;
    }
}
