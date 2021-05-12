package cn.lly.blog.domain;

import java.io.Serializable;

public class Blog implements Serializable {
    private int blogId;
    private String blogSrc;
    private String blogTitle;
    private String imgSrc;

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getBlogSrc() {
        return blogSrc;
    }

    public void setBlogSrc(String blogSrc) {
        this.blogSrc = blogSrc;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", blogSrc='" + blogSrc + '\'' +
                ", blogTitle='" + blogTitle + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                '}';
    }
}
