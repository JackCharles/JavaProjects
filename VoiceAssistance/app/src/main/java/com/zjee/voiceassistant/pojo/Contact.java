package com.zjee.voiceassistant.pojo;

/**
 * <p>Date: 2018/4/10 20:58</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class Contact {
    String userName;
    String phoneNumber;

    public Contact(String userName, String phoneNumber){
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }
}
