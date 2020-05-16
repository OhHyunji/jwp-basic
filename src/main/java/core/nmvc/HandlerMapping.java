package core.nmvc;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link core.mvc.DispatcherServlet} 에서
 * {@link core.mvc.LegacyHandlerMapping}, {@link AnnotationHandlerMapping} 를 통합해서 지원하기 위한 인터페이스
 */
public interface HandlerMapping {
    void initialize();
    Object getHandler(HttpServletRequest request);
}
