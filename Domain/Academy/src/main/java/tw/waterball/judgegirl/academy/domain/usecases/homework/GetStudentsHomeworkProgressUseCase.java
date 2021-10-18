package tw.waterball.judgegirl.academy.domain.usecases.homework;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import tw.waterball.judgegirl.academy.domain.repositories.HomeworkRepository;
import tw.waterball.judgegirl.commons.exceptions.NotFoundException;
import tw.waterball.judgegirl.primitives.Homework;
import tw.waterball.judgegirl.primitives.Student;
import tw.waterball.judgegirl.primitives.submission.Submission;
import tw.waterball.judgegirl.studentapi.clients.StudentServiceDriver;
import tw.waterball.judgegirl.submissionapi.clients.SubmissionServiceDriver;
import tw.waterball.judgegirl.submissionapi.views.SubmissionView;

import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author - sh910913@gmail.com(gordon.liao)
 */
@Named
public class GetStudentsHomeworkProgressUseCase extends AbstractHomeworkUseCase {

    private final StudentServiceDriver studentServiceDriver;
    private final SubmissionServiceDriver submissionServiceDriver;

    public GetStudentsHomeworkProgressUseCase(HomeworkRepository homeworkRepository, StudentServiceDriver studentServiceDriver, SubmissionServiceDriver submissionServiceDriver) {
        super(homeworkRepository);
        this.studentServiceDriver = studentServiceDriver;
        this.submissionServiceDriver = submissionServiceDriver;
    }

    public void execute(Request request, Presenter presenter)
        throws NotFoundException {
        var homework = findHomework(request.homeworkId);
        var students = findStudents(request);
        var questionRecords = new ArrayList<QuestionRecord>();
        for (Student student : students) {
            questionRecords.add(showBestRecords(student, homework));
        }
        presenter.showRecord(questionRecords);
    }

    private List<Student> findStudents(Request request) {
        return studentServiceDriver.getStudentsByEmails(request.emails);
    }

    private QuestionRecord showBestRecords(Student student, Homework homework) {
        List<SubmissionView> collect = homework.getProblemIds().stream()
            .flatMap(problemId -> findBestRecord(student.getId(), problemId).stream()).collect(Collectors.toList());
        QuestionRecord questionRecord = new QuestionRecord(student, collect);
        return questionRecord;
    }

    private Optional<SubmissionView> findBestRecord(int studentId, int problemId) {
        try {
            return Optional.of(submissionServiceDriver.findBestRecord(problemId, studentId));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    public interface Presenter {

        void showRecord(List<QuestionRecord> record);

    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        public int homeworkId;
        public List<String> emails;
    }

    @Value
    public static class QuestionRecord {
        Student student;
        List<SubmissionView> record;

        public QuestionRecord(Student student, List<SubmissionView> record) {
            this.student = student;
            this.record = record;
        }
    }
}
