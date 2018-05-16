package com.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultBean {
	 String passResult="";
	    String username = "";
	    List<RecordBean> recordBeanlist = new ArrayList<>();//未完成的表单，该表单只有一项
	    List<String> finFileList = new ArrayList<>();//已完成的文件列表
	    String unconCount = "";
	    List<String> users = new ArrayList<>();

	    List<String> type1 = new ArrayList<>();
	    List<String> type2 = new ArrayList<>();
	    List<String> type3 = new ArrayList<>();
	    List<String> type4 = new ArrayList<>();

	    @Override
	    public String toString() {
	        return "ResultBean{" +
	                "passResult='" + passResult + '\'' +
	                ", recordBeanlist=" + recordBeanlist +
	                ", type1=" + type1 +
	                ", type2=" + type2 +
	                ", type3=" + type3 +
	                ", type4=" + type4 +
	                '}';
	    }
	    
	    public List<String> getUsers() {
	        return users;
	    }

	    public void setUsers(List<String> users) {
	        this.users = users;
	    }

	    public List<String> getFinFileList() {
	        return finFileList;
	    }

	    public void setFinFileList(List<String> finFileList) {
	        this.finFileList = finFileList;
	    }

	    public String getUnconCount() {
	        return unconCount;
	    }

	    public void setUnconCount(String unconCount) {
	        this.unconCount = unconCount;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPassResult() {
	        return passResult;
	    }

	    public void setPassResult(String passResult) {
	        this.passResult = passResult;
	    }

	    public List<RecordBean> getRecordBeanlist() {
	        return recordBeanlist;
	    }

	    public void setRecordBeanlist(List<RecordBean> recordBeanlist) {
	        this.recordBeanlist = recordBeanlist;
	    }

	    public List<String> getType1() {
	        return type1;
	    }

	    public void setType1(List<String> type1) {
	        this.type1 = type1;
	    }

	    public List<String> getType2() {
	        return type2;
	    }

	    public void setType2(List<String> type2) {
	        this.type2 = type2;
	    }

	    public List<String> getType3() {
	        return type3;
	    }

	    public void setType3(List<String> type3) {
	        this.type3 = type3;
	    }

	    public List<String> getType4() {
	        return type4;
	    }

	    public void setType4(List<String> type4) {
	        this.type4 = type4;
	    }

}
