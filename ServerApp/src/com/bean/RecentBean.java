package com.bean;

import java.util.ArrayList;
import java.util.List;

public class RecentBean {
	String recordMain="";
    List<RecordBean> recordBeanlist = new ArrayList<>();

    @Override
    public String toString() {
        return "RecentBean{" +
                "recordMain='" + recordMain + '\'' +
                ", recordBeanlist=" + recordBeanlist +
                '}';
    }

    public String getRecordMain() {
        return recordMain;
    }

    public void setRecordMain(String recordMain) {
        this.recordMain = recordMain;
    }

    public List<RecordBean> getRecordBeanlist() {
        return recordBeanlist;
    }

    public void setRecordBeanlist(List<RecordBean> recordBeanlist) {
        this.recordBeanlist = recordBeanlist;
    }
}
