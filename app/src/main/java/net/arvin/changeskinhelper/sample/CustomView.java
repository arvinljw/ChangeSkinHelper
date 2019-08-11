package net.arvin.changeskinhelper.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import net.arvin.changeskinhelper.ChangeSkinHelper;
import net.arvin.changeskinhelper.core.ChangeCustomSkinListener;

/**
 * Created by arvinljw on 2019-08-10 17:40
 * Function：
 * Desc：
 */
public class CustomView extends View implements ChangeCustomSkinListener {
    private int circleColor;
    private Paint paint;
    private int radius;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        circleColor = R.color.colorPrimary;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ChangeSkinHelper.getColor(circleColor));
        radius = 100;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getSize(radius * 2, widthMeasureSpec), getSize(radius * 2, heightMeasureSpec));
    }

    public int getSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = size;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 100, paint);
    }

    @Override
    public void setCustomTag() {
        ChangeSkinHelper.setCustomResId(this, circleColor);
//        ChangeSkinHelper.setCustomResIds(this, circleColor);
    }

    @Override
    public void changeCustomSkin() {
        int skinCustomResId = ChangeSkinHelper.getSkinCustomResId(this);
//        int skinCustomResId = ChangeSkinHelper.getSkinCustomResIds(this).get(0);
        paint.setColor(ChangeSkinHelper.getColor(skinCustomResId));
        invalidate();
    }
}
