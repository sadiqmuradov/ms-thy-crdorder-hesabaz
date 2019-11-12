package az.pashabank.apl.ms.model.thy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

public class SecurityQuestion {

    private String code;
    @JsonProperty("name")
    private String question;

    public SecurityQuestion() {
    }

    public SecurityQuestion(String code, String question) {
        this.code = code;
        this.question = question;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SecurityQuestion.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("question='" + question + "'")
                .toString();
    }
}
