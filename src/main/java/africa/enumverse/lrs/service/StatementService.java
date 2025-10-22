package africa.enumverse.lrs.service;

import africa.enumverse.lrs.dto.*;
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

    public StatementResponse createStatement(StatementRequest request) {
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

        Statement saved = statementRepository.save(statement);
        return mapToResponse(saved);
    }

    public List<StatementResponse> getAllStatements() {
        log.debug("Fetching all statements");
        return statementRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<StatementResponse> getStatementById(String id) {
        log.debug("Fetching statement by id: {}", id);
        return statementRepository.findById(id)
                .map(this::mapToResponse);
    }

    public List<StatementResponse> getStatementsByActor(String actorName) {
        log.debug("Fetching statements for actor: {}", actorName);
        return statementRepository.findByActor_Name(actorName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<StatementResponse> getStatementsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching statements between {} and {}", start, end);
        return statementRepository.findByTimestampBetween(start, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<StatementResponse> getStatementsByVerb(String verbId) {
        log.debug("Fetching statements by verb: {}", verbId);
        return statementRepository.findByVerb_Id(verbId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteStatement(String id) {
        log.debug("Deleting statement with id: {}", id);
        statementRepository.deleteById(id);
    }

    // Request DTO to Model mappers
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

    // Model to Response DTO mappers
    private StatementResponse mapToResponse(Statement statement) {
        if (statement == null) return null;
        return StatementResponse.builder()
                .id(statement.getId())
                .actor(mapActorToResponse(statement.getActor()))
                .verb(mapVerbToResponse(statement.getVerb()))
                .object(mapStatementObjectToResponse(statement.getObject()))
                .timestamp(statement.getTimestamp())
                .stored(statement.getStored())
                .authority(mapActorToResponse(statement.getAuthority()))
                .version(statement.getVersion())
                .attachments(statement.getAttachments())
                .result(mapResultToResponse(statement.getResult()))
                .context(mapContextToResponse(statement.getContext()))
                .build();
    }

    private ActorResponse mapActorToResponse(Actor actor) {
        if (actor == null) return null;
        return ActorResponse.builder()
                .id(actor.getId())
                .name(actor.getName())
                .mbox(actor.getMbox())
                .mboxSha1sum(actor.getMboxSha1sum())
                .openId(actor.getOpenId())
                .account(mapAccountToResponse(actor.getAccount()))
                .objectType(actor.getObjectType() != null ? actor.getObjectType().name() : null)
                .build();
    }

    private AccountResponse mapAccountToResponse(Account account) {
        if (account == null) return null;
        return AccountResponse.builder()
                .name(account.getName())
                .homePage(account.getHomePage())
                .build();
    }

    private VerbResponse mapVerbToResponse(Verb verb) {
        if (verb == null) return null;
        return VerbResponse.builder()
                .id(verb.getId())
                .display(verb.getDisplay())
                .build();
    }

    private StatementObjectResponse mapStatementObjectToResponse(StatementObject object) {
        if (object == null) return null;
        return StatementObjectResponse.builder()
                .id(object.getId())
                .objectType(object.getObjectType() != null ? object.getObjectType().name() : null)
                .definition(mapDefinitionToResponse(object.getDefinition()))
                .build();
    }

    private DefinitionResponse mapDefinitionToResponse(Definition definition) {
        if (definition == null) return null;
        return DefinitionResponse.builder()
                .name(definition.getName())
                .description(definition.getDescription())
                .type(definition.getType())
                .moreInfo(definition.getMoreInfo())
                .extensions(definition.getExtensions())
                .build();
    }

    private ResultResponse mapResultToResponse(Result result) {
        if (result == null) return null;
        return ResultResponse.builder()
                .score(mapScoreToResponse(result.getScore()))
                .success(result.getSuccess())
                .completion(result.getCompletion())
                .response(result.getResponse())
                .duration(result.getDuration())
                .extensions(result.getExtensions())
                .build();
    }

    private ScoreResponse mapScoreToResponse(Score score) {
        if (score == null) return null;
        return ScoreResponse.builder()
                .scaled(score.getScaled())
                .raw(score.getRaw())
                .min(score.getMin())
                .max(score.getMax())
                .build();
    }

    private ContextResponse mapContextToResponse(Context context) {
        if (context == null) return null;

        Map<String, List<StatementObjectResponse>> ctxActivities = null;
        if (context.getContextActivities() != null) {
            ctxActivities = context.getContextActivities().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream()
                                    .map(this::mapStatementObjectToResponse)
                                    .collect(Collectors.toList())
                    ));
        }

        return ContextResponse.builder()
                .registration(context.getRegistration())
                .instructorId(context.getInstructorId())
                .teamId(context.getTeamId())
                .contextActivities(ctxActivities)
                .revision(context.getRevision())
                .platform(context.getPlatform())
                .language(context.getLanguage())
                .statement(context.getStatement())
                .extensions(context.getExtensions())
                .build();
    }
}
