package com.club.entity;

import java.util.Date;
import java.math.BigDecimal;

public class FinanceRecord {
    private Integer id;
    private Integer clubId;
    private Integer type;
    private BigDecimal amount;
    private String item;
    private String handler;
    private Date recordTime;
    private String remark;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getClubId() { return clubId; }
    public void setClubId(Integer clubId) { this.clubId = clubId; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }
    public String getHandler() { return handler; }
    public void setHandler(String handler) { this.handler = handler; }
    public Date getRecordTime() { return recordTime; }
    public void setRecordTime(Date recordTime) { this.recordTime = recordTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
