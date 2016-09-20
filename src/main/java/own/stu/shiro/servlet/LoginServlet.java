package own.stu.shiro.servlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import own.stu.shiro.util.CrypographyUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dell on 2016/9/12.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login get ..... ");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login post ..... ");
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        password = CrypographyUtil.encMd5(password);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
            resp.sendRedirect("success.jsp"); //登录成功 跳转
        }catch (Exception e){
            e.printStackTrace();
            req.setAttribute("errorInfo", "用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }

    }
}
