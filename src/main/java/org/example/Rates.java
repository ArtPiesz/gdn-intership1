package org.example;

public class Rates implements Comparable<Rates> {
    private float mid;
    private float bid;
    private float ask;
    public float getMid() {
        return mid;
    }

    public void setMid(float mid) {
        this.mid = mid;
    }

    public float getAsk() {
        return ask;
    }

    public void setAsk(float ask) {
        this.ask = ask;
    }

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    @Override
    public int compareTo(Rates rate) {
        if(this.mid > rate.getMid()) return 1;
        if(this.mid < rate.getMid()) return -1;
        return 0;
    }
}
