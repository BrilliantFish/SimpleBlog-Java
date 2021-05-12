package cn.lly.blog.service.implement;

import cn.lly.blog.dao.BlogDao;
import cn.lly.blog.dao.implement.BlogDaoImple;
import cn.lly.blog.domain.Blog;
import cn.lly.blog.domain.PageBean;
import cn.lly.blog.service.BlogService;

import java.util.List;

public class BlogServiceImple implements BlogService {
    private BlogDao blogDao = new BlogDaoImple();

    @Override
    public PageBean<Blog> pageQuery(int currentPage, int pageSize) {
        //设置总记录数
        int totalCount = blogDao.countBlog();

        //封装PageBean
        PageBean<Blog> pb = new PageBean<Blog>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Blog> list = blogDao.findByPage(start, pageSize);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);


        return pb;
    }

    @Override
    public void deleteBlog(int blogId) {
        blogDao.deleteByBlogId(blogId);
    }

    @Override
    public void save(String blogSrc, String blogTitle, String imgSrc) {
        blogDao.save(blogSrc, blogTitle, imgSrc);
    }
}
