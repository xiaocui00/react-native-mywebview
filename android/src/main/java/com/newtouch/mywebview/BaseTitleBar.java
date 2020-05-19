package com.newtouch.mywebview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @Desc: TitleBar
 */
public class BaseTitleBar extends Toolbar implements View.OnClickListener {
    public static final int DISPLAY = 0;
    public static final int HIDE = 1;
    public static final int HIDEALL = 2;
    private View vStatusBar;
    private TextView mTitleTv;
    private ImageView mFinish;
    private LinearLayout rex;
    private TextView mRightTv;
    private LinearLayout mTitleBarBackground;
    private ImageView mBackImageView;
    private LinearLayout a;
    private ImageView mRightImage;

    private onRightTextViewClickListener onRightTextViewClickListener;
    private onRightImageViewClickListener onRightImageViewClickListener;
    private onLeftBackClickListener onLeftBackClickListener;
    private onLeftFinishClickListener onLeftFinishClickListener;

    public BaseTitleBar(Context context) {
        this(context, null);
    }

    public BaseTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context mContext, AttributeSet attrs, int defStyleAttr) {

    }

    private void initView(Context mContext) {
        inflate(mContext, R.layout.layout_base_title, this);

        vStatusBar = findViewById(R.id.vStatusBar);
        ViewGroup.LayoutParams layoutParamsBar = vStatusBar.getLayoutParams();
        layoutParamsBar.height = getStatusBarHeight(mContext);
        vStatusBar.setLayoutParams(layoutParamsBar);

        mBackImageView = findViewById(R.id.mBackImageView);
        a = findViewById(R.id.a);
        mFinish = findViewById(R.id.mFinish);
        rex = findViewById(R.id.rex);
//        mBackImageView.setOnClickListener(this);
        a.setOnClickListener(this);
//        mFinish.setOnClickListener(this);
        rex.setOnClickListener(this);

        mTitleTv = findViewById(R.id.mTitleTv);
        mTitleTv.setOnClickListener(this);

        mRightTv = findViewById(R.id.mRightTv);
        mRightImage = findViewById(R.id.mRightImage);
        mRightTv.setOnClickListener(this);
        mRightImage.setOnClickListener(this);

        mTitleBarBackground = findViewById(R.id.mTitleBarBackground);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView(getContext());
    }

    /**
     * 标题文字
     *
     * @param title string
     */
    public void setTitle(String title, int visible) {
        if (DISPLAY == visible) {
            mTitleTv.setVisibility(VISIBLE);
        } else if (HIDE == visible) {
            mTitleTv.setVisibility(INVISIBLE);
        }
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    /**
     * 标题 Color
     *
     * @param color color
     */
    public void setTitleColor(int color) {
        if (mTitleTv != null) {
            mTitleTv.setTextColor(color);
        }
    }

    /**
     * 标题大小
     *
     * @param spSize SP
     */
    public void setTitleSize(float spSize) {
        if (mTitleTv != null) {
            mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        }
    }

    /**
     * 右侧TextView 文字
     *
     * @param value string
     */
    public void setRightView(String value, int visibility) {
//        0分享按钮，1文字按钮，2全部隐藏
        switch (visibility) {
            case DISPLAY:
                mRightImage.setVisibility(VISIBLE);
                break;
            case HIDE:
                if (mRightTv != null) {
                    mRightTv.setVisibility(VISIBLE);
                    mRightTv.setText(value);
                }
                break;
            case HIDEALL:
                mRightImage.setVisibility(GONE);
                mRightTv.setVisibility(GONE);
                break;
            default:
                break;

        }
    }

    /**
     * 右侧TextView 文字
     *
     * @param  visibility int
     */
    public void setLeftView(int visibility) {
//        默认显示全部 0退出按钮，1返回按钮，2全部隐藏，3全部显示
        switch (visibility) {
            case DISPLAY:
                mFinish.setVisibility(VISIBLE);
                break;
            case HIDE:
                mBackImageView.setVisibility(VISIBLE);
                break;
            case HIDEALL:
                mFinish.setVisibility(GONE);
                mBackImageView.setVisibility(GONE);
                break;
            case 3:
                mFinish.setVisibility(VISIBLE);
                mBackImageView.setVisibility(VISIBLE);
                break;
            default:
                break;

        }
    }

    /**
     * 右侧TextView color
     *
     * @param color color
     */
    public void setRightTextColor(int color) {
        if (mRightTv != null) {
            mRightTv.setTextColor(color);
        }
    }

    /**
     * 右侧TextView Size
     *
     * @param spSize 单位 Sp
     */
    public void setRightTextSize(float spSize) {
        if (mRightTv != null) {
            mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        }
    }

    /**
     * TitleBar背景色
     *
     * @param resValue color
     */
    public void setTitleBarBackGroundRes(Drawable resValue) {
        if (resValue == null) {
            resValue = new ColorDrawable(getContext().getResources().getColor(R.color.paleRed));
        }
        if (mTitleBarBackground != null) {
            mTitleBarBackground.setBackground(resValue);
        }

    }




    public void setRightTextDrawable(Drawable resValue) {
        if (resValue == null || mRightTv == null) {
            return;
        }
        mRightTv.setBackground(null);
        mRightTv.setCompoundDrawablePadding(3);
        resValue.setBounds(0, 0, resValue.getMinimumWidth(), resValue.getMinimumHeight());
        mRightTv.setCompoundDrawables(null, resValue, null, null);
    }

    public void setRightImage(Drawable resValue) {
        if (resValue == null) {
            return;
        }
        mRightImage.setVisibility(View.VISIBLE);
        mRightTv.setVisibility(View.GONE);
        if (mRightImage != null) {
            mRightImage.setImageDrawable(resValue);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.a && onLeftBackClickListener != null) {
            onLeftBackClickListener.onBackClickListener(v);
        } else if (v.getId() == R.id.rex && onLeftFinishClickListener != null){
            onLeftFinishClickListener.onFinishClickListener(v);
        } else if (v.getId() == R.id.mRightTv && onRightTextViewClickListener != null){
            onRightTextViewClickListener.onTextViewClickListener(v);
        } else if (v.getId() == R.id.mRightImage && onRightImageViewClickListener != null){
            onRightImageViewClickListener.onImageViewClickListener(v);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public interface onRightTextViewClickListener {

       void onTextViewClickListener(View v);
    }

    public interface onRightImageViewClickListener {
        void onImageViewClickListener(View v);
    }

    public interface onLeftBackClickListener {
        void onBackClickListener(View v);
    }

    public interface onLeftFinishClickListener {
        void onFinishClickListener(View v);
    }

    public void setOnRightTextViewClickListener(BaseTitleBar.onRightTextViewClickListener onRightTextViewClickListener) {
        this.onRightTextViewClickListener = onRightTextViewClickListener;
    }

    public void setOnRightImageViewClickListener(BaseTitleBar.onRightImageViewClickListener onRightImageViewClickListener) {
        this.onRightImageViewClickListener = onRightImageViewClickListener;
    }

    public void setOnLeftBackClickListener(BaseTitleBar.onLeftBackClickListener onLeftBackClickListener) {
        this.onLeftBackClickListener = onLeftBackClickListener;
    }

    public void setOnLeftFinishClickListener(BaseTitleBar.onLeftFinishClickListener onLeftFinishClickListener) {
        this.onLeftFinishClickListener = onLeftFinishClickListener;
    }
}
