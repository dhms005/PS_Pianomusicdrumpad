package com.pianomusicdrumpad.pianokeyboard.Piano.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;

import java.util.Vector;

import com.pianomusicdrumpad.pianokeyboard.Piano.a1.b;
import com.pianomusicdrumpad.pianokeyboard.R;

public abstract class a extends View {
    private Bitmap a_bit;
    private Bitmap b_bit;
    private float c;
    private float d = -1.0f;
    private float e = 0.0f;
    private float f;
    private int g;
    private int h;
    private int i = 0;
    private int j = 0;
    private int k = 0;
    private Vector<Integer> l = new Vector<>();
    private String[] m;
    private Vector<String> n = new Vector<>();
    private Vector<String> o = new Vector<>();
    private Paint p = new Paint();

    public abstract void a(int i2);

    public abstract void b(int i2);

    public abstract void c(int i2);

    public abstract void d(int i2);

    public a(Context context) {
        super(context);
    }

    private void a() {
        this.l.removeAllElements();
        this.l.add(1);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(3);
        this.l.add(1);
        this.l.add(2);
        this.l.add(2);
        this.l.add(3);
        this.l.add(4);
    }

    private int getBlackPressedKey() {
        this.j = 0;
        this.i = 0;
        int i2 = (int) (this.d + this.c + 1.0f);
        int i3 = 0;
        while (i2 <= 0) {
            if (this.l.get(i3).intValue() != 3) {
                this.i++;
            }
            i3++;
            i2 = (int) (((float) i2) + this.c);
        }
        float f2 = this.e;
        for (int i4 = 0; i4 < this.l.size(); i4++) {
            if (f2 > (-this.f) && f2 < ((float) getWidth()) && this.l.get(i4).intValue() != 3) {
                this.j++;
                int i5 = this.g;
                if (((float) i5) > f2 && ((float) i5) < this.f + f2) {
                    int i6 = this.h;
                    double height = (double) getHeight();
                    Double.isNaN(height);
                    Double.isNaN(height);
                    if (i6 < ((int) (height * 0.53d))) {
                        return this.j + this.i;
                    }
                }
            }
            f2 += this.c;
        }
        return -1;
    }

    private int getWhitePressedKey() {
        this.j = 0;
        float f2 = this.d;
        float f3 = this.c;
        int abs = (int) (Math.abs((f2 - f3) / f3) + 0.3f);
        this.k = abs;
        int i2 = (int) (((float) this.g) / this.c);
        this.j = i2;
        return i2 + abs;
    }

    public void e(int i2) {
        float f2 = this.c;
        float f3 = ((float) i2) * f2;
        this.d = -f3;
        double d2 = (double) f2;
        Double.isNaN(d2);
        Double.isNaN(d2);
        this.e = -(f3 - ((float) (d2 * 0.75d)));
        invalidate();
    }


    public void onDraw(Canvas canvas) {
        RectF rectF = null;
        Bitmap bitmap = null;
        float f2 = this.d;
        for (int i2 = 0; i2 < this.l.size(); i2++) {
            if (f2 > (-this.c) && f2 < ((float) getWidth())) {
                Vector<String> vector = this.n;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                int i3 = i2 + 1;
                sb.append(i3);
                if (vector.contains(sb.toString())) {
                    Bitmap c2 = b.c(this.l.get(i3 - 1).intValue(), getContext());
                    this.a_bit = c2;
                    if (c2 != null) {
                        canvas.drawBitmap(c2, (Rect) null, new RectF(f2, 0.0f, this.c + f2, (float) getHeight()), (Paint) null);
                    }
                } else {
                    Bitmap b2 = b.b(this.l.get(i2).intValue(), getContext());
                    this.a_bit = b2;
                    if (b2 != null) {
                        canvas.drawBitmap(b2, (Rect) null, new RectF(f2, 0.0f, this.c + f2, (float) getHeight()), (Paint) null);
                        String[] strArr = this.m;
                        canvas.drawText(strArr[i2], ((this.c / 2.0f) + f2) - (this.p.measureText(strArr[i2]) / 2.0f), (((float) getHeight()) - (this.p.descent() / 2.0f)) - this.p.getTextSize(), this.p);
                    }
                }
            }
            f2 += this.c;
        }
        float f3 = this.e;
        int i4 = 0;
        for (int i5 = 0; i5 < this.l.size() - 1; i5++) {
            if (f3 > (-this.f) && f3 < ((float) getWidth()) && this.l.get(i5).intValue() != 3) {
                i4++;
                if (this.o.contains("" + (this.i + i4))) {
                    bitmap = b.a(R.drawable.black_pressed, getContext());
                    this.b_bit = bitmap;
                    if (bitmap != null) {
                        double height = (double) getHeight();
                        Double.isNaN(height);
                        Double.isNaN(height);
                        rectF = new RectF(f3, 0.0f, this.f + f3, (float) ((int) (height * 0.53d)));
                        canvas.drawBitmap(bitmap, (Rect) null, rectF, (Paint) null);
                    }
                } else {
                    bitmap = b.a(R.drawable.black_key, getContext());
                    this.b_bit = bitmap;
                    if (bitmap != null) {
                        double height2 = (double) getHeight();
                        Double.isNaN(height2);
                        Double.isNaN(height2);
                        rectF = new RectF(f3, 0.0f, this.f + f3, (float) ((int) (height2 * 0.53d)));
                        canvas.drawBitmap(bitmap, (Rect) null, rectF, (Paint) null);
                    }
                }
              //  bitmap = null;
              //  rectF = null;
                canvas.drawBitmap(bitmap, (Rect) null, rectF, (Paint) null);
            }
            this.l.get(i5).intValue();
            f3 += this.c;
        }
    }


    public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        super.onLayout(z, i2, i3, i4, i5);
        a();
        float width = ((float) getWidth()) / 10.0f;
        this.c = width;
        double d2 = (double) width;
        Double.isNaN(d2);
        Double.isNaN(d2);
        this.f = ((float) (d2 * 0.25d)) * 2.0f;
        float f2 = this.c;
        float f3 = 26.0f * f2;
        this.d = -f3;
        double d3 = (double) f2;
        Double.isNaN(d3);
        Double.isNaN(d3);
        this.e = -(f3 - ((float) (d3 * 0.75d)));
        this.p.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.p.setTextSize((float) ((int) (this.c * 0.3f)));
        this.p.setAntiAlias(true);
    }


    public void onMeasure(int i2, int i3) {
        setMeasuredDimension(MeasureSpec.getSize(i2), MeasureSpec.getSize(i3));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5 || motionEvent.getActionMasked() == 2) {
            int pointerCount = motionEvent.getPointerCount();
            if (motionEvent.getActionMasked() == 0) {
                this.n.removeAllElements();
                this.o.removeAllElements();
            }
            motionEvent.getActionIndex();
            for (int i2 = 0; i2 < pointerCount; i2++) {
                this.g = (int) motionEvent.getX(i2);
                this.h = (int) motionEvent.getY(i2);
                int blackPressedKey = getBlackPressedKey();
                int whitePressedKey = getWhitePressedKey();
                if (blackPressedKey != -1) {
                    vector2.add(blackPressedKey + "");
                    Vector<String> vector3 = this.o;
                    if (!vector3.contains(blackPressedKey + "")) {
                        b(blackPressedKey);
                    }
                } else if (whitePressedKey != -1) {
                    vector.add(whitePressedKey + "");
                    Vector<String> vector4 = this.n;
                    if (!vector4.contains(whitePressedKey + "")) {
                        a(whitePressedKey);
                    }
                }
            }
            for (int i3 = 0; i3 < this.n.size(); i3++) {
                if (!vector.contains(this.n.get(i3))) {
                    d(Integer.parseInt(this.n.get(i3)));
                }
            }
            for (int i4 = 0; i4 < this.o.size(); i4++) {
                if (!vector2.contains(this.o.get(i4))) {
                    c(Integer.parseInt(this.o.get(i4)));
                }
            }
            this.n.removeAllElements();
            this.o.removeAllElements();
            for (int i5 = 0; i5 < vector.size(); i5++) {
                this.n.add(String.valueOf(vector.get(i5)));
            }
            for (int i6 = 0; i6 < vector2.size(); i6++) {
                this.o.add(String.valueOf(vector2.get(i6)));
            }
            vector.removeAllElements();
            vector2.removeAllElements();
            invalidate();
        }
        if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
            int pointerCount2 = motionEvent.getPointerCount();
            int actionIndex = motionEvent.getActionIndex();
            this.g = (int) motionEvent.getX(actionIndex);
            this.h = (int) motionEvent.getY(actionIndex);
            int blackPressedKey2 = getBlackPressedKey();
            int whitePressedKey2 = getWhitePressedKey();
            if (blackPressedKey2 != -1) {
                c(blackPressedKey2);
                Vector<String> vector5 = this.o;
                vector5.remove(blackPressedKey2 + "");
            } else if (whitePressedKey2 != -1) {
                d(whitePressedKey2);
                Vector<String> vector6 = this.n;
                vector6.remove(whitePressedKey2 + "");
            }
            if (pointerCount2 == 1) {
                this.n.removeAllElements();
                this.o.removeAllElements();
            }
            invalidate();
        }
        motionEvent.getAction();
        return true;
    }

    public void setWhiteKeyNotesText(String[] strArr) {
        this.m = strArr;
    }
}
