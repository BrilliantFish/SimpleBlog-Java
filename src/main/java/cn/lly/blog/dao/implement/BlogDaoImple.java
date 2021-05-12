package cn.lly.blog.dao.implement;

import cn.lly.blog.dao.BlogDao;
import cn.lly.blog.domain.Blog;
import cn.lly.blog.domain.User;
import cn.lly.blog.util.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class BlogDaoImple implements BlogDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
    @Override
    public int countBlog() {
        String sql = "SELECT COUNT(*) FROM blog";

        return template.queryForObject(sql, Integer.class);
    }

    @Override
    public List<Blog> findByPage(int start, int pageSize) {
        List<Blog> blogList = null;

        try {
            //1.定义sql
            String sql = "SELECT * FROM blog LIMIT ?, ?";
            //2.执行sql
            blogList = template.query(sql, new BeanPropertyRowMapper<Blog>(Blog.class),start, pageSize);
            System.out.println(blogList.toString());
        }
        catch (Exception e) {

        }
        return blogList;
    }

    @Override
    public void deleteByBlogId(int blogId) {
        try {
            //1.定义sql
            String sql = "DELETE FROM blog WHERE blogId = ?";
            //2.执行sql
            template.update(sql, blogId);
        }
        catch (Exception e) {

        }
    }

    @Override
    public void save(String blogSrc, String blogTitle, String imgSrc) {
        try {
            //1.定义sql
            String sql = "INSERT INTO  blog(blogSrc,blogTitle,imgSrc) VALUES(?,?,?)";
            //2.执行sql
            template.update(sql, blogSrc, blogTitle, imgSrc);
        }
        catch (Exception e) {

        }
    }
}
