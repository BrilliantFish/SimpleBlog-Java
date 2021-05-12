package cn.lly.blog.service;

import cn.lly.blog.domain.Blog;
import cn.lly.blog.domain.PageBean;

import java.util.List;

public interface BlogService {
    PageBean<Blog> pageQuery(int currentPage, int pageSize);
    void deleteBlog(int blogId);
    void save(String blogSrc, String blogTitle, String imgSrc);
}
