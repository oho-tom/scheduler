import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CreateUserCheck extends HttpServlet {

    protected Connection conn = null;

    public void init() throws ServletException{
        String url = "jdbc:mysql://localhost/servletschedule";
        String user = "scheduleuser";
        String password = "schedulepass";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
        }catch (ClassNotFoundException e){
            log("ClassNotFoundException:" + e.getMessage());
        }catch (SQLException e){
            log("SQLException:" + e.getMessage());
        }catch (Exception e){
            log("Exception:" + e.getMessage());
        }
    }

    public void destory(){
        try{
            if (conn != null){
                conn.close();
            }
        }catch (SQLException e){
            log("SQLException:" + e.getMessage());
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException{

        res.setContentType("text/html; charset=utf-8");
        PrintWriter out = res.getWriter();

        String user = req.getParameter("user");
        String pass = req.getParameter("pass");
        String rollStr = req.getParameter("roll");
        int roll;
        if (rollStr == null || rollStr.length() == 0){
            roll = -1;
        }else{
            roll = Integer.parseInt(rollStr);
        }

        HttpSession session = req.getSession(true);

        boolean check = createUser(user, pass, roll);
        if (check){
            session.setAttribute("CreateUserCheck", "Success");
            res.sendRedirect("/schedule/NewUser");
        }else{
            session.setAttribute("CreateUserCheck", "Fail");
            res.sendRedirect("/schedule/NewUser");
        }
    }

    protected boolean createUser(String user, String pass, int roll){
        if (user == null || user.length() == 0 || pass == null || pass.length() == 0 || roll == -1){
            return false;
        }

        try {

            String sql = "insert into usertable (user, pass, roll) values (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setInt(3, roll);
            int num = pstmt.executeUpdate();

            return true;
        }catch (SQLException e){
            log("SQLException:" + e.getMessage());
            return false;
        }
    }
}
