package core.mvc;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
  private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

  private RequestMapping requestMapping;

  @Override
  public void init() {
    requestMapping = new RequestMapping();
    requestMapping.initMapping();
  }

  @Override
  public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    String uri = req.getRequestURI();
    logger.debug("Method : {}, URI : {}", req.getMethod(), uri);

    Controller controller = requestMapping.findController(uri);
    try {
      String viewName = controller.execute(req, resp);
      move(viewName, req, resp);
    } catch (Exception e) {
      throw new ServletException(e.getMessage());
    }
  }

  private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
      resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
      return;
    }

    RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
    requestDispatcher.forward(req, resp);
  }
}
