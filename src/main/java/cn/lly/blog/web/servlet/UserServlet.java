package cn.lly.blog.web.servlet;

import cn.lly.blog.domain.ResultInfo;
import cn.lly.blog.domain.User;
import cn.lly.blog.service.UserService;
import cn.lly.blog.service.implement.UserServiceImple;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Random;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明UserService业务对象
    private UserService service = new UserServiceImple();

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        boolean flag = service.login(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(flag){
            //登录成功
            info.setFlag(true);
            String username = user.getUserName();
            //存储登录信息到客户端
            Cookie cookie = new Cookie("loginNumber",username);
            response.addCookie(cookie);
            //存储登录信息到服务器端
            HttpSession session = request.getSession();
            session.setAttribute("LOGIN_NUMBER_"+username,username);
        }
        else{
            //登录失败
            info.setFlag(false);
            info.setErrorMsg("登录失败，请检查用户名或密码是否正确!");
        }

        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //验证校验
        String checkcode = request.getParameter("checkcode");
        //获取邮箱
        String email = request.getParameter("email");

        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER_"+email);
        session.removeAttribute("CHECKCODE_SERVER_"+email);  //为了保证验证码只能使用一次
        System.out.println(checkcode_server);

        if(!service.emailExist(email)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("邮箱："+email+"已注册！");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //比较
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(checkcode)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(flag){
            //注册成功
            info.setFlag(true);
        }
        else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败，用户名已存在!");
        }

        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 查询单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String number = "";
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals("loginNumber")) {
                    number = cookie.getValue();

                }
            }
        }

        //从session中获取登录用户
        String name = (String)request.getSession().getAttribute("LOGIN_NUMBER_"+number);
        String sign = "";

        if(name != null && name.equals(number)){
            sign = name;
            System.out.println("登录成功");
        }
        else {
            sign = "false";
            System.out.println("登录失败");

        }
        response.setContentType("text/txt;charset=utf-8");
        response.getWriter().write(sign);
    }

    public void getCheckCode(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        //获取用户邮箱，区别不同用户的验证码；
        String queryString = request.getQueryString();
        System.out.println(queryString);

        String email = queryString.substring(queryString.lastIndexOf('=') + 1);

        
        //服务器通知浏览器不要缓存
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");

        //在内存中创建一个长80，宽30的图片，默认黑色背景
        //参数一：长
        //参数二：宽
        //参数三：颜色
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        //获取画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色为黑色
        g.setColor(Color.BLACK);
        //填充图片
        g.fillRect(0,0, width,height);

        //产生4个随机验证码，12Ey
        String checkCode = getCheckCode();
        //将验证码放入HttpSession中
        request.getSession().removeAttribute("CHECKCODE_SERVER_"+email);
        request.getSession().setAttribute("CHECKCODE_SERVER_"+email,checkCode);
        System.out.println(request.getSession().getAttribute("CHECKCODE_SERVER_"+email));


        //设置画笔颜色为白色
        g.setColor(Color.WHITE);
        //设置字体的小大
        g.setFont(new Font("黑体",Font.BOLD,24));
        //向图片上写入验证码
        g.drawString(checkCode,15,25);

        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        ImageIO.write(image,"PNG",response.getOutputStream());
    }

    /**
     * 产生4位随机字符串
     */
    private String getCheckCode() {
        String base = "0123456789ABCDEFGabcdefg";
        int size = base.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=1; i <= 4; i++){
            //产生0到size-1的随机值
            int index = r.nextInt(size);
            //在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            //将c放入到StringBuffer中去
            sb.append(c);
        }
        return sb.toString();
    }
    public void exit(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String number = "";
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals("loginNumber")) {
                    number = cookie.getValue();
                }
            }
        }
        request.getSession().removeAttribute("LOGIN_NUMBER_"+number);
        System.out.println("exit: "+number);
    }
}
