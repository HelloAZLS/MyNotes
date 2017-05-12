package ysg.gdcp.cn.mynotes.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/12 15:55.
 *
 * @author ysg
 */

public class NoteInfo extends BmobObject {
    private String CONTENT ;
    private String imagePath;
    private String videoPath;
    private String TIME;

    public NoteInfo() {
    }

    public NoteInfo( String CONTENT, String imagePath, String videoPath, String TIME) {

        this.CONTENT = CONTENT;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.TIME = TIME;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }


    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }
}
