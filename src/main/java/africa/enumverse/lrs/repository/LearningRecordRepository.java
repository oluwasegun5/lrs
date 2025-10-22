package africa.enumverse.lrs.repository;

import africa.enumverse.lrs.model.LearningRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LearningRecordRepository extends MongoRepository<LearningRecord, String> {

    List<LearningRecord> findByUserId(String userId);

    List<LearningRecord> findByCourseId(String courseId);

    List<LearningRecord> findByUserIdAndCourseId(String userId, String courseId);

    List<LearningRecord> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<LearningRecord> findByCompleted(Boolean completed);
}

