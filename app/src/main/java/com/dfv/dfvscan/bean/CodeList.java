package com.dfv.dfvscan.bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class CodeList {


    String code_number;

    int is_write;

    public CodeList(String code_number) {
        this.code_number = code_number;
    }

    public CodeList(String code_number, int is_write) {
        this.code_number = code_number;
        this.is_write = is_write;
    }

    public int getIs_write() {
        return is_write;
    }

    public void setIs_write(int is_write) {
        this.is_write = is_write;
    }

    public String getCode_number() {
        return code_number;
    }

    public void setCode_number(String code_number) {
        this.code_number = code_number;
    }
}
