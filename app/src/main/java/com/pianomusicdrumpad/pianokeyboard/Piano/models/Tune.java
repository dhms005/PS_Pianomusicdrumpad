package com.pianomusicdrumpad.pianokeyboard.Piano.models;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Tune {
    private List<Note> notes;
    private String title;

    public Tune(String str, List<Note> list) {
        setTitle(str);
        this.notes = list;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public String getTitle() {
        return this.title;
    }

    public void setNotes(List<Note> list) {
        this.notes = list;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
