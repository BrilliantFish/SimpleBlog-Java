package cn.lly.blog.dao;

import cn.lly.blog.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 用户保存
     * @param user
     */
    void save(User user);

    User findByNameAndPassword(String name, String password);

    User findByEmail(String email);
}
