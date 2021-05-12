package cn.lly.blog.dao.implement;

import cn.lly.blog.dao.UserDao;
import cn.lly.blog.domain.User;
import cn.lly.blog.util.JDBCUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImple implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());

    @Override
    public User findByName(String username){
        User user = null;
        try {
            //1.定义sql
            String sql = "SELECT * FROM blog_user WHERE userName = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        }
        catch (Exception e) {

        }

        return user;
    }

    @Override
    public void save(User user) {
        try {
            //1.定义sql
            String sql = "INSERT INTO  blog_user(userName,password,email) VALUES(?,?,?)";
            //2.执行sql
            template.update(sql,user.getUserName(),user.getPassword(),user.getEmail());
        }
        catch (Exception e) {

        }
    }

    @Override
    public User findByNameAndPassword(String name, String password) {
        User user = null;

        try {
            //1.定义sql
            String sql = "SELECT * FROM blog_user WHERE userName = ? AND password = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), name, password);
        }
        catch (Exception e) {

        }

        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        try {
            //1.定义sql
            String sql = "SELECT * FROM blog_user WHERE email = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), email);
        }
        catch (Exception e) {

        }

        return user;
    }
}
