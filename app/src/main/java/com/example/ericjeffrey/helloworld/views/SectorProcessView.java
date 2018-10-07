package com.example.ericjeffrey.helloworld.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

public class SectorProcessView extends View {
    /**
     * 默认动画持续时间为 1s
     */
    public static final int DEFAULT_ANIMATION_DURATION = 1000;

    /**
     * 宽高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 是否显示背景圆以及文字
     */
    private boolean mShowBGOval;
    private boolean mShowText;
    /**
     * 扇形矩形与背景色矩形
     */
    private RectF mSectorRect;
    private RectF mBGRect;
    /**
     * 扇形颜色，背景圆形颜色，文字颜色
     * 默认：#77007FFF #007FFF #000000
     */
    private int mSectorColor;
    private int mBGColor;
    private int mTextColor;
    /**
     * 文字大小，默认为 40
     */
    private float mTextSize;
    /**
     * 绘制动画时当前比例变化的监听器
     */
    private OnRateChangeListener mOnRateChangeListener;

    /**
     * 当前比例，绘制动画时使用
     */
    private float mCurrentRate;
    /**
     * 是否正在绘制动画
     */
    private boolean mIsAnimating;

    public SectorProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mShowBGOval = true;
        mShowText = true;

        mSectorRect = new RectF(0, 0, 0, 0);
        mBGRect = new RectF(0, 0, 0, 0);

        mSectorColor = Color.parseColor("#77007FFF");
        mBGColor = Color.parseColor("#007FFF");
        mTextColor = Color.BLACK;
        mTextSize = 40;

        mWidth = mHeight = 0;

        mCurrentRate = (float) (2 / 3.0);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWidth == 0) {
            mWidth = getWidth();
            mHeight = getHeight();
        }

        if (mShowBGOval) {
            mBGRect.left = mWidth * (1 - mCurrentRate) / 2;
            mBGRect.right = mWidth * (1 + mCurrentRate) / 2;
            mBGRect.top = mHeight * (1 - mCurrentRate) / 2;
            mBGRect.bottom = mHeight * (1 + mCurrentRate) / 2;
            mPaint.setColor(mBGColor);
            canvas.drawOval(mBGRect, mPaint);
        }

        if (mSectorRect.bottom == 0) {
            mSectorRect.right = mWidth;
            mSectorRect.bottom = mHeight;
        }
        mPaint.setColor(mSectorColor);
        canvas.drawArc(mSectorRect, 0, mCurrentRate * 360, true, mPaint);

        if (mShowText) {
            mPaint.setColor(mTextColor);
            canvas.drawText(String.format(Locale.CHINA, "%.0f%%", mCurrentRate * 100),
                    mWidth / 2, mHeight / 2 + mTextSize / 3, mPaint);
        }

        if (mOnRateChangeListener != null)
            mOnRateChangeListener.onRateChange(this, mCurrentRate);
    }

    /**
     * 开始绘制动画
     *
     * @param fromRate        起始比例，0~1之间
     * @param toRate          结束比例，0~1之间
     * @param animateDuration 时长
     */
    public void startAnimate(final float fromRate, final float toRate, final int animateDuration) {
        if (mIsAnimating)
            return;
        if (fromRate > 1 || toRate > 1)
            return;
        mIsAnimating = true;
        mCurrentRate = fromRate;
        new Thread(new Runnable() {
            int tick = 1;

            @Override
            public void run() {
                while (tick <= animateDuration) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentRate += (toRate - fromRate) / animateDuration;
                            invalidate();
                        }
                    });
                    tick++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mIsAnimating = false;
            }
        }).start();
    }

    public void drawRate(float rate) {
        if (mIsAnimating)
            return;
        mCurrentRate = rate;
        invalidate();
    }

    public void setBGOvalColor(int bgColor) {
        this.mBGColor = bgColor;
        invalidate();
    }

    public void setSectorColor(int sectorColor) {
        this.mSectorColor = sectorColor;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidate();
    }

    public void setShowBGOval(boolean showBGOval) {
        this.mShowBGOval = showBGOval;
    }

    public void setShowText(boolean showText) {
        this.mShowText = showText;
    }

    public void setOnRateChangeListener(OnRateChangeListener onRateChangeListener) {
        this.mOnRateChangeListener = onRateChangeListener;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        mPaint.setTextSize(mTextSize);
        invalidate();
    }

    /**
     * 绘制动画时比例变化监听器
     */
    public interface OnRateChangeListener {
        void onRateChange(View view, float rate);
    }
}
