package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.SoundPool;

import androidx.core.view.PointerIconCompat;

import java.util.Timer;
import java.util.TimerTask;

import com.pianomusicdrumpad.pianokeyboard.R;

/* renamed from: com.pianomusicdrumpad.pianokeyboard.b */
public class C0934b {

    
    private static C0934b f190q;
    private static int f191r;
    public SoundPool soundPool;
    int[] f193b;
    int[] f194c;
    int[] f195d;
    int[] f196e;
    int[] f197f;
    int[] f198g;
    float f199h;
    private int[] f200i = new int[0];
    private int[] SoundArray = {R.raw.aa_1, R.raw.ae0, R.raw.aa0, R.raw.ae1, R.raw.aa1, R.raw.ae2, R.raw.aa2, R.raw.ae3, R.raw.aa3, R.raw.ae4, R.raw.aa4, R.raw.ae5, R.raw.aa5, R.raw.ae6, R.raw.aa6};
    private int[] f202k;
    private int[] f203l;
    private int[] f204m;
    private int[] f205n;
    private int[] f206o;
    private int[] f207p;


    private int m94f(int i) {
        if (i <= 4) {
            return 0;
        }
        if (i <= 7) {
            return 1;
        }
        if (i <= 11) {
            return 2;
        }
        if (i <= 14) {
            return 3;
        }
        if (i <= 18) {
            return 4;
        }
        if (i <= 21) {
            return 5;
        }
        if (i <= 25) {
            return 6;
        }
        if (i <= 28) {
            return 7;
        }
        if (i <= 32) {
            return 8;
        }
        if (i <= 35) {
            return 9;
        }
        if (i <= 39) {
            return 10;
        }
        if (i <= 42) {
            return 11;
        }
        if (i <= 46) {
            return 12;
        }
        return i <= 49 ? 13 : 14;
    }


    private int m95g(int i) {
        if (i <= 3) {
            return 0;
        }
        if (i <= 5) {
            return 1;
        }
        if (i <= 8) {
            return 2;
        }
        if (i <= 10) {
            return 3;
        }
        if (i <= 13) {
            return 4;
        }
        if (i <= 15) {
            return 5;
        }
        if (i <= 18) {
            return 6;
        }
        if (i <= 20) {
            return 7;
        }
        if (i <= 23) {
            return 8;
        }
        if (i <= 25) {
            return 9;
        }
        if (i <= 28) {
            return 10;
        }
        if (i <= 30) {
            return 11;
        }
        if (i <= 33) {
            return 12;
        }
        return i <= 35 ? 13 : 14;
    }


    private int m96h(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 4;
            case 3:
                return 6;
            case 4:
                return 2;
            case 5:
                return 4;
            case 6:
                return 1;
            case 7:
                return 4;
            case 8:
                return 6;
            case 9:
                return 2;
            case 10:
                return 4;
            case 11:
                return 1;
            case 12:
                return 4;
            case 13:
                return 6;
            case 14:
                return 2;
            case 15:
                return 4;
            case 16:
                return 1;
            case 17:
                return 4;
            case 18:
                return 6;
            case 19:
                return 2;
            case 20:
                return 4;
            case 21:
                return 1;
            case 22:
                return 4;
            case 23:
                return 6;
            case 24:
                return 2;
            case 25:
                return 4;
            case 26:
                return 1;
            case 27:
                return 4;
            case 28:
                return 6;
            case 29:
                return 2;
            case 30:
                return 4;
            case 31:
                return 1;
            case 32:
                return 4;
            case 33:
                return 6;
            case 34:
                return 2;
            case 35:
                return 4;
            case 36:
                return 1;
            default:
                return 0;
        }
    }


    private int m97i(int i) {
        switch (i) {
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 6:
                return 1;
            case 7:
                return 3;
            case 9:
                return 2;
            case 10:
                return 3;
            case 11:
                return 5;
            case 13:
                return 1;
            case 14:
                return 3;
            case 16:
                return 2;
            case 17:
                return 3;
            case 18:
                return 5;
            case 20:
                return 1;
            case 21:
                return 3;
            case 23:
                return 2;
            case 24:
                return 3;
            case 25:
                return 5;
            case 27:
                return 1;
            case 28:
                return 3;
            case 30:
                return 2;
            case 31:
                return 3;
            case 32:
                return 5;
            case 34:
                return 1;
            case 35:
                return 3;
            case 37:
                return 2;
            case 38:
                return 3;
            case 39:
                return 5;
            case 41:
                return 1;
            case 42:
                return 3;
            case 44:
                return 2;
            case 45:
                return 3;
            case 46:
                return 5;
            case 48:
                return 1;
            case 49:
                return 3;
            case 51:
                return 2;
            case 52:
                return 3;
            default:
                return 0;
        }
    }

    public C0934b() {
        int[] iArr = new int[0];
        this.f202k = iArr;
        int[] iArr2 = {R.raw.ba_1, R.raw.be0, R.raw.ba0, R.raw.be1, R.raw.ba1, R.raw.be2, R.raw.ba2, R.raw.be3, R.raw.ba3, R.raw.be4, R.raw.ba4, R.raw.be5, R.raw.ba5, R.raw.be6, R.raw.ba6};
        this.f203l = iArr2;
        this.f204m = new int[0];
        this.f205n = new int[]{R.raw.sa_1, R.raw.se0, R.raw.sa0, R.raw.se1, R.raw.sa1, R.raw.se2, R.raw.sa2, R.raw.se3, R.raw.sa3, R.raw.se4, R.raw.sa4, R.raw.se5, R.raw.sa5, R.raw.se6, R.raw.sa6};
        this.f206o = new int[0];
        this.f207p = new int[]{R.raw.gn_a_0, R.raw.gn_e_1, R.raw.gn_a_1, R.raw.gn_e_2, R.raw.gn_a_2, R.raw.gn_e_3, R.raw.gn_a_3, R.raw.gn_e_4, R.raw.gn_a_4, R.raw.gn_e_5, R.raw.gn_a_5, R.raw.gn_e_6, R.raw.gn_a_6, R.raw.gn_e_7, R.raw.gn_a_7};
        this.f193b = new int[iArr.length];
        this.f194c = new int[iArr2.length];
        this.f195d = new int[88];
        this.f196e = new int[88];
    }


    public static C0934b m93a() {
        if (f190q == null) {
            f190q = new C0934b();
        }
        return f190q;
    }


    public void mo19483a(int i) {
        int i2 = i - 1;
        int[] iArr = this.f195d;
        if (i2 < iArr.length) {
            final int i3 = iArr[i2];
            new Timer().schedule(new TimerTask() {
                public void run() {
                    if (C0934b.this.soundPool != null) {
                        C0934b.this.soundPool.pause(i3);
                    }
                }
            }, 400);
        }
    }


    public void mo19484a(int i, float f) {
        int i2 = i - 1;
        int[] iArr = this.f195d;
        if (i2 < iArr.length) {
            iArr[i2] = this.soundPool.play(this.f194c[m95g(i)], f, f, 1, 0, (float) Math.pow(Math.pow(2.0d, 0.08333333333333333d), (double) ((float) m96h(i))));
        }
    }


    public void mo19485a(Activity activity, final ProgressDialog progressDialog) {
        if (this.soundPool != null) {
            progressDialog.dismiss();
            return;
        }
        int i = 0;
        this.soundPool = new SoundPool(12, 3, 0);
        this.f199h = 2.0f;
        int i2 = 0;
        while (true) {
            int[] iArr = this.f197f;
            if (i2 >= iArr.length) {
                break;
            }
            this.f193b[i2] = this.soundPool.load(activity, iArr[i2], 1);
            progressDialog.setProgress((int) this.f199h);
            final int i3 = (int) this.f199h;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog.setMessage(i3 + "%");
                }
            });
            this.f199h += 1.1f;
            i2++;
        }
        this.f199h += 2.0f;
        while (true) {
            int[] iArr2 = this.f198g;
            if (i < iArr2.length) {
                this.f194c[i] = this.soundPool.load(activity, iArr2[i], 1);
                final int i4 = (int) this.f199h;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.setMessage(i4 + "%");
                    }
                });
                i++;
            } else {
                this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                        C0934b.this.f199h += 6.0f;
                        progressDialog.setProgress((int) C0934b.this.f199h);
                        progressDialog.setMessage(C0934b.this.f199h + "%");
                        if (i == C0934b.this.f194c[C0934b.this.f194c.length - 5]) {
                            progressDialog.dismiss();
                        }
                    }
                });
                return;
            }
        }
    }


    public void mo19486b() {
        SoundPool soundPool = this.soundPool;
        if (soundPool != null) {
            soundPool.release();
            this.soundPool = null;
        }
    }


    public void mo19487b(int i) {
        int i2 = i - 1;
        int[] iArr = this.f196e;
        if (i2 < iArr.length) {
            final int i3 = iArr[i2];
            new Timer().schedule(new TimerTask() {
                public void run() {
                    if (C0934b.this.soundPool != null) {
                        C0934b.this.soundPool.pause(i3);
                    }
                }
            }, 400);
        }
    }


    public void mo19488b(int i, float f) {
        int i2 = i - 1;
        int[] iArr = this.f196e;
        if (i2 < iArr.length) {
            iArr[i2] = this.soundPool.play(this.f194c[m94f(i)], f, f, 1, 0, (float) Math.pow(Math.pow(2.0d, 0.08333333333333333d), (double) ((float) m97i(i))));
        }
    }


    public void mo19489c(int i) {
        int i2 = i - 1;
        int[] iArr = this.f195d;
        if (i2 < iArr.length) {
            this.soundPool.pause(iArr[i2]);
        }
    }


    public void mo19490d(int i) {
        int i2 = i - 1;
        int[] iArr = this.f196e;
        if (i2 < iArr.length) {
            this.soundPool.pause(iArr[i2]);
        }
    }


    public void mo19491e(int i) {
        if (f191r != i || this.soundPool == null) {
            f191r = i;
            switch (i) {
                case 1002:
                    this.f197f = this.f200i;
                    this.f198g = this.SoundArray;
                    break;
                case PointerIconCompat.TYPE_HELP:
                    this.f197f = this.f202k;
                    this.f198g = this.f203l;
                    break;
                case PointerIconCompat.TYPE_WAIT:
                    this.f197f = this.f204m;
                    this.f198g = this.f205n;
                    break;
                case 1005:
                    this.f197f = this.f206o;
                    this.f198g = this.f207p;
                    break;
            }
            SoundPool soundPool = this.soundPool;
            if (soundPool != null) {
                soundPool.release();
                this.soundPool = null;
            }
        }
    }
}
