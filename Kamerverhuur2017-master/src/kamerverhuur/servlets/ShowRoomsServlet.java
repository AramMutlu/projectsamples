package kamerverhuur.servlets;

import kamerverhuur.Room;
import kamerverhuur.Storage;
import kamerverhuur.users.HomeOwner;
import kamerverhuur.users.Tenant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ShowRoomsServlet")
public class ShowRoomsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Methode om alle kamers te laten zien die de (ingelogde) Verhuurder in het bezit heeft
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user") instanceof Tenant){
            response.getWriter().print("<h1>U bent niet gemachtigd deze pagina te bezoeken</h1>" +
                    "<a href=\"./LoginServlet\">Klik hier om naar uw pagina te gaan</a>");
            return;
        } else if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("./login.html");
        }

        //maak een nieuwe pagina aan
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<body>");
        pw.print("<br>");
        pw.print("<h1>U heeft de volgende kamers: </h1>");
        //TODO

        HomeOwner currHomeOwner = (HomeOwner) request.getSession().getAttribute("user");

        for (Room r: currHomeOwner.getRooms()) {
        //check welke Verhuurder is ingelogd en print zijn/haar kamers
            pw.println("<p>" + r.toString() + "</p>");
        }

        pw.print("<br>");

        //Knoppen om een nieuwe kamer toe te voegen en om de gebruikers te bekijken
        pw.print("<FORM METHOD='LINK' ACTION='./AddRoomServlet'><INPUT TYPE='submit' VALUE='Kamer toevoegen'></FORM>");
        pw.print("<FORM METHOD='LINK' ACTION='./ShowPersonsServlet'><INPUT TYPE='submit' VALUE='Bekijk gebruikers'></FORM>");
        pw.print("<FORM METHOD='POST' ACTION='./LoginServlet'><INPUT TYPE='submit' name='extra' VALUE='logout'></FORM>");
        pw.println("</body></html>");
    }
}
