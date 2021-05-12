package cn.lly.blog.dao;

import cn.lly.blog.domain.Admin;

public interface AdminDao {
    /**
     * 根据用户名查询用户信息
     * @param name
     * @return
     */
    Admin findByName(String name);

    /**
     * 用户保存
     * @param admin
     */
    void save(Admin admin);

    Admin findByNameAndPassword(String name, String password);
}
