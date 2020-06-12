package in.dete.oops;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurant implements Serializable {
    private String n;
    private String v;
    private String vn;
    private String r;
    private String uid;
    private ArrayList<String> img; // Image URLs

    public Restaurant(){

    }
    public Restaurant(String n, String v, String vn, String r, ArrayList<String> img, String uid) {
        this.n = n;
        this.v = v;
        this.vn = vn;
        this.r = r;
        this.img = img;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }
}
