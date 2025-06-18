package com.pianomusicdrumpad.pianokeyboard.Piano.SuperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Keep;

import com.pianomusicdrumpad.pianokeyboard.Piano.models.C0945e;

import java.util.Vector;



@Keep
public class SQLiteHelper extends SQLiteOpenHelper {


    private static SQLiteHelper f216c;


    private Vector<C0945e> f217a = new Vector<>();


    private C0945e f218b;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    private SQLiteHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }


    public static SQLiteHelper m113a(Context context) {
        if (f216c == null) {
            f216c = new SQLiteHelper(context, "recordingDataBaseName", (SQLiteDatabase.CursorFactory) null, 1);
        }
        return f216c;
    }


    public void mo19507b(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(str, "recordingName=?", new String[]{str2});
        writableDatabase.close();
    }


    public void mo19504a(String str, String str2, String str3) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recordingName", str2);
        contentValues.put("path", str3);
        writableDatabase.insert(str, (String) null, contentValues);
        RecoedSound_c.m88a("Debug-- ", "Data Successfully Added--- Record Name--  " + str2 + "Record Path-- " + str3);
    }


    public C0945e[] mo19506a(String str) {
        int i;
        this.f217a.removeAllElements();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from " + str, (String[]) null);
        rawQuery.moveToLast();
        while (true) {
            if (rawQuery.isBeforeFirst()) {
                break;
            }
            C0945e eVar = new C0945e();
            this.f218b = eVar;
            eVar.mo19511a(rawQuery.getString(0));
            this.f218b.mo19513b(rawQuery.getString(1));
            this.f217a.add(this.f218b);
            rawQuery.moveToPrevious();
        }
        readableDatabase.close();
        int size = this.f217a.size();
        C0945e[] eVarArr = new C0945e[size];
        for (i = 0; i < size; i++) {
            eVarArr[i] = this.f217a.get(i);
        }
        return eVarArr;
    }


    public boolean mo19505a(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from " + str, (String[]) null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            if (rawQuery.getString(0).equals(str2)) {
                readableDatabase.close();
                return false;
            }
            rawQuery.moveToNext();
        }
        readableDatabase.close();
        return true;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table recordingTable(recordingName TEXT,path TEXT)");
        RecoedSound_c.m88a("Debug-- ", "Table Successfully Created");
    }
}
