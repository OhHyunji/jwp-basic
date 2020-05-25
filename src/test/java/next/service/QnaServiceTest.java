package next.service;

import next.CannotDeleteException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class QnaServiceTest {
    private QnaService qnaService;
    private QuestionDao questionDao;
    private AnswerDao answerDao;

    @Before
    public void setUp() {
        questionDao = new MockQuestionDao();
        answerDao = new MockAnswerDao();
        qnaService = new QnaService(questionDao, answerDao);
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestion_notExist_questionId() throws CannotDeleteException {
        qnaService.deleteQuestion(1L, newUser("az"));
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestion_anotherUser() throws CannotDeleteException {
        long mockQuestionId = 1;
        Question question = newQuestion(mockQuestionId, "az");
        questionDao.insert(question);

        qnaService.deleteQuestion(mockQuestionId, newUser("olaf"));
    }

    @Test
    public void deleteQuestion_notExist_sameWriter() throws CannotDeleteException {
        long mockQuestionId = 1;
        Question question =  newQuestion(mockQuestionId, "az");
        questionDao.insert(question);

        qnaService.deleteQuestion(mockQuestionId, newUser("az"));
    }

    private Question newQuestion(long questionId, String userId) {
        return new Question(questionId, userId, "title", "contents", new Date(), 0);
    }

    private User newUser(String userId) {
        return new User(userId, "password", "name", "email");
    }
}