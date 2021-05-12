package cn.lly.blog.service;

import cn.lly.blog.domain.User;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean regist(User user);

    boolean emailExist(String email);

    boolean login(User user);
}