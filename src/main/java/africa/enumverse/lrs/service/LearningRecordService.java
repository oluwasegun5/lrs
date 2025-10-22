package africa.enumverse.lrs.service;

import africa.enumverse.lrs.dto.LearningRecordRequest;
import africa.enumverse.lrs.exception.ResourceNotFoundException;
import africa.enumverse.lrs.model.LearningRecord;
import africa.enumverse.lrs.repository.LearningRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearningRecordService {

    private final LearningRecordRepository learningRecordRepository;

    public LearningRecord createLearningRecord(LearningRecordRequest request) {
        log.debug("Creating learning record for user: {} and course: {}", request.getUserId(), request.getCourseId());

        LearningRecord record = LearningRecord.builder()
                .userId(request.getUserId())
                .courseId(request.getCourseId())
                .activityType(request.getActivityType())
                .activityName(request.getActivityName())
                .score(request.getScore())
                .completed(request.getCompleted())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationMinutes(request.getDurationMinutes())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        return learningRecordRepository.save(record);
    }

    public List<LearningRecord> getAllLearningRecords() {
        log.debug("Fetching all learning records");
        return learningRecordRepository.findAll();
    }

    public Optional<LearningRecord> getLearningRecordById(String id) {
        log.debug("Fetching learning record by id: {}", id);
        return learningRecordRepository.findById(id);
    }

    public List<LearningRecord> getLearningRecordsByUserId(String userId) {
        log.debug("Fetching learning records for user: {}", userId);
        return learningRecordRepository.findByUserId(userId);
    }

    public List<LearningRecord> getLearningRecordsByCourseId(String courseId) {
        log.debug("Fetching learning records for course: {}", courseId);
        return learningRecordRepository.findByCourseId(courseId);
    }

    public List<LearningRecord> getLearningRecordsByUserAndCourse(String userId, String courseId) {
        log.debug("Fetching learning records for user: {} and course: {}", userId, courseId);
        return learningRecordRepository.findByUserIdAndCourseId(userId, courseId);
    }

    public List<LearningRecord> getCompletedRecords(Boolean completed) {
        log.debug("Fetching completed learning records: {}", completed);
        return learningRecordRepository.findByCompleted(completed);
    }

    public LearningRecord updateLearningRecord(String id, LearningRecordRequest request) {
        log.debug("Updating learning record with id: {}", id);

        LearningRecord record = learningRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning record", id));

        record.setUserId(request.getUserId());
        record.setCourseId(request.getCourseId());
        record.setActivityType(request.getActivityType());
        record.setActivityName(request.getActivityName());
        record.setScore(request.getScore());
        record.setCompleted(request.getCompleted());
        record.setStartTime(request.getStartTime());
        record.setEndTime(request.getEndTime());
        record.setDurationMinutes(request.getDurationMinutes());
        record.setStatus(request.getStatus());
        record.setUpdatedAt(LocalDateTime.now());

        return learningRecordRepository.save(record);
    }

    public void deleteLearningRecord(String id) {
        log.debug("Deleting learning record with id: {}", id);

        LearningRecord record = learningRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Learning record", id));

        learningRecordRepository.delete(record);
    }
}
