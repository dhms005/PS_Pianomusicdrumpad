package com.pianomusicdrumpad.pianokeyboard.Piano.models;

import androidx.annotation.Keep;

import java.io.Serializable;


@Keep
public class C0946f implements Serializable {


    private int f221a;
    private String f222b;
    private long f223c;
    private int f224d;
    private boolean f225e;

    public C0946f(int i, String str, long j, int i2, boolean z) {
        this.f221a = i;
        this.f222b = str;
        this.f223c = j;
        this.f224d = i2;
        mo19515a(z);
    }


    public int mo19514a() {
        return this.f221a;
    }


    public void mo19515a(boolean z) {
        this.f225e = z;
    }


    public String mo19516b() {
        return this.f222b;
    }


    public long mo19517c() {
        return this.f223c;
    }


    public int mo19518d() {
        return this.f224d;
    }


    public boolean mo19519e() {
        return this.f225e;
    }
}
