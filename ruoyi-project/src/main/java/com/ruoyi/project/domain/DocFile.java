package com.ruoyi.project.domain;


public class DocFile extends Object {
    private Integer url;
    private String name;

    public Integer getUrl() {
        return url;
    }

    public void setUrl(Integer url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocFile{" +
                "url=" + url +
                ", name='" + name + '\'' +
                '}';
    }
}