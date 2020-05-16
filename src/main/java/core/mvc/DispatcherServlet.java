package core.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xpath.internal.operations.Mod;
import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 이번 장에서 MVC 프레임워크를 개선하고나면
 * 기존에 구현되어있던 컨트롤러를 annotation 기반으로 변경해야한다.
 *
 * 그런데 새로운 MVC 프레임워크를 적용하는동안 새로운 기능을 추가하거나 변경할 수 없으면 안되니까..
 * 점진적으로 적용이 가능한 구조로 개발해야한다.
 *
 * 점진적으로 적용 가능하려면
 * 1) 먼저 annotation 기반으로 MVC 프레임워크를 구현한 후
 * 2) 기존 MVC 프레임워크와 annotation 기반 MVC 프레임워크를 통하는 방식으로 구현해야한다.
 */
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() throws ServletException {
        // TODO[az] HandlerMapping List 초기화

        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO[az] HandlerMapping List 에서 요청 URL에 해당하는 컨트롤러를 찾아 메소드를 실행
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            // legacy
            Controller controller = legacyHandlerMapping.findController(req.getRequestURI());
            if(controller != null) {
                render(req, resp, controller.execute(req, resp));
            }

            // annotation
            HandlerExecution handler = annotationHandlerMapping.getHandler(req);
            render(req, resp, handler.handle(req, resp));
        } catch (Throwable e) {
            logger.error("Exception: ", e);
            throw new ServletException(e.getMessage());
        }

        // TODO[az] 기존 컨트롤러를 새로 추가한 애노테이션 기반으로 설정 후
        // 정상적으로 동작하는지 테스트
        // 테스트에 성공하면 기존 컨틀로러를 새로운 mVC 프레임워크로 점진적으로 변경
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
