package kamerverhuur.servlets;

import kamerverhuur.ContextManager;
import kamerverhuur.Storage;
import kamerverhuur.users.HomeOwner;
import kamerverhuur.users.Tenant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.acl.Owner;
import java.util.ArrayList;

@WebServlet("/ShowPersonsServlet")
public class ShowPersonsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    /**
     * Alle gebruikers in het systeem laten printen en bijhouden (met cookies) hoevaak iemand op de pagina is gekomen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("/login.html");
            return;
        }

        //cookies ophalen
        Cookie[] cookies = request.getCookies();

        Cookie counterCookie = null;
        //loop door cookies om de jusite "cookie" te vinden
        for (Cookie c : cookies) {
            if (c.getName().equals("cookie")) {
                counterCookie = c;
            }
        }
        //Check om de value van de cookie 1 op te hogen
        if (counterCookie != null) {
            String counter = counterCookie.getValue();
            int value = Integer.parseInt(counter);
            value++;
            counter = String.valueOf(value);
            counterCookie.setValue(counter);
            response.addCookie(counterCookie);
        } else {
            counterCookie = new Cookie("cookie", String.valueOf(1));
            response.addCookie(counterCookie);
        }

        //maak een nieuwe pagina aan
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<body>");
        //laat zien hoevaak de gebruiker deze pagina heeft bezocht
        pw.println("U heeft deze site: " + counterCookie.getValue() + " keer bezocht!");
        pw.println("<br>");
        pw.println("De volgende huurders hebben zich geregistreerd: ");
        pw.println("<br>");
        //Loop door alle gebruikers
        for (int i = 0; i < Storage.getInstance().getUsers().size(); i++) {
            //Check of de gebruiker een huurder is
            if (Storage.getInstance().getUsers().get(i) instanceof Tenant) {
                //print de gebruikersnaam van de huurder
                pw.println("<p>" + Storage.getInstance().getUsers().get(i).getUserName() + "</p>");
            }
        }

        pw.println("De volgende verhuurders hebben zich geregistreerd: ");
        //Loop door alle gebruikers
        for (int i = 0; i < Storage.getInstance().getUsers().size(); i++) {
            //Check of de gebruiker een verhuurder is
            if (Storage.getInstance().getUsers().get(i) instanceof HomeOwner) {
                //print de gebruikersnaam van de verhuurder
                pw.println("<p>" + (Storage.getInstance().getUsers().get(i).getUserName()) + "</p>");
            }
        }
        pw.println("<br>");
        //Knop om terug naar de vorige pagina te gaan
        pw.print("<a href = '#' onclick = 'history.go(-1)' >Ga terug</a>");
        pw.print("<FORM METHOD='POST' ACTION='./LoginServlet'><INPUT TYPE='submit' name='extra' VALUE='logout'></FORM>");
        pw.println("</body></html>");
    }
}
