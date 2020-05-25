package next.service;

import next.CannotDeleteException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    @InjectMocks
    private QnaService qnaService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private AnswerDao answerDao;

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestion_notExist_questionId() throws CannotDeleteException {
        long questionId = 1L;
        when(questionDao.findById(questionId)).thenReturn(null);

        qnaService.deleteQuestion(questionId, newUser("az"));
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteQuestion_anotherUser() throws CannotDeleteException {
        long questionId = 1;
        when(questionDao.findById(questionId)).thenReturn(newQuestion(questionId, "az"));

        qnaService.deleteQuestion(questionId, newUser("olaf"));
    }

    @Test
    public void deleteQuestion_ok() throws CannotDeleteException {
        long questionId = 1;
        when(questionDao.findById(questionId)).thenReturn(newQuestion(questionId, "az"));

        qnaService.deleteQuestion(questionId, newUser("az"));
    }


    private Question newQuestion(long questionId, String userId) {
        return new Question(questionId, userId, "title", "contents", new Date(), 0);
    }

    private User newUser(String userId) {
        return new User(userId, "password", "name", "email");
    }
}