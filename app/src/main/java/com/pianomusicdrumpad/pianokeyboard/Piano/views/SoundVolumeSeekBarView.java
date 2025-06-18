package com.pianomusicdrumpad.pianokeyboard.Piano.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.BitmapConvert;
import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.SharePreference;
import com.pianomusicdrumpad.pianokeyboard.R;

public class SoundVolumeSeekBarView extends View {


    Paint f254a = new Paint();


    private Rect f255b;


    private Rect f256c;


    private Rect f257d;


    private Bitmap f258e;


    private Bitmap f259f;


    private Bitmap f260g;


    private Bitmap f261h;


    private C0949a f262i;


    private int f263j = 0;


    private int f264k = 0;


    private int f265l = 100;


    private int f266m = -1;

    /* renamed from: com.pianomusicdrumpad.pianokeyboard.views.SoundVolumeSeekBarView$a */
    public interface C0949a {

        void mo19220a(int i);
    }

    public SoundVolumeSeekBarView(Context context) {
        super(context);
    }

    public SoundVolumeSeekBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SoundVolumeSeekBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    private void m129a() {
        Rect rect = null;
        double width = (double) getWidth();
        Double.isNaN(width);
        Double.isNaN(width);
        if (((double) this.f263j) > width * 0.98d) {
            int width2 = getWidth();
            int height = getHeight();
        } else {
            rect = new Rect(0, 0, this.f263j, getHeight());
        }
        this.f255b = rect;
        this.f256c = new Rect(this.f263j - ((int) ((((float) getHeight()) * 0.78f) / 2.0f)), 0, (int) (((float) this.f263j) + ((((float) getHeight()) * 0.78f) / 2.0f)), getHeight());
        invalidate();
    }


    public void onDraw(Canvas canvas) {
        this.f254a.setAntiAlias(true);
        this.f254a.setDither(true);
        Bitmap bitmap = this.f260g;
        if (!(bitmap == null || this.f258e == null)) {
            canvas.drawBitmap(bitmap, (Rect) null, this.f257d, this.f254a);
            Bitmap bitmap2 = this.f258e;
            Rect rect = this.f255b;
            canvas.drawBitmap(bitmap2, rect, rect, this.f254a);
        }
        canvas.drawBitmap(this.f259f, (Rect) null, this.f256c, (Paint) null);
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (getWidth() > 0 && getHeight() > 0) {
            this.f258e = BitmapConvert.m83a(R.drawable.slider_filled, getContext());
            this.f260g = BitmapConvert.m83a(R.drawable.slider_base, getContext());
            this.f261h = BitmapConvert.m83a(R.drawable.slider_button, getContext());
            this.f259f = BitmapConvert.m83a(R.drawable.slider_button, getContext());
            this.f258e = Bitmap.createScaledBitmap(this.f258e, getWidth(), getHeight(), true);
            setProgress(((Integer) SharePreference.m89a(getContext(), "soundValumeKey")).intValue());
            this.f257d = new Rect(0, 0, getWidth(), getHeight());
            m129a();
        }
    }


    public void onMeasure(int i, int i2) {
        setMeasuredDimension(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.f263j = (int) motionEvent.getX();
        }
        if (motionEvent.getAction() == 2) {
            this.f263j = (int) motionEvent.getX();
        }
        if (this.f263j >= getWidth() - ((int) (((float) getHeight()) * 0.23f)) || this.f263j <= ((int) (((float) getHeight()) * 0.23f))) {
            return true;
        }
        int width = (this.f265l * this.f263j) / getWidth();
        this.f264k = width;
        C0949a aVar = this.f262i;
        if (aVar != null) {
            aVar.mo19220a(width);
        }
        m129a();
        return true;
    }

    public void setProgress(int i) {
        this.f266m = i;
        this.f263j = (getWidth() * i) / this.f265l;
        invalidate();
    }

    public void setVolumeProgess(C0949a aVar) {
        this.f262i = aVar;
    }
}
