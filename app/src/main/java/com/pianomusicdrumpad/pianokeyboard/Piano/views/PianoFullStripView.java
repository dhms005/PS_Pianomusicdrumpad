package com.pianomusicdrumpad.pianokeyboard.Piano.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;

import java.util.Vector;

import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.BitmapConvert;
import com.pianomusicdrumpad.pianokeyboard.R;

public class PianoFullStripView extends View {


    Matrix f240a = new Matrix();


    private float f241b;


    private float f242c;


    private float f243d;


    private float f244e = 0.0f;


    private float f245f = 0.0f;


    private float f246g;


    private a f247h;


    private Paint f248i = new Paint();


    private Bitmap f249j;


    private Bitmap f250k;


    private Bitmap f251l;


    private Vector<Integer> f252m = new Vector<>();


    private boolean f253n = true;

    public PianoFullStripView(Context context) {
        super(context);
    }

    public PianoFullStripView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PianoFullStripView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    private void m128a() {
        this.f252m.removeAllElements();
        this.f252m.add(1);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(1);
        this.f252m.add(2);
        this.f252m.add(2);
        this.f252m.add(3);
        this.f252m.add(4);
    }


    public void onDraw(Canvas canvas) {
        Bitmap bitmap;
        canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        float f = 0.0f;
        for (int i = 0; i < this.f252m.size(); i++) {
            Bitmap b = BitmapConvert.m84b(this.f252m.get(i).intValue(), getContext());
            this.f249j = b;
            if (b != null) {
                canvas.drawBitmap(b, (Rect) null, new RectF(f, 0.0f, this.f243d + f, (float) getHeight()), (Paint) null);
            }
            f += this.f243d;
        }
        float f2 = this.f245f;
        for (int i2 = 0; i2 < this.f252m.size() - 1; i2++) {
            if (!(this.f252m.get(i2).intValue() == 3 || (bitmap = this.f250k) == null)) {
                double height = (double) getHeight();
                Double.isNaN(height);
                Double.isNaN(height);
                canvas.drawBitmap(bitmap, (Rect) null, new RectF(f2, 0.0f, this.f246g + f2, (float) (height * 0.53d)), (Paint) null);
            }
            f2 += this.f243d;
        }
        float f3 = 0.0f;
        for (int i3 = 0; i3 < this.f252m.size(); i3++) {
            float f4 = this.f244e;
            if ((f3 < f4 || f3 >= f4 + (this.f243d * 10.0f)) && this.f251l != null) {
                this.f248i.setAlpha(150);
                canvas.drawBitmap(this.f251l, (Rect) null, new RectF(f3, 0.0f, this.f243d + f3, (float) getHeight()), this.f248i);
            }
            f3 += this.f243d;
        }
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        m128a();
        float width = ((float) getWidth()) / 52.0f;
        this.f243d = width;
        float f = 30.0f * width;
        this.f244e = f;
        this.f244e = f - (5.0f * width);
        double d = (double) width;
        Double.isNaN(d);
        Double.isNaN(d);
        this.f245f = (float) (d * 0.75d);
        double d2 = (double) this.f243d;
        Double.isNaN(d2);
        Double.isNaN(d2);
        this.f246g = ((float) (d2 * 0.25d)) * 2.0f;
        this.f251l = BitmapConvert.m83a(R.drawable.scroll_bg, getContext());
        this.f250k = BitmapConvert.m83a(R.drawable.black_key, getContext());
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        a aVar;
        a aVar2;
        int i2;
        if (motionEvent.getAction() == 0) {
            float x = (float) ((int) motionEvent.getX());
            this.f242c = x;
            if (x > ((float) ((int) (this.f243d * 5.0f)))) {
                int width = getWidth();
                float f = this.f243d;
                if (x < ((float) (width - ((int) (f * 5.0f))))) {
                    float f2 = this.f242c - (f * 5.0f);
                    this.f244e = f2;
                    aVar2 = this.f247h;
                    i2 = ((int) (f2 / f)) + 1;
                    aVar2.e(i2);
                }
            }
            if (this.f242c > ((float) (getWidth() - ((int) (this.f243d * 5.0f))))) {
                this.f244e = (float) (getWidth() - ((int) (this.f243d * 10.0f)));
            } else {
                this.f244e = x;
            }
            i2 = (int) (this.f244e / this.f243d);
            aVar2 = this.f247h;
            aVar2.e(i2);
        }
        if (motionEvent.getAction() == 2) {
            float x2 = motionEvent.getX();
            this.f241b = x2;
            this.f242c = x2;
            if (x2 > ((float) ((int) (this.f243d * 5.0f)))) {
                int width2 = getWidth();
                float f3 = this.f243d;
                if (x2 < ((float) (width2 - ((int) (f3 * 5.0f))))) {
                    float f4 = this.f242c - (5.0f * f3);
                    this.f244e = f4;
                    aVar = this.f247h;
                    i = ((int) (f4 / f3)) + 1;
                    aVar.e(i);
                }
            }
            if (this.f242c > ((float) (getWidth() - ((int) (this.f243d * 5.0f))))) {
                this.f244e = (float) (getWidth() - ((int) (this.f243d * 10.0f)));
            } else {
                this.f244e = x2;
            }
            i = (int) (this.f244e / this.f243d);
            aVar = this.f247h;
            aVar.e(i);
        }
        invalidate();
        return true;
    }

    public void setPianoView(a aVar) {
        this.f247h = aVar;
    }
}
