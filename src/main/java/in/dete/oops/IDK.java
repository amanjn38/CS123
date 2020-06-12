package in.dete.oops;

import java.io.Serializable;

public class IDK implements Serializable {

    String rn, tp,i, s, otp;

    public IDK(String rn, String tp, String i, String s, String otp) {
        this.rn = rn;
        this.tp = tp;
        this.i = i;
        this.s = s;
        this.otp = otp;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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
