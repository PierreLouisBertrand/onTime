package com.example.onTime.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Classe pour la petite vague bleue en haut de chaque fragment
 */
public class WavyLinearLayout extends LinearLayout {

    private Paint paint;
    private Path path;

    public WavyLinearLayout(Context context) {
        super(context);
        this.init();
    }

    public WavyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        this.paint = new Paint();
        paint.setColor(Color.parseColor("#428BCA"));
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        this.path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = this.getWidth();
        float height = this.getHeight();

        this.path.moveTo(0, 0);
        this.path.quadTo(width, 0, width, 0);
        this.path.quadTo(width, 0, width, height - (height / 8));
        this.path.quadTo(3 * (width / 4), height - (height / 4), width / 2, height - (height / 8));
        this.path.quadTo(width / 4, height, 0, height - (height / 8));
        this.path.quadTo(0, height, 0, 0);

        canvas.drawPath(this.path, this.paint);

    }
}
