package africa.enumverse.lrs.repository;

import africa.enumverse.lrs.model.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatementRepository extends MongoRepository<Statement, String> {

    List<Statement> findByActor_Name(String actorName);

    List<Statement> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<Statement> findByVerb_Id(String verbId);
}
