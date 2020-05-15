package core.nmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.JspView;
import core.mvc.ModelAndView;

/**
 * 1.
 * 9장에서 만들었던 MVC 프레임워크에 아쉬운점이 있다.
 * - 새로운 컨트롤러가 추가될때마다 {@link core.mvc.RequestMapping} 클래스에 요청 URL과 컨트롤러를 추가해야함. (귀찮음)
 * - URL 맵핑할 때 HTTP 메소드도 같이 맵핑하면 좋을것같음.
 *
 * 아래 처럼 개선되면 좋을것 같다.
 * - URL 마다 컨트롤러를 만들어서 맵핑하는게 아니라, 메소드만 추가
 * - (URL, HTTP 메소드)로 선언.
 */

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView(new JspView("/users/list.jsp"));
    }

    @RequestMapping(value = "/users/show", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView(new JspView("/users/show.jsp"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users create");
        return new ModelAndView(new JspView("redirect:/users"));
    }
}