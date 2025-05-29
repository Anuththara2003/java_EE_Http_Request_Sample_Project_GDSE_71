package Documents.AAD.JavaEE.Test_Project;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dbcp")
public class DBCPServlet extends HttpServlet {

    BasicDataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/events_db");
        dataSource.setUsername("root");
        dataSource.setPassword("sandaru2003");
        dataSource.setInitialSize(50);
        dataSource.setMaxTotal(100);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM eventTable").executeQuery();
            List<Map<String, String>> elist = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, String> event = new HashMap<>();
                event.put("eid", resultSet.getString("id")); // frontend uses eid, but actual column is id
                event.put("ename", resultSet.getString("name"));
                event.put("edescription", resultSet.getString("description"));
                event.put("edate", resultSet.getString("date"));
                event.put("eplace", resultSet.getString("place"));
                elist.add(event);
            }

            resp.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), elist);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> event = mapper.readValue(req.getInputStream(), Map.class);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/events_db", "root", "sandaru2003");

            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO eventTable (id, name, description, date, place) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, event.get("eid")); // frontend sends "eid"
            stmt.setString(2, event.get("ename"));
            stmt.setString(3, event.get("edescription"));
            stmt.setString(4, event.get("edate"));
            stmt.setString(5, event.get("eplace"));

            int rows = stmt.executeUpdate();
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), rows);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> event = mapper.readValue(req.getInputStream(), Map.class);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/events_db", "root", "sandaru2003");

            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE eventTable SET name = ?, description = ?, date = ?, place = ? WHERE id = ?"
            );
            stmt.setString(1, event.get("ename"));
            stmt.setString(2, event.get("edescription"));
            stmt.setString(3, event.get("edate"));
            stmt.setString(4, event.get("eplace"));
            stmt.setString(5, event.get("eid")); // still "eid" from frontend

            int rows = stmt.executeUpdate();
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), rows);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
        String eid = body.get("eid");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/events_db", "root", "sandaru2003");

            PreparedStatement stmt = connection.prepareStatement("DELETE FROM eventTable WHERE id = ?");
            stmt.setString(1, eid); // "eid" is just the request key, DB uses "id"

            int rows = stmt.executeUpdate();
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), rows);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
