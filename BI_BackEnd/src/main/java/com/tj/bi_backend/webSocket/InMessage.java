package com.tj.bi_backend.webSocket;

import java.util.Date;

public class InMessage {
    private String from;
    private String to;
    private String content;
    private Date data;

    public InMessage() {
    }

    public InMessage(String from, String to, String content, Date data) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
