package ysg.gdcp.cn.mynotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ListView mLvNote;
    private Intent mIntent;
    private BaseAdapter myAdapter;
    private NotesDatabase mNoteDB;
    private SQLiteDatabase mDbReader;
    private  Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mNoteDB =new NotesDatabase(this);
        mDbReader =mNoteDB.getReadableDatabase();

    }

    private void initDatas() {

        cursor = mDbReader.query(NotesDatabase.TABLE_NAME,null,null,null,null,null,null);
        myAdapter =new MyAdapter(this,cursor);
        mLvNote.setAdapter(myAdapter);
    }

    private void initViews() {

        findViewById(R.id.btn_text).setOnClickListener(this);
        findViewById(R.id.btn_img).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);
        mLvNote = (ListView) findViewById(R.id.lv_data);
       mLvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               cursor.moveToPosition(position);
               Intent detaliIntent =new Intent(MainActivity.this,DetailActivity.class);
               detaliIntent.putExtra(NotesDatabase.ID,cursor.getInt(cursor.getColumnIndex(NotesDatabase.ID)));
               detaliIntent.putExtra(NotesDatabase.CONTENT,cursor.getString(cursor.getColumnIndex(NotesDatabase.CONTENT)));
               detaliIntent.putExtra(NotesDatabase.TIME,cursor.getString(cursor.getColumnIndex(NotesDatabase.TIME)));
               detaliIntent.putExtra(NotesDatabase.imagePath,cursor.getString(cursor.getColumnIndex(NotesDatabase.imagePath)));
               detaliIntent.putExtra(NotesDatabase.videoPath,cursor.getString(cursor.getColumnIndex(NotesDatabase.videoPath)));
               startActivity(detaliIntent);
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    @Override
    public void onClick(View v) {
        mIntent= new Intent(this,AddActivity.class);
        switch (v.getId()) {
            case R.id.btn_text:
                mIntent.putExtra("Tag","text");
                startActivity(mIntent);
                break;
            case R.id.btn_img:
                mIntent.putExtra("Tag","image");
                startActivity(mIntent);
                break;
            case R.id.btn_video:
                mIntent.putExtra("Tag","video");
                startActivity(mIntent);
                break;
        }
    }
}
