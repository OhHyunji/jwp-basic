package core.nmvc;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * step1. @Controller 붙어있는 클래스들을 찾아서
     * step2. (url, HTTP method) 요청에 적합한 method를 호출하도록 Map<HandlerKey, HandlerExecution> handlerExecutions 을 채운다.
     *  - HandlerKey: url, HTTP method
     *  - HandlerExecution: 실행할 method
     */
    @Override
    public void initialize() {
        final Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();

        controllers.entrySet().stream()
                .flatMap(this::toRequestMappingElements)
                .forEach(r -> handlerExecutions.put(r.createHandlerKey(), r.createHandlerExecution()));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }

    private Stream<_RequestMappingElement> toRequestMappingElements(Map.Entry<Class<?>, Object> entry) {

        final Class<?> controller = entry.getKey();
        final Object controllerInstance = entry.getValue();

        return Arrays.stream(controller.getMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .map(m -> new _RequestMappingElement(controllerInstance, m, m.getAnnotation(RequestMapping.class)));
    }

    private class _RequestMappingElement {
        private Object instance;
        private Method method;
        private RequestMapping requestMapping;

        public _RequestMappingElement(Object instance, Method method, RequestMapping requestMapping) {
            this.instance = instance;
            this.method = method;
            this.requestMapping = requestMapping;
        }

        public HandlerKey createHandlerKey() {
            return new HandlerKey(requestMapping.value(), requestMapping.method());
        }

        public HandlerExecution createHandlerExecution() {
            return new HandlerExecution(instance,  method);
        }
    }
}
