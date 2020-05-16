package core.nmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private Object instance;
    private Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO: method.invoke(instance);
        return null;
    }
}
