package com.codans.customseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * 作者：xmm on 2017/6/8 0008 09:39
 * 邮箱：15067596185@163.com
 */
public class CustomProgressBar extends ProgressBar {
    /**
     * 属性
     */
    //文字大小
    private float textSize;
    //文字颜色
    private int textColor;
    //超过进度文字显示的颜色
    private int selectTextColor;
    //三角形的边长
    private float triangleLength;
    //三角形颜色
    private int triangleColor;
    /**
     * 画笔
     */
    //文字画笔
    private Paint textPaint;
    //三角形画笔
    private Paint trianglePaint;

    /**
     * 文字
     *
     * @param context
     */
    private String[] text = {"平台发货", "团长阅读", "传递图书", "队员收书"};

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        textSize = attr.getDimension(R.styleable.CustomProgressBar_textSize, getDpValue(11));
        textColor = attr.getColor(R.styleable.CustomProgressBar_textColor, getResources().getColor(R.color.colorPrimary));
        selectTextColor = attr.getColor(R.styleable.CustomProgressBar_selectTextColor, getResources().getColor(R.color.colorAccent));
        triangleLength = attr.getDimension(R.styleable.CustomProgressBar_triangleLength, getDpValue(8));
        triangleColor = attr.getColor(R.styleable.CustomProgressBar_triangleColor, getResources().getColor(R.color.colorPrimary));
        attr.recycle();
        initPaint();
        //设置预留空间
        setPadding(0, getDpValue(40), 0, getDpValue(20));
    }

    private void initPaint() {
        //初始化文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        //初始化三角形画笔
        trianglePaint = new Paint();
        trianglePaint.setAntiAlias(true);
        trianglePaint.setColor(triangleColor);
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制文字和三角形
        Rect bgRect = getProgressDrawable().getBounds();
        for (int i = 0; i < 4; i++) {
            if (getProgress() >= i + 1) {
                textPaint.setColor(selectTextColor);
            } else {
                textPaint.setColor(textColor);
            }
            float textWidth = textPaint.measureText(text[i]);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            //计算文字基线Y坐标
            float progressTextBaseLineY = textSize / 2 - fm.descent + (fm.bottom - fm.top) / 2 + getDpValue(19);
            canvas.drawText(text[i], bgRect.width() / 4 * i + (bgRect.width() / 4 - textWidth) / 2, progressTextBaseLineY, textPaint);
            //绘制三角形
            Path path = new Path();
            path.moveTo(bgRect.width() / 4 * i + bgRect.width() / 8, getDpValue(40) - (float) Math.sqrt(triangleLength * triangleLength - triangleLength * triangleLength / 4));// 此点为多边形的起点
            path.lineTo(bgRect.width() / 4 * i + bgRect.width() / 8 - triangleLength / 2, getDpValue(40));
            path.lineTo(bgRect.width() / 4 * i + bgRect.width() / 8 + triangleLength / 2, getDpValue(40));
            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, trianglePaint);
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
