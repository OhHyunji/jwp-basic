package core.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> preInstantiateBeans;
    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for(Class<?> clazz : preInstantiateBeans) {
            if(Objects.isNull(beans.get(clazz))) {
                instantiateClass(clazz);
            }
        }
    }

    /**
     * instantiateClass, instantiateConstructor 두 메소드의 재귀호출을 통해 복잡한 의존관계에 있는 빈을 생성한다.
     */

    private Object instantiateClass(Class<?> clazz) {
        log.debug("########## instantiateClass ##########");
        Object bean = beans.get(clazz);
        if(Objects.nonNull(bean)) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if(Objects.isNull(injectedConstructor)) {
            bean = BeanUtils.instantiate(clazz);
            beans.put(clazz, bean);
            return bean;
        }

        log.debug("Constructor: {}", injectedConstructor);
        bean = instantiateConstructor(injectedConstructor);
        beans.put(clazz, bean);
        return bean;
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        log.debug("########## instantiateConstructor ##########");
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();

        for(Class<?> clazz : parameterTypes) {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
            if(!preInstantiateBeans.contains(concreteClass)) {
                throw new IllegalStateException(clazz + "는 Bean이 아닙니다.");
            }

            Object bean = beans.get(concreteClass);
            if(Objects.isNull(bean)) {
                bean = instantiateClass(concreteClass);
            }
            args.add(bean);
        }

        return BeanUtils.instantiateClass(constructor, args.toArray());
    }
}
