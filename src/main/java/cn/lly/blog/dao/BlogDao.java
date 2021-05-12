package cn.lly.blog.dao;

import cn.lly.blog.domain.Blog;

import java.util.List;

public interface BlogDao {
    int countBlog();
    List<Blog> findByPage(int start,int pageSize);
    void deleteByBlogId(int blogId);
    void save(String blogSrc, String blogTitle, String imgSrc);
}
