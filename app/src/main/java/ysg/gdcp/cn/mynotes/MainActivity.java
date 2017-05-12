package ysg.gdcp.cn.mynotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;
import ysg.gdcp.cn.mynotes.data.NoteDatabaseUtils;
import ysg.gdcp.cn.mynotes.data.NotesDatabase;
import ysg.gdcp.cn.mynotes.domain.NoteInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ListView mLvNote;
    private Intent mIntent;
    private BaseAdapter myAdapter;
    private NotesDatabase mNoteDB;
    private SQLiteDatabase mDbReader;
    private Cursor cursor;
    private boolean isGome;
    private LinearLayout llBtn;
    private ArrayList<NoteInfo> mNoteList;
    private boolean isCleanServerData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mNoteDB = new NotesDatabase(this);
        mDbReader = mNoteDB.getReadableDatabase();
        Bmob.initialize(this, "c022a6dff9fe2252de5e21742f40533c");
    }

    private void initDatas() {

        cursor = mDbReader.query(NotesDatabase.TABLE_NAME, null, null, null, null, null, null);
        myAdapter = new MyAdapter(this, cursor);
        mLvNote.setAdapter(myAdapter);
    }

    private void initViews() {

        findViewById(R.id.btn_text).setOnClickListener(this);
        findViewById(R.id.btn_img).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);
        findViewById(R.id.btn_icon).setOnClickListener(this);
        mLvNote = (ListView) findViewById(R.id.lv_data);
        llBtn = (LinearLayout) findViewById(R.id.ll_btn);
        mLvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent detaliIntent = new Intent(MainActivity.this, DetailActivity.class);
                detaliIntent.putExtra(NotesDatabase.ID, cursor.getInt(cursor.getColumnIndex(NotesDatabase.ID)));
                detaliIntent.putExtra(NotesDatabase.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDatabase.CONTENT)));
                detaliIntent.putExtra(NotesDatabase.TIME, cursor.getString(cursor.getColumnIndex(NotesDatabase.TIME)));
                detaliIntent.putExtra(NotesDatabase.imagePath, cursor.getString(cursor.getColumnIndex(NotesDatabase.imagePath)));
                detaliIntent.putExtra(NotesDatabase.videoPath, cursor.getString(cursor.getColumnIndex(NotesDatabase.videoPath)));
                startActivity(detaliIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
        isGome = false;
        toggle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cacle:
                Toast.makeText(this, "你点击了注销条目", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tong:
                Toast.makeText(this, "开始备份", Toast.LENGTH_SHORT).show();
                synchro();
                break;
            case R.id.reduction:
                Toast.makeText(this, "开始还原", Toast.LENGTH_SHORT).show();
                reduction();
                break;
        }
        return true;

    }

    //还原数据的方法
    private void reduction() {
        //删除本地的数据
        mDbReader.delete(NotesDatabase.TABLE_NAME, null, null);
        //请求网络的数据
        findAllDataFromBmob();
        //把网络的数据写入数据库
    }

    //备份方法
    private void synchro() {
        //清除服务器上的数据
        cleanServceData();

        //备份数据
        backUps();

    }

    //删除所有服务器的数据
    private void cleanServceData() {
        //查找所有数据
        isCleanServerData = true;
        findAllDataFromBmob();
    }

    private void findAllDataFromBmob() {
        BmobQuery<NoteInfo> query = new BmobQuery<NoteInfo>();
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<NoteInfo>() {
            @Override
            public void done(List<NoteInfo> object, BmobException e) {
                if (e == null) {
//                    Toast.makeText(MainActivity.this, "查询成功：共\" + object.size() + \"条数据。", Toast.LENGTH_SHORT).show();
                    for (NoteInfo info : object) {
                        //删除数据
                        if (isCleanServerData) {
                            deleteDataFromBmob(info);
                        } else {
                            //还原数据之把数据写到数据库
                            addDataFromDatabase(info);
                        }
                    }
                    isCleanServerData = false;
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //删除Bmob的数据
    private void deleteDataFromBmob(NoteInfo info) {

        info.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    //向数据库中插入数据
    private void addDataFromDatabase(NoteInfo info) {
        ContentValues cv = new ContentValues();
        cv.put(NotesDatabase.CONTENT, info.getCONTENT());
        cv.put(NotesDatabase.TIME, info.getTIME());
        cv.put(NotesDatabase.imagePath, info.getImagePath());
        cv.put(NotesDatabase.videoPath, info.getVideoPath());
        mDbReader.insert(NotesDatabase.TABLE_NAME, null, cv);
        initDatas();
        Toast.makeText(this, "还原数据成功", Toast.LENGTH_SHORT).show();
    }

    private void backUps() {
        //得到数据库表的数据
        mNoteList = new ArrayList<>();
        ArrayList<BmobObject> mNoteInfoList = NoteDatabaseUtils.getDateFromDatabase(mDbReader, mNoteList);
        if (mNoteInfoList != null) {
            //将数据备份到服务器
            new BmobBatch().insertBatch(mNoteInfoList).doBatch(new QueryListListener<BatchResult>() {
                public void done(List<BatchResult> list, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            BatchResult result = list.get(i);
                            BmobException ex = result.getError();
                            if (ex == null) {

                            } else {
//                                Log.i("同步操作", "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                                Toast.makeText(MainActivity.this, "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(MainActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "备份失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        mIntent = new Intent(this, AddActivity.class);
        switch (v.getId()) {
            case R.id.btn_text:
                mIntent.putExtra("Tag", "text");
                startActivity(mIntent);
                break;
            case R.id.btn_img:
                mIntent.putExtra("Tag", "image");
                startActivity(mIntent);
                break;
            case R.id.btn_video:
                mIntent.putExtra("Tag", "video");
                startActivity(mIntent);
                break;
            case R.id.btn_icon:
                toggle();
                break;
        }
    }

    private void toggle() {
        if (isGome) {
            isGome = false;
            llBtn.setVisibility(View.VISIBLE);
        } else {
            isGome = true;
            llBtn.setVisibility(View.GONE);
        }
    }

}
