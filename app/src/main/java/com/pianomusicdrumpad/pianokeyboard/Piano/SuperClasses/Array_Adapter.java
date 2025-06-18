package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0945e;
import com.pianomusicdrumpad.pianokeyboard.Piano.views.TimeView;
import com.pianomusicdrumpad.pianokeyboard.R;


public abstract class Array_Adapter extends ArrayAdapter {


    private Context f208a;


    public TimeView f209b;


    private SQLiteHelper f210c;


    private C0945e[] f211d;


    private boolean f212e = false;


    private int f213f = -1;


    private boolean f214g;


    private ImageView f215h;


    public abstract void mo19269a();


    public abstract void mo19270a(int i);


    public abstract void mo19271a(String str, ImageView imageView);


    public abstract void mo19272b();

    public Array_Adapter(Context context, int i, TimeView timeView) {
        super(context, i);
        this.f208a = context;
        this.f209b = timeView;
        SQLiteHelper a = SQLiteHelper.m113a(context);
        this.f210c = a;
        this.f211d = a.mo19506a("recordingTable");
    }


    public void mo19497a(int i, ImageView imageView) {
        if (this.f214g) {
            if (i == this.f213f) {
                this.f214g = false;
                mo19269a();
                imageView.setImageResource(R.drawable.recording_btn_bg_play_selector);
                return;
            }
            this.f214g = false;
            mo19269a();
            imageView.setImageResource(R.drawable.recording_btn_bg_play_selector);
        }
        this.f214g = true;
        this.f213f = i;
        this.f215h = imageView;
        mo19271a(this.f211d[i].mo19512b(), imageView);
        imageView.setImageResource(R.drawable.recording_btn_bg_pause_selector);
    }


    public void mo19498c() {
        this.f214g = false;
    }

    public int getCount() {
        return this.f211d.length;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.recording_list_layout, (ViewGroup) null);
        }
        TextView textView = (TextView) view.findViewById(R.id.toneNameTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.recordStopBtnImageView);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.recordDeleteBtnImageView);
        final ImageView imageView3 = (ImageView) view.findViewById(R.id.playPauseBtnImageView);
        imageView3.setImageResource((this.f213f != i || !this.f214g) ? R.drawable.recording_btn_bg_play_selector : R.drawable.recording_btn_bg_pause_selector);
        textView.setText("" + this.f211d[i].mo19510a());
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Array_Adapter.this.mo19272b();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Array_Adapter.this.mo19497a(i, imageView3);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Array_Adapter.this.mo19270a(i);
            }
        });
        return view;
    }
}
