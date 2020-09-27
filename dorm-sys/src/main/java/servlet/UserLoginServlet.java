package servlet;

import com.mysql.jdbc.interceptors.SessionAssociationInterceptor;
import dao.UserDAO;
import model.User;
import util.JSONUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/user/login")
public class UserLoginServlet extends AbstractBaseServlet{
    @Override
    public Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取用户信息
        User user = JSONUtil.read(req.getInputStream(),User.class);
        //根据用户名和密码在数据库中查询
        User queryUser = UserDAO.query(user);
        if (queryUser == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        HttpSession session = req.getSession();//获取Session如果获取不到创建一个
        session.setAttribute("user",queryUser);
        return null;
    }
}
