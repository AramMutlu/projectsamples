package kamerverhuur.servlets;

import kamerverhuur.Room;
import kamerverhuur.users.HomeOwner;
import kamerverhuur.users.Tenant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddRoomServlet")
public class AddRoomServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user") == null){
             response.sendRedirect("./login.html");
        } else if (request.getSession().getAttribute("user") instanceof Tenant){
            response.getWriter().print("<h1>U bent niet gemachtigd deze pagina te bezoeken</h1>" +
                    "<a href=\"./LoginServlet\">Klik hier om naar uw pagina te gaan</a>");
        }

        Room room = new Room(
                request.getParameter("title"),
                request.getParameter("address"),
                Integer.parseInt(request.getParameter("surface")),
                Integer.parseInt(request.getParameter("rent")),
                request.getParameter("city"));

        HomeOwner owner = (HomeOwner) request.getSession().getAttribute("user");
        owner.addRoom(room);

        response.sendRedirect("./ShowRoomsServlet");




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("./WEB-INF/addroom.html").forward(request, response);
    }
}
