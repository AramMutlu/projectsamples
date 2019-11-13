package kamerverhuur.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import kamerverhuur.Storage;
import kamerverhuur.exception.UserException;
import kamerverhuur.users.HomeOwner;
import kamerverhuur.users.Tenant;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private Storage storage;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        storage = Storage.getInstance();
        //check om te kijken of er ingelogd of geregistreerd wordt.
        if (request.getParameter("extra").equals("login")) {
            login(request, response);
        } else if (request.getParameter("extra").equals("register")) {
            register(request, response);
        } else if (request.getParameter("extra").equals("logout")){
            request.getSession().setAttribute("user", null);
            response.sendRedirect("./login.html");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") instanceof HomeOwner){
            response.sendRedirect("./ShowRoomsServlet");
        } else if (request.getSession().getAttribute("user") instanceof Tenant){
            request.getRequestDispatcher("./WEB-INF/huurder.html").forward(request, response);
        } else {
            response.sendRedirect("./login.html");
        }
    }

    /**
     * Methode om te checken of de gebruiker de juiste gegevens invult om in te loggen
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //session ophalen
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //checken of de inlog gegevens correct zijn
        if (Storage.getInstance().isValidLogin(username, password)) {
            session.setAttribute("user", storage.getUserByName(username));

            //check of het om een verhuurder of huurder gaat en redirect ze naar de juiste pagina
            if (Storage.getInstance().getUserByName(username) instanceof Tenant) {
                request.getRequestDispatcher("./WEB-INF/huurder.html").forward(request, response);
            } else if (Storage.getInstance().getUserByName(username) instanceof HomeOwner) {
                response.sendRedirect("./ShowRoomsServlet");
            }
        }else{
        //Mochten de inlog gegevens incorrect zijn
            request.getRequestDispatcher("./WEB-INF/fouteinlog.html").forward(request, response);
        }

    }

    /**
     * De registreer methode is ervoor om een nieuwe verhuurder/huurder te registeren in het systeem.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        //een check om te kijken of de ingevulde wachtwoorden overeenkomen
        if (!password1.equals(password2)) {
            //mocht dit niet zo zijn, maak dan een nieuwe pagina aan met een melding
            response.getWriter().print("<html>" +
                    "<body>" +
                    "wachtwoorden komen niet overeen" +
                    "</br>" +
                    "<a href=\"./registreer.html\">Probeer opnieuw</a>" +
                    "</body>" +
                    "</html>");
        }

        //De nieuwe gebruiker toevoegen aan het systeem.
        try {
            if (request.getParameter("userType").equals("huurder")) {
                storage.addUser(new Tenant(request.getParameter("username"), request.getParameter("password1")));
            } else if (request.getParameter("userType").equals("verhuurder")) {
                storage.addUser(new HomeOwner(request.getParameter("username"), request.getParameter("password1")));
            }
            request.getRequestDispatcher("./login.html").forward(request, response);
        } catch (UserException ue) {
            System.out.println("Gebruiker bestaat al...");
            request.getRequestDispatcher("./register.html").forward(request, response);
        }
    }
}
