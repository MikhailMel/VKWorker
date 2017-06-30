package ru.scratty.dialog.captcha;

import com.vk.api.sdk.exceptions.ApiCaptchaException;

public class Captcha {

    private String url;

    private String sid;

    private String text;



    public Captcha(ApiCaptchaException e) {
        this.url = e.getImage();
        this.sid = e.getSid();
    }

    public String getUrl() {
        return url;
    }

    public String getSid() {
        return sid;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "url='" + url + '\'' +
                ", sid='" + sid + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
