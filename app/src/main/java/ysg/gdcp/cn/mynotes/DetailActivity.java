package ysg.gdcp.cn.mynotes;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import ysg.gdcp.cn.mynotes.data.NotesDatabase;

/**
 * Created by Administrator on 2017/5/12 07:10.
 *
 * @author ysg
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private NotesDatabase mNoteDB;
    private SQLiteDatabase dbWriter;
    private ImageView ivDimg;
    private VideoView vvDvideo;
    private TextView tvDtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();
        mNoteDB = new NotesDatabase(this);
        dbWriter = mNoteDB.getWritableDatabase();
    }

    private void initViews() {
        findViewById(R.id.btn_ddel).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        ivDimg = (ImageView) findViewById(R.id.iv_dimg);
        vvDvideo = (VideoView) findViewById(R.id.vv_dvideo);
        tvDtext = (TextView) findViewById(R.id.tv_dtext);
        if (getIntent().getStringExtra(NotesDatabase.imagePath).equals("null")) {
            ivDimg.setVisibility(View.GONE);
        } else {
            ivDimg.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(NotesDatabase.imagePath));
            ivDimg.setImageBitmap(bitmap);
        }
        if (getIntent().getStringExtra(NotesDatabase.videoPath).equals("null")) {
            vvDvideo.setVisibility(View.GONE);
        }else{
            vvDvideo.setVisibility(View.VISIBLE);
            vvDvideo.setVideoURI(Uri.parse(getIntent().getStringExtra(NotesDatabase.videoPath)));
            vvDvideo.start();
        }

        tvDtext.setText(getIntent().getStringExtra(NotesDatabase.CONTENT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_ddel:
                delData();
                finish();
                break;
        }
    }

    private void delData() {
        dbWriter.delete(NotesDatabase.TABLE_NAME, "_id=" + getIntent().getIntExtra(NotesDatabase.ID, 0), null);

    }

}
