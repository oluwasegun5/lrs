package africa.enumverse.lrs.service;

import africa.enumverse.lrs.dto.StatementRequest;
import africa.enumverse.lrs.model.*;
import africa.enumverse.lrs.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;

    public Statement createStatement(StatementRequest request) {
        log.debug("Creating statement for actor: {}", request != null && request.getActor() != null ? request.getActor().getName() : "<none>");

        Statement statement = Statement.builder()
                .actor(mapActor(request.getActor()))
                .verb(mapVerb(request.getVerb()))
                .object(mapActivity(request.getObject()))
                .timestamp(LocalDateTime.now())
                .stored(LocalDateTime.now())
                .context(mapContext(request.getContext()))
                .result(mapResult(request.getResult()))
                .build();

        return statementRepository.save(statement);
    }

    public List<Statement> getAllStatements() {
        log.debug("Fetching all statements");
        return statementRepository.findAll();
    }

    public Optional<Statement> getStatementById(String id) {
        log.debug("Fetching statement by id: {}", id);
        return statementRepository.findById(id);
    }

    public List<Statement> getStatementsByActor(String actorName) {
        log.debug("Fetching statements for actor: {}", actorName);
        return statementRepository.findByActor_Name(actorName);
    }

    public List<Statement> getStatementsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching statements between {} and {}", start, end);
        return statementRepository.findByTimestampBetween(start, end);
    }

    public List<Statement> getStatementsByVerb(String verbId) {
        log.debug("Fetching statements by verb: {}", verbId);
        return statementRepository.findByVerb_Id(verbId);
    }

    public void deleteStatement(String id) {
        log.debug("Deleting statement with id: {}", id);
        statementRepository.deleteById(id);
    }

    private Actor mapActor(StatementRequest.ActorDto dto) {
        if (dto == null) return null;
        Account account = null;
        if (dto.getAccount() != null) {
            account = Account.builder()
                    .name(dto.getAccount().getName())
                    .homePage(dto.getAccount().getHomePage())
                    .build();
        }

        ActorType type = null;
        if (dto.getObjectType() != null) {
            try {
                type = ActorType.valueOf(dto.getObjectType());
            } catch (IllegalArgumentException e) {
                log.warn("Unknown actor objectType '{}', defaulting to Agent", dto.getObjectType());
                type = ActorType.Agent;
            }
        }

        return Actor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .mbox(dto.getMbox())
                .mboxSha1sum(dto.getMboxSha1sum())
                .openId(dto.getOpenId())
                .account(account)
                .objectType(type)
                .build();
    }

    private Verb mapVerb(StatementRequest.VerbDto dto) {
        if (dto == null) return null;
        return Verb.builder()
                .id(dto.getId())
                .display(dto.getDisplay())
                .build();
    }

    private StatementObject mapActivity(StatementRequest.ActivityDto dto) {
        if (dto == null) return null;
        ObjectType objType = null;
        if (dto.getObjectType() != null) {
            try {
                objType = ObjectType.valueOf(dto.getObjectType());
            } catch (IllegalArgumentException e) {
                log.warn("Unknown objectType '{}', defaulting to Activity", dto.getObjectType());
                objType = ObjectType.Activity;
            }
        }

        Definition definition = null;
        if (dto.getDefinition() != null) {
            definition = Definition.builder()
                    .name(dto.getDefinition().getName())
                    .description(dto.getDefinition().getDescription())
                    .type(dto.getDefinition().getType())
                    .moreInfo(dto.getDefinition().getMoreInfo())
                    .extensions(dto.getDefinition().getExtensions())
                    .build();
        }

        return StatementObject.builder()
                .id(dto.getId())
                .objectType(objType)
                .definition(definition)
                .build();
    }

    private Context mapContext(StatementRequest.ContextDto dto) {
        if (dto == null) return null;

        Map<String, List<StatementObject>> ctxActivities = null;
        if (dto.getContextActivities() != null) {
            ctxActivities = dto.getContextActivities().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream().map(this::mapActivity).collect(Collectors.toList())
                    ));
        }

        return Context.builder()
                .registration(dto.getRegistration())
                .instructorId(dto.getInstructorId())
                .teamId(dto.getTeamId())
                .contextActivities(ctxActivities)
                .revision(dto.getRevision())
                .platform(dto.getPlatform())
                .language(dto.getLanguage())
                .statement(dto.getStatement())
                .extensions(dto.getExtensions())
                .build();
    }

    private Result mapResult(StatementRequest.ResultDto dto) {
        if (dto == null) return null;

        Score score = null;
        if (dto.getScore() != null) {
            score = Score.builder()
                    .scaled(dto.getScore().getScaled())
                    .raw(dto.getScore().getRaw())
                    .min(dto.getScore().getMin())
                    .max(dto.getScore().getMax())
                    .build();
        }

        return Result.builder()
                .score(score)
                .success(dto.getSuccess())
                .completion(dto.getCompletion())
                .response(dto.getResponse())
                .duration(dto.getDuration())
                .extensions(dto.getExtensions())
                .build();
    }
}
