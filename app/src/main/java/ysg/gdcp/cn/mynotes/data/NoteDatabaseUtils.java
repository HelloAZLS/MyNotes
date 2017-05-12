package ysg.gdcp.cn.mynotes.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ysg.gdcp.cn.mynotes.domain.NoteInfo;

/**
 * Created by Administrator on 2017/5/12 15:53.
 *
 * @author ysg
 */

public class NoteDatabaseUtils {

    public static ArrayList getDateFromDatabase(SQLiteDatabase db, ArrayList list) {
        String sql = "select * from " + NotesDatabase.TABLE_NAME;
        NoteInfo info = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            info = new NoteInfo();

            info.setCONTENT(cursor.getString(cursor.getColumnIndex(NotesDatabase.CONTENT)));
            info.setTIME(cursor.getString(cursor.getColumnIndex(NotesDatabase.TIME)));
            info.setImagePath(cursor.getString(cursor.getColumnIndex(NotesDatabase.imagePath)));
            info.setVideoPath(cursor.getString(cursor.getColumnIndex(NotesDatabase.videoPath)));
            list.add(info);

        }
        return list;
    }

}
