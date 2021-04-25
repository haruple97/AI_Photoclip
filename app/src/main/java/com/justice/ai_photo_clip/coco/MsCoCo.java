package com.justice.ai_photo_clip.coco;

public class MsCoCo {
    private String code;
    private String name;
    private int important;
    private int filter;

    public MsCoCo(String code, String name, int important, int filter) {
        this.code = code;
        this.name = name;
        this.important = important;
        this.filter = filter;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getImportant() {
        return important;
    }

    public int getFilter() {
        return filter;
    }

    @Override
    public String toString() {
        return "MsCoCo{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", important=" + important +
                ", filter=" + filter +
                '}';
    }
}
