package core.ref;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Junit4TestRunner {

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test junit4Test = clazz.newInstance();

        Arrays.stream(clazz.getMethods())
                .filter(m -> Arrays.stream(m.getDeclaredAnnotations()).anyMatch(a ->a.annotationType().equals(MyTest.class)))
                .forEach(m -> {
                    try {
                        m.invoke(junit4Test );
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

    }
}
