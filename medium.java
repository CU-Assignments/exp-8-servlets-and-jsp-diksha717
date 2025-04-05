import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private final String jdbcURL = "jdbc:mysql://localhost:3306/yourdb";
    private final String dbUser = "root";
    private final String dbPass = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empId = request.getParameter("id");

        out.println("<html><head><title>Employee Search</title></head><body>");
        out.println("<h2>Search Employee by ID</h2>");
        out.println("<form method='get' action='employees'>");
        out.println("<input type='text' name='id' placeholder='Enter ID' />");
        out.println("<input type='submit' value='Search' />");
        out.println("<a href='employees'> Show All </a>");
        out.println("</form>");

        out.println("<h3>Employee List:</h3>");
        out.println("<table border='1' cellpadding='5'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Department</th><th>Salary</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPass);

            PreparedStatement stmt;

            if (empId != null && !empId.isEmpty()) {
                stmt = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
                stmt.setInt(1, Integer.parseInt(empId));
            } else {
                stmt = conn.prepareStatement("SELECT * FROM employees");
            }

            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("department") + "</td>");
                out.println("<td>" + rs.getDouble("salary") + "</td>");
                out.println("</tr>");
            }

            if (!hasResults) {
                out.println("<tr><td colspan='4'>No employee(s) found.</td></tr>");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("</table>");
        out.println("</body></html>");
    }
}
