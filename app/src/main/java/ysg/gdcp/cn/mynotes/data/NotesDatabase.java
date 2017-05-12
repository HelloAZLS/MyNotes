package ysg.gdcp.cn.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/11 19:16.
 *
 * @author ysg
 */

public class NotesDatabase extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "myNotes";
    public static final String CONTENT = "content";
    public static final String imagePath = "path";
    public static final String videoPath = "video";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public NotesDatabase(Context context) {
        super(context, "myNotes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT NOT NULL," + imagePath + " ," + videoPath
                + " ," + TIME + " TEXT NOT NULL)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
