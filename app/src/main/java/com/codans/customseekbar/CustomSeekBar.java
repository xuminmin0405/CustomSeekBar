package com.codans.customseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

/**
 * 作者：xmm on 2017/6/7 0007 09:45
 * 邮箱：15067596185@163.com
 */
public class CustomSeekBar extends SeekBar {
    /**
     * 属性
     */
    //当前进度文字的大小
    private float progressTextSize;
    //当前进度文字颜色
    private int progressTextColor;
    //当前进度的指针图片
    private Bitmap cursorBackground;
    //刻度线的长度
    private float scaleLineHeight;
    //刻度线的宽度
    private float scaleLineWidth;
    //刻度线的颜色
    private int scaleLineColor;
    //刻度文字的大小
    private float scaleTextSize;
    //刻度文字的颜色
    private int scaleTextColor;
    /**
     * 画笔
     */
    //当前进度文字画笔
    private Paint progressPaint;
    //刻度线画笔
    private Paint scaleLinePaint;
    //刻度文字画笔
    private Paint scaleTextPaint;
    /**
     * 指针图片宽高
     */
    private float cursorBgWidth, cursorBgHeight;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar, defStyleAttr, 0);
        //初始化属性
        progressTextSize = attr.getDimension(R.styleable.CustomSeekBar_progressTextSize, getDpValue(14));
        progressTextColor = attr.getColor(R.styleable.CustomSeekBar_progressTextColor, getResources().getColor(R.color.colorPrimary));
        int bgResId = attr.getResourceId(R.styleable.CustomSeekBar_cursorBackground, R.mipmap.ic_launcher);
        cursorBackground = BitmapFactory.decodeResource(getResources(), bgResId);
        scaleLineHeight = attr.getDimension(R.styleable.CustomSeekBar_scaleLineHeight, getDpValue(5));
        scaleLineWidth = attr.getDimension(R.styleable.CustomSeekBar_scaleLineWidth, getDpValue(1));
        scaleLineColor = attr.getColor(R.styleable.CustomSeekBar_scaleLineColor, getResources().getColor(R.color.colorPrimary));
        scaleTextSize = attr.getDimension(R.styleable.CustomSeekBar_scaleTextSize, getDpValue(11));
        scaleTextColor = attr.getColor(R.styleable.CustomSeekBar_scaleTextColor, getResources().getColor(R.color.colorPrimary));
        //获取当前进度的指针图片宽高
        cursorBgWidth = cursorBackground.getWidth();
        cursorBgHeight = cursorBackground.getHeight();
        attr.recycle();
        initPaint();
        //设置预留空间
        setPadding((int) Math.ceil(cursorBgWidth) / 2, (int) Math.ceil(progressTextSize + cursorBgHeight + scaleLineHeight),
                (int) Math.ceil(cursorBgWidth) / 2, (int) Math.ceil(scaleTextSize + scaleLineHeight));
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //初始化当前进度文字画笔
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(progressTextColor);
        progressPaint.setTextSize(progressTextSize);
        //初始化刻度线画笔
        scaleLinePaint = new Paint();
        scaleLinePaint.setAntiAlias(true);
        scaleLinePaint.setColor(scaleLineColor);
        scaleLinePaint.setStrokeWidth(scaleLineWidth);
        //初始化刻度文字画笔
        scaleTextPaint = new Paint();
        scaleTextPaint.setAntiAlias(true);
        scaleTextPaint.setColor(scaleTextColor);
        scaleTextPaint.setTextSize(scaleTextSize);
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制指针图片
         */
        Rect bgRect = getProgressDrawable().getBounds();
        //计算指针图片的x坐标
        float cursorBgX = bgRect.width() * getProgress() / getMax();
        //计算指针图片的y坐标
        float cursorBgY = progressTextSize;
        canvas.drawBitmap(cursorBackground, cursorBgX, cursorBgY, progressPaint);
        /**
         * 绘制当前进度文字
         */
        String progressText = getProgress() + "%";
        //测量当前进度文字宽度
        float progressTextWidth = progressPaint.measureText(progressText);
        Paint.FontMetrics fm = progressPaint.getFontMetrics();
        //计算文字基线Y坐标
        float progressTextBaseLineY = progressTextSize / 2 - fm.descent + (fm.bottom - fm.top) / 2;
        //计算当前进度文字X坐标
        float progressTextX = cursorBgX + (cursorBgWidth - progressTextWidth) / 2;
        canvas.drawText(progressText, progressTextX, progressTextBaseLineY, progressPaint);
        /**
         * 绘制刻度线和刻度
         */
        for (int i = 0; i < 11; i++) {
            //绘制刻度线
            canvas.drawLine(bgRect.width() / 10 * i + cursorBgWidth / 2 + scaleLineWidth / 2,
                    progressTextSize + cursorBgHeight,
                    bgRect.width() / 10 * i + cursorBgWidth / 2 + scaleLineWidth / 2,
                    progressTextSize + cursorBgHeight + scaleLineHeight, scaleLinePaint);
            //绘制刻度
            if (i > 0 && i < 10) {
                String scaleText = String.valueOf(i * 10) + "%";
                Paint.FontMetrics scaleFm = scaleTextPaint.getFontMetrics();
                //获取刻度文字的宽度
                float scaleTextWidth = scaleTextPaint.measureText(scaleText);
                canvas.drawText(scaleText, bgRect.width() / 10 * i + (cursorBgWidth - scaleTextWidth) / 2,
                        getHeight() - (scaleFm.bottom - scaleFm.descent), scaleTextPaint);
            }
        }
    }

    /**
     * 获取dp对应的px值
     *
     * @param value 要转换的值
     * @return dp对应的px值
     */
    private int getDpValue(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, getContext().getResources().getDisplayMetrics());
    }
}
