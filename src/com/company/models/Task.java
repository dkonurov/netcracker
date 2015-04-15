package com.company.models;


public class Task {

    private Integer id;
    private String url;
    private String name;
    private Long attempts;
    private Long attemptsOk;
    private Long time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAttempts() {
        return attempts;
    }

    public void setAttempts(Long attempts) {
        this.attempts = attempts;
    }

    public Long getAttemptsOk() {
        return attemptsOk;
    }

    public void setAttemptsOk(Long attemptsOk) {
        this.attemptsOk = attemptsOk;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
