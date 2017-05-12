package ysg.gdcp.cn.mynotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/11 20:35.
 *
 * @author ysg
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,R.layout.main_item,null);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.iviVideo = (ImageView) convertView.findViewById(R.id.iv_video);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        cursor.moveToPosition(position);
        String text =cursor.getString(cursor.getColumnIndex("content"));
        String time  =cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("path"));
        String videoUrl =cursor.getString(cursor.getColumnIndex("video"));
        viewHolder.tvText.setText(text);
        viewHolder.tvTime.setText(time);
        viewHolder.ivImage.setImageBitmap(getImageThumbnail(url,200,200));
        viewHolder.iviVideo.setImageBitmap(getVideoThumbnail(videoUrl,200,200, MediaStore.Images.Thumbnails.MICRO_KIND));
        return convertView;
    }

    private class ViewHolder {
        TextView tvText;
        TextView tvTime;
        ImageView ivImage;
        ImageView iviVideo;

    }
    //为了防止OOM异常，要把拍的图片转化成缩略图
    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        //为true时，将不返回实际的bitmap，也不给其分配内存空间（避免OOM），但允许我们查看图片的信息（宽 高..）
        options.inJustDecodeBounds = false;
        //outWidth 图片原始宽度 outHeight 图片原始高度
        int oWidth = options.outWidth/width;
        int oHeight = options.outHeight/height;
        int or = 1;
        if (oWidth < oHeight) {
            or = oWidth;
        } else {
            or = oHeight;
        }
        if (or <= 0) {
            or = 1;
        }
        options.inSampleSize = or;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    public Bitmap getVideoThumbnail(String uri, int width, int height,int kind) {
        Bitmap bitmap =null;
        bitmap =ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return  bitmap;

    }
}
