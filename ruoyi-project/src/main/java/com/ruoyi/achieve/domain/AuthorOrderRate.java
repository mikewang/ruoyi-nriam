package com.ruoyi.achieve.domain;

import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

public class AuthorOrderRate {

    public BigDecimal rate;

    public Integer userId;

    public Integer order;


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
