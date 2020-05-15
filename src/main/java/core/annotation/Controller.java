package core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * reflections(https://github.com/ronmamo/reflections)을 활용해
 * @Controller 이 설정되어있는 클래스를 찾은 후
 * @RequestMapping 설정에 따라 요청 URL과 HTTP 메소드를 연결할 수 있도록 구현할 수 있다.
 *
 * 이 요구사항을 구현하려면 자바 리플렉션을 활용해야하므로,
 * 리플렉션 먼저 연습해보고 (10.1.2)
 * MVC 프레임워크 구현 다음단계로 넘어간다. (10.1.3)
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";
}
