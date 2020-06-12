package in.dete.oops;

import java.io.Serializable;

public class ShowCart implements Serializable {

    String rn, tp,i;

    public ShowCart(String rn, String tp, String i) {
        this.rn = rn;
        this.tp = tp;
        this.i = i;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }
}
