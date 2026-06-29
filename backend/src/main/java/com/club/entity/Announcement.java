package com.club.entity;

import java.util.Date;

public class Announcement {
    private Integer id;
    private Integer clubId;
    private String title;
    private String content;
    private Date publishTime;
    private Integer status;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getClubId() { return clubId; }
    public void setClubId(Integer clubId) { this.clubId = clubId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
