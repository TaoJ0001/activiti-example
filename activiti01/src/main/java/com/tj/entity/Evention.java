package com.tj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 出差申请中的流程变量对象
 * 如果将entity存储到流程变量中，必须实现序列化接口Serializable，为了防止由于新增字段无法反序列化，需要生成serialVersionUID.
 */
public class Evention implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 出差单的名字
     */
    private String evectionName;

    /**
     * 出差天数
     */
    private  Double num;

    /**
     * 出差开始时间
     */
    private Date beginDate;

    /**
     * 出差结束时间
     */
    private Date endDate;

    /**
     * 出差目的地
     */
    private String destination;

    /**
     * 出差的原因
     */
    private String reson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvectionName() {
        return evectionName;
    }

    public void setEvectionName(String evectionName) {
        this.evectionName = evectionName;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

}
