package probus.makuro.probuszpizza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MalikDB extends SQLiteOpenHelper {

    private static final String NAMA_BD = "malikdb";
    private static final int VERSION = 1;
    public MalikDB(@Nullable Context context) {
        super(context,NAMA_BD,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table orang(id integer primary key autoincrement,nama text,asal text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
