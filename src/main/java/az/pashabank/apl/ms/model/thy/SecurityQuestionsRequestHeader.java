package az.pashabank.apl.ms.model.thy;


import java.util.StringJoiner;

public class SecurityQuestionsRequestHeader {

    private String languageCode;

    public SecurityQuestionsRequestHeader() {
    }

    public SecurityQuestionsRequestHeader(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SecurityQuestionsRequestHeader.class.getSimpleName() + "[", "]")
                .add("languageCode='" + languageCode + "'")
                .toString();
    }
}
