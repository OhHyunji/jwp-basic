package next.service;

import com.google.common.collect.Maps;
import next.dao.QuestionDao;
import next.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockQuestionDao implements QuestionDao {
    private final Map<Long, Question> questions = Maps.newHashMap();

    @Override
    public Question insert(Question question) {
        return questions.put(question.getQuestionId(), question);
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(questions.values());
    }

    @Override
    public Question findById(long questionId) {
        return questions.get(questionId);
    }

    @Override
    public void update(Question question) {
        questions.put(question.getQuestionId(), question);
    }

    @Override
    public void delete(long questionId) {
        questions.remove(questionId);
    }

    @Override
    public void updateCountOfAnswer(long questionId) {
        Question prev = questions.get(questionId);
        Question question = new Question(questionId, prev.getWriter(), prev.getTitle(), prev.getContents(), prev.getCreatedDate(), prev.getCountOfAnswer() + 1);
        questions.put(questionId, question);
    }
}