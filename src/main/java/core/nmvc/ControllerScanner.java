package core.nmvc;

import core.annotation.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {
    public static Map<Class<?>, Object> scan() {
        final Reflections reflections = new Reflections("my.project"); // TODO my.project 뭐징
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream().collect(Collectors.toMap(Function.identity(), ControllerScanner::newInstance));
    }

    private static Object newInstance(Class<?> clazz) throws IllegalStateException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to create instance of "+ clazz.getName(), e);
        }
    }
}


