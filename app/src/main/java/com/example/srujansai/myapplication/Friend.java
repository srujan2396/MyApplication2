package com.example.srujansai.myapplication;

/**
 * Created by srujan sai on 11-07-2017.
 */

public class Friend {

    String fname,fphno,fstatus,uphno;
    public Friend(String fname, String fphno, String fstatus,String uphno){
        this.fname=fname;
        this.fphno=fphno;
        this.fstatus=fstatus;
        this.uphno=uphno;

    }

    public String getUphno() {
        return uphno;
    }

    public void setUphno(String uphno) {
        this.uphno = uphno;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFphno() {
        return fphno;
    }

    public void setFphno(String fphno) {
        this.fphno = fphno;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }
}
