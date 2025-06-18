package com.pianomusicdrumpad.pianokeyboard.Piano.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses.DensityDpi;

import java.util.Timer;
import java.util.TimerTask;

public class TimeView extends View {


    private Paint f267a = new Paint();

    public int f268b = 0;
    public int f269c = 0;
    public long f270d = 0;
    public long f271e = 0;
    private Timer f272f = new Timer();


    private TimerTask f273g;


    private int f274h = 0;


    private int f275i = 20;



    public long f276j;


    private boolean f277k;

    public TimeView(Context context) {
        super(context);
        this.f267a.setColor(-1);
        this.f267a.setTextSize(DensityDpi.m82a((float) this.f275i, getContext()));
    }

    public TimeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f267a.setColor(-1);
        this.f267a.setTextSize(DensityDpi.m82a((float) this.f275i, getContext()));
    }

    public TimeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f267a.setColor(-1);
        this.f267a.setTextSize(DensityDpi.m82a((float) this.f275i, getContext()));
    }


    public void mo19614a() {
        this.f268b = 0;
        this.f269c = 0;
        this.f270d = 0;
        this.f276j = 0;
        this.f271e = 0;
        invalidate();
    }


    public void mo19615b() {
        this.f277k = true;
        if (this.f273g == null) {
            TimerTask r2 = new TimerTask() {
                public void run() {
                    TimeView timeView = TimeView.this;
                    long unused = timeView.f270d = timeView.f276j + (System.currentTimeMillis() - TimeView.this.f271e);
                    TimeView timeView2 = TimeView.this;
                    int unused2 = timeView2.f268b = (int) (timeView2.f270d / 1000);
                    TimeView timeView3 = TimeView.this;
                    int unused3 = timeView3.f269c = timeView3.f268b / 60;
                    TimeView timeView4 = TimeView.this;
                    int unused4 = timeView4.f268b = timeView4.f268b % 60;
                    TimeView.this.postInvalidate();
                }
            };
            this.f273g = r2;
            this.f272f.schedule(r2, 10, 500);
            this.f271e = System.currentTimeMillis();
        }
    }


    public void mo19616c() {
        this.f277k = false;
        TimerTask timerTask = this.f273g;
        if (timerTask != null) {
            timerTask.cancel();
            this.f273g = null;
            long currentTimeMillis = System.currentTimeMillis() - this.f271e;
            this.f270d = currentTimeMillis;
            this.f276j += currentTimeMillis;
        }
    }


    public boolean mo19617d() {
        return this.f277k;
    }

    public long getMillisSeconds() {
        long currentTimeMillis = this.f276j + (System.currentTimeMillis() - this.f271e);
        this.f270d = currentTimeMillis;
        return currentTimeMillis;
    }

    public int getMin() {
        return this.f269c;
    }

    public int getSeconds() {
        return this.f268b;
    }

    public String getTime() {
        StringBuilder sb;
        StringBuilder sb2;
        if (this.f268b > 9) {
            sb = new StringBuilder();
            sb.append(this.f268b);
            sb.append("");
        } else {
            sb = new StringBuilder();
            sb.append("0");
            sb.append(this.f268b);
        }
        String sb3 = sb.toString();
        if (this.f269c > 9) {
            sb2 = new StringBuilder();
            sb2.append(this.f269c);
            sb2.append("");
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("0");
            sb4.append(this.f269c);
            sb2 = sb4;
        }
        String sb5 = sb2.toString();
        return sb5 + ":" + sb3;
    }


    public void onDraw(Canvas canvas) {
        int textSize = (int) (this.f267a.getTextSize() - (this.f267a.descent() / 2.0f));
        this.f274h = (int) (this.f267a.measureText(getTime()) - (this.f267a.ascent() / 2.0f));
        while (this.f274h > getWidth()) {
            int i = this.f275i - 1;
            this.f275i = i;
            this.f267a.setTextSize(DensityDpi.m82a((float) i, getContext()));
            this.f274h = (int) (this.f267a.measureText(getTime()) - (this.f267a.ascent() / 2.0f));
            textSize = (int) (this.f267a.getTextSize() - (this.f267a.descent() / 2.0f));
        }
        canvas.drawText(getTime(), (float) ((getWidth() >> 1) - (this.f274h >> 1)), (float) ((getHeight() >> 1) + (textSize >> 1)), this.f267a);
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.f267a.setTextSize(DensityDpi.m82a(20.0f, getContext()));
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void setMin(int i) {
        this.f269c = i;
    }

    public void setSeconds(int i) {
        this.f268b = i;
    }

    public void setTimerStart(boolean z) {
        this.f277k = z;
    }
}
