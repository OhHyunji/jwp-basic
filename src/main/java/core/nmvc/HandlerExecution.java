package core.nmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;

/**
 * 값에 저장되는 method 정보는 자바 리플렉션으로 해당 메소드를 실행할 수 있어야한다.
 * ex) method.invoke(instance)
 */
public class HandlerExecution {
    private Object instance;
    private Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
