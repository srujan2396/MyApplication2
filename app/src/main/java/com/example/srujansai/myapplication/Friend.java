package com.example.srujansai.myapplication;

/**
 * Created by srujan sai on 11-07-2017.
 */

public class Friend {

    String fname,fphno,fstatus;
    public Friend(String fname, String fphno, String fstatus){
        this.fname=fname;
        this.fphno=fphno;
        this.fstatus=fstatus;

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
