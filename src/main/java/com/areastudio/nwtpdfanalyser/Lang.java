package com.areastudio.nwtpdfanalyser;

import java.io.Serializable;

/**
 * Created by julien on 27.11.15.
 */
public class Lang implements Serializable {
    int id;
    String text;
    String code;
    boolean intro;
    boolean appendix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIntro() {
        return intro;
    }

    public void setIntro(boolean intro) {
        this.intro = intro;
    }

    public boolean isAppendix() {
        return appendix;
    }

    public void setAppendix(boolean appendix) {
        this.appendix = appendix;
    }
}
