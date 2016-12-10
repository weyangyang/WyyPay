package com.wyy.pay.bean;

import java.io.Serializable;

/**
 * Created by liyusheng on 16/12/10.
 */

public class StatementsDiscountBean implements Serializable {
   private int type;
    private double number;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "StatementsDiscountBean{" +
                "type=" + type +
                ", number=" + number +
                '}';
    }
}
