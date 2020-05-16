package core.ref;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.model.Question;
import next.model.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    private static final String SEPARATOR = ",";

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug("########## {} Info ##########", clazz.getName());

        logger.debug("<Fields:*>");
        logger.debug(StringUtils.join(toFieldNames(clazz.getDeclaredFields()), SEPARATOR));
        logger.debug("<Fields:public>");
        logger.debug(StringUtils.join(toFieldNames(clazz.getFields()), SEPARATOR));

        logger.debug("<Constructors:*>");
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(c -> logger.debug(c.toString()));
        logger.debug("<Constructors:public>");
        Arrays.stream(clazz.getConstructors()).forEach(c -> logger.debug(c.toString()));

        logger.debug("<Methods:*>");
        Arrays.stream(clazz.getDeclaredMethods()).forEach(c -> logger.debug(c.toString()));
        logger.debug("<Methods:public>");
        Arrays.stream(clazz.getMethods()).forEach(c -> logger.debug(c.toString()));
    }
    
    @Test
    public void newInstanceWithConstructorArgs() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<User> clazz = User.class;

        final Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        final int allArgsCount = clazz.getDeclaredFields().length;
        final Constructor<?> allArgsConstructor = Arrays.stream(declaredConstructors)
                .filter(c -> c.getParameterCount() == allArgsCount)
                .findFirst().orElseThrow(IllegalStateException::new);

        final User user = (User) allArgsConstructor.newInstance("userId", "password", "userName", "user@gmail.com");

        logger.debug("########## {} Instance ##########", clazz.getName());
        logger.debug(user.toString());
    }
    
    @Test
    public void privateFieldAccess() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<Student> clazz = Student.class;
        final Student student = clazz.newInstance();

        final Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "아젤리아");
        final Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.setInt(student, 29);

        logger.debug(student.toString());
    }

    private List<String> toFieldNames(Field[] fields) {
        return Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
    }
}
