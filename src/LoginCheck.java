import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginCheck extends HttpServlet {

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

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException{

        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        HttpSession session = request.getSession(true);

        boolean check = authUser(user, pass, session);
        if (check){
            /* �F�����Z�b�g */
            session.setAttribute("login", "OK");

            /* �F������K��MonthView�T�[�u���b�g������ */
            response.sendRedirect("/schedule/MonthView");
        }else{
            /* �F���ｸ�s����A���O�C�������� */
            session.setAttribute("status", "Not Auth");
            response.sendRedirect("/schedule/LoginPage");
        }
    }

    protected boolean authUser(String user, String pass, HttpSession session){
        if (user == null || user.length() == 0 || pass == null || pass.length() == 0){
            return false;
        }

        try {
            String sql = "SELECT * FROM usertable WHERE user = ? && pass = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                int userid = rs.getInt("id");
                int roll = rs.getInt("roll");
                String username = rs.getString("user");

                session.setAttribute("userid", Integer.toString(userid));
                session.setAttribute("roll", Integer.toString(roll));
                session.setAttribute("username", username);

                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            log("SQLException:" + e.getMessage());
            return false;
        }
    }
}
