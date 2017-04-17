import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Logout extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException{

        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession(true);
        session.invalidate();

        res.sendRedirect("/schedule/LoginPage");
    }
}
