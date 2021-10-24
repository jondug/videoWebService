package org.webexample.bilibili.model;

public class Works_comment {
    private int commentid;
    private int userid;
    private int worksid;
    private String content;

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getWorksid() {
        return worksid;
    }

    public void setWorksid(int worksid) {
        this.worksid = worksid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
