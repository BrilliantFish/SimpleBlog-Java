package cn.lly.blog.service.implement;

import cn.lly.blog.dao.UserDao;
import cn.lly.blog.dao.implement.UserDaoImple;
import cn.lly.blog.domain.User;
import cn.lly.blog.service.UserService;

public class UserServiceImple implements UserService {
    private UserDao userDao = new UserDaoImple();
    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByName(user.getUserName());
        //判断u是否为null
        if(u != null){
            //用户名存在，注册失败
            return false;
        }
        userDao.save(user);

        return true;
    }

    @Override
    public boolean emailExist(String email) {
        //根据邮箱查询是否有该用户
        User u = userDao.findByEmail(email);
        //判断u是否为null
        if(u != null){
            //邮箱存在，注册失败
            System.out.println(email+"邮箱存在");
            return false;
        }
        System.out.println(email+"邮箱不存在");
        return true;

    }

    @Override
    public boolean login(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByNameAndPassword(user.getUserName(),user.getPassword());
        //判断u是否为null
        if(u == null){
            //用户不存在，登录失败
            return false;
        }

        return true;
    }
}
