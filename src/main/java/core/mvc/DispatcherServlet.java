package core.mvc;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Mod;
import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import core.nmvc.HandlerMapping;
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

    private List<HandlerMapping> handlerMappings = Lists.newArrayList();

    @Override
    public void init() throws ServletException {
        handlerMappings = Lists.newArrayList(new LegacyHandlerMapping(), new AnnotationHandlerMapping("core.nmvc.controller"));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        final Object handler = findHandler(req);

        try {
            ModelAndView modelAndView = execute(handler, req, resp);
            render(req, resp, modelAndView);
        } catch (Throwable e) {
            logger.error("Exception: ", e);
            throw new ServletException(e.getMessage());
        }

        /**
         * 여기까지 통합작업을 완료했으면 기존에 컨트롤러를 annotation 컨트롤러 기반으로 변경한다.
         * 이렇게 통합할 경우 기존 컨트롤러에 대한 지원도 가능하면서
         * 새로 추가한 annotation 컨트롤러 지원도 가능하기 때문에 점진적인 리팩토링이 가능하다.
         */
    }

    private ModelAndView execute(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(handler instanceof AbstractController) {
            AbstractController abstractController = (AbstractController) handler;
            return abstractController.execute(req, resp);
        } else if (handler instanceof HandlerExecution) {
            HandlerExecution handlerExecution = (HandlerExecution) handler;
            return handlerExecution.handle(req, resp);
        } else {
            throw new IllegalStateException("Unsupported handler: " + handler);
        }
    }

    private Object findHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .map(m -> m.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 URL입니다."));
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
