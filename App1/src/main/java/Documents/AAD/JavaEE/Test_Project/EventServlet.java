package Documents.AAD.JavaEE.Test_Project;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/event")
public class EventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setHeaders(resp);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/events_db", "root", "sandaru2003");

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

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setHeaders(resp);

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setHeaders(resp);

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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setHeaders(resp);

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

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
