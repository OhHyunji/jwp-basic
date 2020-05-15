package core.ref;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final String TEST_METHOD_PREFIX = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.newInstance();

        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).filter(method -> method.getName().startsWith(TEST_METHOD_PREFIX)).forEach(m -> {
            try {
                m.invoke(junit3Test);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
