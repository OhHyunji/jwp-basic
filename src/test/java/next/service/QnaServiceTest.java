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
    private QnaService mockQnaService;
    private QuestionDao mockQuestionDao;
    private AnswerDao mockAnswerDao;

    @Before
    public void setUp() {
        mockQuestionDao = new MockQuestionDao();
        mockAnswerDao = new MockAnswerDao();
        mockQnaService = new QnaService(mockQuestionDao, mockAnswerDao);
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestion_notExist_questionId() throws CannotDeleteException {
        mockQnaService.deleteQuestion(1L, newUser("az"));
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestion_anotherUser() throws CannotDeleteException {
        long mockQuestionId = 1;
        Question question = newQuestion(mockQuestionId, "az");
        mockQuestionDao.insert(question);

        mockQnaService.deleteQuestion(mockQuestionId, newUser("olaf"));
    }

    @Test
    public void deleteQuestion_notExist_sameWriter() throws CannotDeleteException {
        long mockQuestionId = 1;
        Question question =  newQuestion(mockQuestionId, "az");
        mockQuestionDao.insert(question);

        mockQnaService.deleteQuestion(mockQuestionId, newUser("az"));
    }

    private Question newQuestion(long questionId, String userId) {
        return new Question(questionId, userId, "title", "contents", new Date(), 0);
    }

    private User newUser(String userId) {
        return new User(userId, "password", "name", "email");
    }
}