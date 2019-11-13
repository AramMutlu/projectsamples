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

@WebServlet("/SearchRoomServlet")
public class SearchRoomServlet extends HttpServlet {

    /**
     * In de doPost wordt er gezocht naar kamers en deze worden daarna laten zien op een nieuwe pagina
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user") instanceof HomeOwner){
            response.getWriter().print("<h1>U bent niet gemachtigd deze pagina te bezoeken</h1>" +
                    "<a href=\"./login.html\">Klik hier om naar uw pagina te gaan</a>");
            return;
        } else if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("./login.html");
        }

        int surface = 0;
        int maxPrice = 0;

        //Als er niks wordt ingevuld door de gebruiker dan wordt surface 0
        if (request.getParameter("surface").equals("")) {
            surface = 0;
        } else {
            surface = Integer.parseInt(request.getParameter("surface"));
        }
        //mocht de gebruiker niks invullen bij maxprice, dan wordt er 5000000 ingevuld
        if (request.getParameter("maxprice").equals("")) {
            maxPrice = 5000000;
        } else {
            maxPrice = Integer.parseInt(request.getParameter("maxprice"));
        }
        String location = request.getParameter("location");

        //maak een nieuwe pagina aan om de kamers te laten zien
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<body>");

        //loop door alle users
        for (int i = 0; i < Storage.getInstance().getUsers().size(); i++) {
            //kijk of ze een verhuurder zijn
            if (Storage.getInstance().getUsers().get(i) instanceof HomeOwner) {
                //loop door zijn/haar kamers
                for (Room r : ((HomeOwner) Storage.getInstance().getUsers().get(i)).getRooms()) {
                    //vergelijk de oppervlakte wensen van de huurder en de oppervlakte van de kamer
                    if (r.getSquareMeters() >= surface) {
                        //verglijk de prijs van de kamer en de maximale prijs van de huurder die is ingevuld
                        if (r.getRent() <= maxPrice) {
                            //Check nu de locatie/stad
                            if (r.getCity().equals(location)) {
                                //print de kamer die aan alle eisen voldoet
                                pw.println("<p>" + r.toString() + "</p>");
                            }
                        }
                    }
                }
            }
        }
        pw.print("<FORM METHOD='POST' ACTION='./LoginServlet'><INPUT TYPE='submit' name='extra' VALUE='logout'></FORM>");
        pw.println("</body></html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("./LoginServlet");
    }
}
