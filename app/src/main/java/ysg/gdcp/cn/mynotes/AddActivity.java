package ysg.gdcp.cn.mynotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ysg.gdcp.cn.mynotes.data.NotesDatabase;

/**
 * Created by Administrator on 2017/5/11 19:45.
 *
 * @author ysg
 */

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private String mValue;
    private ImageView mIvImage;
    private VideoView mVvVideo;
    private EditText edContent;
    private NotesDatabase mNoteDB;
    private SQLiteDatabase mDbWriter;
    private File imgPath;
    private File vidoePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mValue = getIntent().getStringExtra("Tag");
        initViews();
        mNoteDB = new NotesDatabase(this);
        mDbWriter = mNoteDB.getWritableDatabase();
    }

    private void initViews() {
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_cacel).setOnClickListener(this);
        mIvImage = (ImageView) findViewById(R.id.iv_aimage);
        mVvVideo = (VideoView) findViewById(R.id.vv_avideo);
        edContent = (EditText) findViewById(R.id.et_text);
        if (mValue.equals("text")) {
            //文字
            mIvImage.setVisibility(View.GONE);
            mVvVideo.setVisibility(View.GONE);
        } else if (mValue.equals("image")) {
            mIvImage.setVisibility(View.VISIBLE);
            mVvVideo.setVisibility(View.GONE);
            //打开照相机，把拍的照片放到指定路径
            Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imgPath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".jpg");
            imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgPath));
            startActivityForResult(imgIntent, 1);

        } else if (mValue.equals("video")) {
            mIvImage.setVisibility(View.GONE);
            mVvVideo.setVisibility(View.VISIBLE);
            //打开摄像机机，把拍的视频放到指定路径
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            vidoePath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".mp4");
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(vidoePath));
            startActivityForResult(videoIntent, 2);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //加载指定路劲的图片
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath.getAbsolutePath());
            mIvImage.setImageBitmap(bitmap);
        }else if(requestCode==2){
            mVvVideo.setVideoURI(Uri.fromFile(vidoePath));
            mVvVideo.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                addData();
                finish();
                break;
            case R.id.btn_cacel:
                finish();
                break;
        }
    }

    public void addData() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDatabase.CONTENT, edContent.getText().toString());
        cv.put(NotesDatabase.TIME, getTime());
        cv.put(NotesDatabase.imagePath, imgPath + "");
        cv.put(NotesDatabase.videoPath, vidoePath + "");
        mDbWriter.insert(NotesDatabase.TABLE_NAME, null, cv);
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String dateStr = format.format(date);
        return dateStr;

    }




}
