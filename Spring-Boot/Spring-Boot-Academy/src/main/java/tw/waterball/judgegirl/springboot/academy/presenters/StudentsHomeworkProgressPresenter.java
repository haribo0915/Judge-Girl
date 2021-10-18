package tw.waterball.judgegirl.springboot.academy.presenters;

import static tw.waterball.judgegirl.commons.utils.StreamUtils.mapToList;

import tw.waterball.judgegirl.academy.domain.usecases.homework.GetStudentsHomeworkProgressUseCase;
import tw.waterball.judgegirl.primitives.Student;
import tw.waterball.judgegirl.primitives.submission.Submission;
import tw.waterball.judgegirl.primitives.submission.verdict.Verdict;
import tw.waterball.judgegirl.springboot.academy.view.ExamHome;
import tw.waterball.judgegirl.springboot.academy.view.ExamView;
import tw.waterball.judgegirl.springboot.academy.view.StudentsHomeworkProgressView;
import tw.waterball.judgegirl.submissionapi.views.SubmissionView;
import tw.waterball.judgegirl.submissionapi.views.VerdictView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sh910913@gmail.com
 */
public class StudentsHomeworkProgressPresenter implements GetStudentsHomeworkProgressUseCase.Presenter {
    private StudentsHomeworkProgressView studentsHomeworkProgressView = new StudentsHomeworkProgressView();

    @Override
    public void showRecord(List<GetStudentsHomeworkProgressUseCase.QuestionRecord> record) {
        var studentProgress = new StudentsHomeworkProgressView.StudentProgress();
        studentsHomeworkProgressView.scoreBoard = new HashMap<>();
        for (int i = 0; i < record.size(); i++) {
            var questionRecord = record.get(i);
            Student student = questionRecord.getStudent();
            studentProgress.setStudentId(student.getId());
            studentProgress.setStudentName(student.getName());
            var objectObjectMap = new HashMap<Integer, Integer>();
            for (int i1 = 0; i1 < questionRecord.getRecord().size(); i1++) {
                SubmissionView submission = questionRecord.getRecord().get(i1);
                int problemId = submission.getProblemId();
                VerdictView verdict = submission.getVerdict();
                objectObjectMap.put(problemId, verdict.getTotalGrade());
            }
            studentProgress.setQuestionScores(objectObjectMap);
            studentsHomeworkProgressView.scoreBoard.put(student.getEmail(), studentProgress);
        }
    }

    public StudentsHomeworkProgressView present() {
        return studentsHomeworkProgressView;
    }

}
