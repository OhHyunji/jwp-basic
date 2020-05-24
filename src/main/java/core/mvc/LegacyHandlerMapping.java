package core.mvc;

import java.util.HashMap;
import java.util.Map;

import core.jdbc.JdbcTemplate;
import core.nmvc.HandlerMapping;
import next.dao.AnswerDao;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.dao.QuestionDao;
import next.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.controller.HomeController;
import next.controller.qna.AddAnswerController;
import next.controller.qna.ApiDeleteQuestionController;
import next.controller.qna.ApiListQuestionController;
import next.controller.qna.CreateFormQuestionController;
import next.controller.qna.CreateQuestionController;
import next.controller.qna.DeleteAnswerController;
import next.controller.qna.DeleteQuestionController;
import next.controller.qna.ShowQuestionController;
import next.controller.qna.UpdateFormQuestionController;
import next.controller.qna.UpdateQuestionController;
import next.controller.user.LoginController;
import next.controller.user.LogoutController;
import next.controller.user.ProfileController;
import next.controller.user.UpdateFormUserController;
import next.controller.user.UpdateUserController;

import javax.servlet.http.HttpServletRequest;

public class LegacyHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, Controller> mappings = new HashMap<>();

    @Override
    public void initialize() {
        QuestionDao questionDao = new JdbcQuestionDao(JdbcTemplate.getInstance());  // TODO JdbcTemplate.getInstance()
        AnswerDao answerDao = new JdbcAnswerDao(JdbcTemplate.getInstance());

        mappings.put("/", new HomeController(questionDao));
        // mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        // mappings.put("/users", new ListUserController());
        mappings.put("/users/login", new LoginController());
        mappings.put("/users/profile", new ProfileController());
        mappings.put("/users/logout", new LogoutController());
        // mappings.put("/users/create", new CreateUserController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());
        QnaService qnaService = new QnaService(questionDao, answerDao);
        mappings.put("/qna/delete", new DeleteQuestionController(qnaService));
        mappings.put("/qna/show", new ShowQuestionController(questionDao, answerDao));
        mappings.put("/qna/form", new CreateFormQuestionController());
        mappings.put("/qna/create", new CreateQuestionController(questionDao));
        mappings.put("/qna/updateForm", new UpdateFormQuestionController(questionDao));
        mappings.put("/qna/update", new UpdateQuestionController(questionDao));
        mappings.put("/api/qna/deleteQuestion", new ApiDeleteQuestionController(qnaService));
        mappings.put("/api/qna/list", new ApiListQuestionController(questionDao));
        mappings.put("/api/qna/addAnswer", new AddAnswerController(questionDao, answerDao));
        mappings.put("/api/qna/deleteAnswer", new DeleteAnswerController(answerDao));

        logger.info("Initialized Request Mapping!");
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String url = request.getRequestURI();
        return mappings.get(url);
    }
}
