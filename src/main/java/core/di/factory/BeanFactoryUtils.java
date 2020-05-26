package core.di.factory;

import static org.reflections.ReflectionUtils.getAllConstructors;
import static org.reflections.ReflectionUtils.withAnnotation;

import com.google.common.collect.Sets;
import core.annotation.Inject;

import java.lang.reflect.Constructor;
import java.util.Set;

public class BeanFactoryUtils {

    /**
     * 인자로 전달하는 클래스의 생성자 중 {@link Inject} 애노테이션이 설정되어있는 생성자를 반환.
     * - {@link Inject} 애노테이션이 설정되어있는 생성자는 클래스당 하나로 가정한다.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = getAllConstructors(clazz, withAnnotation(Inject.class));
        if(injectedConstructors.isEmpty()) {
            return null;
        }

        return injectedConstructors.iterator().next();
    }

    /**
     * 인자로 전달되는 클래스의 구현클래스를 찾는다.
     * - injectedClazz.isInterface(): 전달받은 인자 자체가 구현클래스 -> 바로 반환.
     * - !injectedClazz.isInterface(): {@link BeanFactory}가 관리하는 모든 클래스 중 인터페이스를 구현하는 클래스를 찾아 반환.
     */
    public static Class<?> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstantiateBeans) {
        if(!injectedClazz.isInterface()) {
            return injectedClazz;
        }

        for(Class<?> clazz : preInstantiateBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
            if(interfaces.contains(injectedClazz)) {
                return clazz;
            }
        }

        throw new IllegalStateException(injectedClazz + "인터페이스를 구현하는 Bean이 존재하지 않는다.");
    }
}
