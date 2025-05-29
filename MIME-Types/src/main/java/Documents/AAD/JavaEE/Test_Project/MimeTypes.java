package Documents.AAD.JavaEE.Test_Project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet("/mime")
public class MimeTypes  extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//           //read text /plain data from http request body
//      String body = new BufferedReader(new InputStreamReader(
//               req.getInputStream()))
//               .lines().collect(Collectors.joining("/n"));
//
//
//       resp.setContentType("text/plain");
//       resp.getWriter().write(body);
//
//
//    }


//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        String address = req.getParameter("address");
//
//        resp.setContentType("text/plain");
//        resp.getWriter().write(name+" "+address);
//
//    }

//read multipart/form data from a http request
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        String name = req.getParameter("name");
//        Part part=req.getPart("file");
//        String fileName=part.getSubmittedFileName();
//        resp.setContentType("text/plain");
//        resp.getWriter().write(name+" "+fileName);
//
//    }

    //read JSON data from a http request body


//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(req.getReader());
//
//        String name = jsonNode.get("Name").asText();
//        String address = jsonNode.get("Address").asText();
//
//        resp.setContentType("text/plain");
//        resp.getWriter().println(name + " " + address);
//
//
//    }



    //how to read JASON Array From http request

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        List <JsonNode> users = mapper.readValue(req.getReader(), new TypeReference<List<JsonNode>>() {});

        for (JsonNode user : users) {
            //data reading
            String name = user.get("Name").asText();
            String address = user.get("Address").asText();
            System.out.println(name +" "+ address);
        }
        resp.setContentType("text/plain");
        resp.getWriter().println("ok");
    }
}