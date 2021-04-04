package tw.waterball.judgegirl.entities.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@AllArgsConstructor
public class Answer {
    private final Answer.Id id;
    private final String submissionId;
    private final Date answerTime;

    public Answer(Id id, String submissionId) {
        this.id = id;
        this.submissionId = submissionId;
        answerTime = new Date();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Id {
        private Integer number;
        private final Question.Id questionId;
        private final int studentId;

        public Id(Question.Id questionId, int studentId) {
            this.questionId = questionId;
            this.studentId = studentId;
        }
    }

    public int getNumber() {
        return getId().getNumber();
    }

    public int getExamId() {
        return getId().questionId.getExamId();
    }

    public int getProblemId() {
        return getId().questionId.getProblemId();
    }

    public int getStudentId() {
        return getId().studentId;
    }

}
