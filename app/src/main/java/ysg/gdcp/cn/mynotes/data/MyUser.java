package ysg.gdcp.cn.mynotes.data;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/5/12 21:36.
 *
 * @author ysg
 */

public class MyUser extends BmobUser {
    private  String PhoneNum;

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }
}
