package cn.lly.blog.web.servlet;

import cn.lly.blog.domain.Blog;
import cn.lly.blog.domain.PageBean;
import cn.lly.blog.service.BlogService;
import cn.lly.blog.service.implement.BlogServiceImple;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet("/blog/*")
public class BlogServlet extends BaseServlet {
    private BlogService service = new BlogServiceImple();

    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        //2.处理参数
        int currentPage = 0; //当前页码，如果不传递，则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }
        else{
            currentPage = 1;
        }

        int pageSize = 0;   //每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }
        else{
            pageSize = 5;
        }

        //3. 调用service查询PageBean对象
        PageBean<Blog> pb = service.pageQuery(currentPage, pageSize);

        System.out.println(pb);

        //4. 将pageBean对象序列化为json，返回
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), pb);
    }

    public void getPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取参数的中文编码
        String blogSrc = request.getParameter("blogSrc");
        blogSrc = new String(blogSrc.getBytes("iso-8859-1"),"utf-8");  //重新编码
        System.out.println(blogSrc);
        //2.使用字节输入流加载文件进内存
        //2.1找到文件服务器路径
        ServletContext servletContext = this.getServletContext();
        String realPath = servletContext.getRealPath("/blog/" + blogSrc);
        System.out.println(realPath);
        //2.2用字节流关联
        FileInputStream fis = new FileInputStream(realPath);

        //4.将输入流的数据写出到输出流中
        response.setContentType("text/txt;charset=utf-8");
        ServletOutputStream sos = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len = 0;
        while((len = fis.read(buff)) != -1){
            sos.write(buff,0,len);
        }

        fis.close();
    }

    public void deleteBlog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String blogIdStr = request.getParameter("blogId");

        //处理参数
        int blogId = Integer.parseInt(blogIdStr);

        service.deleteBlog(blogId);

        request.getRequestDispatcher("/admin_blog_manage.html").forward(request, response);
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入save");
        request.setCharacterEncoding("utf-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String imgSrc = "";
        String blogSrc = "";
        if(isMultipart){//如果form表单中有Multipart属性则执行
            DiskFileItemFactory factory=new DiskFileItemFactory();
            ServletFileUpload upload=new ServletFileUpload(factory);
            try {
                //通过parseRequest解析所有form标签中的字段并将其内容解析到List中
                List<FileItem> list=upload.parseRequest(request);
                for (FileItem fileItem:list){
                    String itemName = fileItem.getFieldName();  //获取普通表单字段的name值
                    if(fileItem.isFormField()){
                        if(itemName.equals("imgSrc")){
                            imgSrc = fileItem.getString("UTF-8");
                        }
                        else{

                        }
                    }
                    else{
                        //获取文件名
                        String name = fileItem.getName();
                        blogSrc = name;
                        //获取文件内容
                        //定义文件上传路径
                        //获取服务器路径
                        String path = request.getSession().getServletContext().getRealPath("/blog");
                        File file = new File(path, name);
                        fileItem.write(file);
                    }
                }
            }
            catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String blogTitle = blogSrc.substring(0,blogSrc.lastIndexOf('.'));

        service.save(blogSrc, blogTitle, imgSrc);
    }
}
