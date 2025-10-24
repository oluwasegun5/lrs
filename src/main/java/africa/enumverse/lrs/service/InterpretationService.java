package africa.enumverse.lrs.service;

import africa.enumverse.lrs.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Interprets simplified learning events and converts them to xAPI statements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InterpretationService {

    private static final String BASE_VERB_URI = "http://adlnet.gov/expapi/verbs/";
    private static final String BASE_ACTIVITY_URI = "http://example.com/activities/";
    private static final String BASE_ACTIVITY_TYPE_URI = "http://adlnet.gov/expapi/activities/";
    private static final String BASE_EXTENSION_URI = "http://example.com/extensions/";

    /**
     * Convert simplified learning event to full xAPI statement request
     */
    public StatementRequest interpretLearningEvent(SimplifiedLearningEvent event) {
        log.info("Interpreting learning event: {} - {} - {}",
                event.getLearnerName(), event.getAction(), event.getActivityName());

        // Build Actor
        ActorDto actor = buildActor(event);

        // Build Verb
        VerbDto verb = buildVerb(event.getAction());

        // Build Object (Activity)
        ActivityDto activity = buildActivity(event);

        // Build Result (if applicable)
        ResultDto result = buildResult(event);

        // Build Context
        ContextDto context = buildContext(event);

        return StatementRequest.builder()
                .actor(actor)
                .verb(verb)
                .object(activity)
                .result(result)
                .context(context)
                .build();
    }

    private ActorDto buildActor(SimplifiedLearningEvent event) {
        String actorId = event.getLearnerId() != null ?
                event.getLearnerId() :
                UUID.randomUUID().toString();

        String mbox = event.getLearnerEmail() != null ?
                "mailto:" + event.getLearnerEmail() :
                null;

        return ActorDto.builder()
                .id(actorId)
                .name(event.getLearnerName())
                .mbox(mbox)
                .objectType("Agent")
                .build();
    }

    private VerbDto buildVerb(String action) {
        Map<String, String> display = new HashMap<>();
        String verbId;

        // Map common actions to xAPI verbs
        switch (action.toLowerCase()) {
            case "completed":
            case "finished":
                verbId = BASE_VERB_URI + "completed";
                display.put("en-US", "completed");
                break;
            case "started":
            case "began":
            case "initiated":
                verbId = BASE_VERB_URI + "initialized";
                display.put("en-US", "started");
                break;
            case "passed":
                verbId = BASE_VERB_URI + "passed";
                display.put("en-US", "passed");
                break;
            case "failed":
                verbId = BASE_VERB_URI + "failed";
                display.put("en-US", "failed");
                break;
            case "viewed":
            case "watched":
                verbId = BASE_VERB_URI + "viewed";
                display.put("en-US", "viewed");
                break;
            case "attempted":
                verbId = BASE_VERB_URI + "attempted";
                display.put("en-US", "attempted");
                break;
            case "answered":
                verbId = BASE_VERB_URI + "answered";
                display.put("en-US", "answered");
                break;
            case "scored":
                verbId = BASE_VERB_URI + "scored";
                display.put("en-US", "scored");
                break;
            default:
                verbId = BASE_VERB_URI + action.toLowerCase();
                display.put("en-US", action.toLowerCase());
        }

        return VerbDto.builder()
                .id(verbId)
                .display(display)
                .build();
    }

    private ActivityDto buildActivity(SimplifiedLearningEvent event) {
        String activityId = event.getActivityId() != null ?
                event.getActivityId() :
                BASE_ACTIVITY_URI + UUID.randomUUID().toString();

        Map<String, String> name = new HashMap<>();
        name.put("en-US", event.getActivityName() != null ?
                event.getActivityName() :
                "Learning Activity");

        String activityType = event.getActivityType() != null ?
                BASE_ACTIVITY_TYPE_URI + event.getActivityType() :
                BASE_ACTIVITY_TYPE_URI + "course";

        ActivityDefinitionDto definition = ActivityDefinitionDto.builder()
                .name(name)
                .type(activityType)
                .build();

        return ActivityDto.builder()
                .id(activityId)
                .objectType("Activity")
                .definition(definition)
                .build();
    }

    private ResultDto buildResult(SimplifiedLearningEvent event) {
        // Only build result if there's relevant data
        if (event.getScore() == null &&
            event.getPassed() == null &&
            event.getCompleted() == null &&
            event.getDuration() == null) {
            return null;
        }

        ScoreDto score = null;
        if (event.getScore() != null) {
            // Convert 0-100 score to 0-1 scaled score
            float scaled = event.getScore() / 100.0f;
            score = ScoreDto.builder()
                    .scaled(scaled)
                    .raw(event.getScore())
                    .min(0.0f)
                    .max(100.0f)
                    .build();
        }

        String duration = normalizeDuration(event.getDuration());

        return ResultDto.builder()
                .score(score)
                .success(event.getPassed())
                .completion(event.getCompleted())
                .duration(duration)
                .build();
    }

    private ContextDto buildContext(SimplifiedLearningEvent event) {
        Map<String, Object> extensions = new HashMap<>();

        // Add metadata as extensions
        if (event.getMetadata() != null) {
            event.getMetadata().forEach((key, value) -> {
                extensions.put(BASE_EXTENSION_URI + key, value);
            });
        }

        // Add session info if available
        if (event.getSessionId() != null) {
            extensions.put(BASE_EXTENSION_URI + "sessionId", event.getSessionId());
        }

        return ContextDto.builder()
                .platform(event.getPlatform())
                .instructorId(event.getInstructorId())
                .registration(event.getSessionId())
                .extensions(extensions.isEmpty() ? null : extensions)
                .build();
    }

    /**
     * Normalize duration to ISO 8601 format
     */
    private String normalizeDuration(String duration) {
        if (duration == null) return null;

        // If already in ISO 8601 format (PT...), return as-is
        if (duration.startsWith("PT") || duration.startsWith("P")) {
            return duration;
        }

        try {
            // Try to parse as seconds
            long seconds = Long.parseLong(duration);
            Duration javaDuration = Duration.ofSeconds(seconds);
            return javaDuration.toString();
        } catch (NumberFormatException e) {
            log.warn("Could not parse duration: {}, returning as-is", duration);
            return duration;
        }
    }

    /**
     * Validate that required fields are present
     */
    public boolean validateLearningEvent(SimplifiedLearningEvent event) {
        if (event == null) {
            log.error("Event is null");
            return false;
        }

        if (event.getLearnerName() == null && event.getLearnerId() == null) {
            log.error("Either learnerName or learnerId must be provided");
            return false;
        }

        if (event.getAction() == null || event.getAction().trim().isEmpty()) {
            log.error("Action is required");
            return false;
        }

        if (event.getActivityName() == null && event.getActivityId() == null) {
            log.error("Either activityName or activityId must be provided");
            return false;
        }

        return true;
    }
}
