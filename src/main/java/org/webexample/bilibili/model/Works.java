package org.webexample.bilibili.model;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class Works {
    private int worksid;
    private int userid;
    private String title;
    private String brief;
//    @TableField("like")
    private int likenum;
    private String photo;
    private String video;
    private int collect;
    private int coin;
    private int subareaid;
    private Date workstime;
    private int vv;

    public int getVv() {
        return vv;
    }

    public void setVv(int vv) {
        this.vv = vv;
    }

    public int getWorksid() {
        return worksid;
    }

    public void setWorksid(int worksid) {
        this.worksid = worksid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int like) {
        this.likenum = like;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getSubareaid() {
        return subareaid;
    }

    public void setSubareaid(int subareaid) {
        this.subareaid = subareaid;
    }

    public Date getWorkstime() {
        return workstime;
    }

    public void setWorkstime(Date workstime) {
        this.workstime = workstime;
    }
}
