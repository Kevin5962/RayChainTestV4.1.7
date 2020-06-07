package cn.mrray.raybaas.demo.entity;

import java.io.Serializable;

public class Transfer implements Serializable {
    private String from;
    private String to;
    private Long amount;

    public Transfer(String from, String to, Long amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Long getAmount() {
        return amount;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
