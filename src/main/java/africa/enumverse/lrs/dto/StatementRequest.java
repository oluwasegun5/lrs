package africa.enumverse.lrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementRequest {

    private ActorDto actor;
    private VerbDto verb;
    private ActivityDto object;
    private ContextDto context;
    private ResultDto result;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActorDto {
        private String id; // UUID
        private String name;
        private String mbox; // mailto: URI
        private String mboxSha1sum;
        private String openId;
        private AccountDto account;
        private String objectType; // Agent | Group

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AccountDto {
            private String name;
            private String homePage;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerbDto {
        private String id; // URI
        private Map<String, String> display; // language map
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityDto {
        private String id; // URI
        private String objectType; // Activity|Agent|Group|SubStatement|StatementRef
        private ActivityDefinitionDto definition;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityDefinitionDto {
        private Map<String, String> name; // language map
        private Map<String, String> description;
        private String type; // URI
        private String moreInfo; // URI
        private Map<String, Object> extensions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextDto {
        private String registration;
        private String instructorId;
        private String teamId;
        private Map<String, List<ActivityDto>> contextActivities;
        private String revision;
        private String platform;
        private String language;
        private String statement; // statement id
        private Map<String, Object> extensions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultDto {
        private ScoreDto score;
        private Boolean success;
        private Boolean completion;
        private String response;
        private String duration; // ISO8601
        private Map<String, Object> extensions;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ScoreDto {
            private Float scaled;
            private Float raw;
            private Float min;
            private Float max;
        }
    }
}
