package az.pashabank.apl.ms.model.thy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

public class SecurityQuestionsRestResponse {

    private ResponseHeader responseHeader;
    @JsonProperty("return")
    private SecurityQuestionsReturn securityQuestionsReturn;

    public SecurityQuestionsRestResponse() {
    }

    public SecurityQuestionsRestResponse(SecurityQuestionsReturn securityQuestionsReturn) {
        this.securityQuestionsReturn = securityQuestionsReturn;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public SecurityQuestionsReturn getSecurityQuestionsReturn() {
        return securityQuestionsReturn;
    }

    public void setSecurityQuestionsReturn(SecurityQuestionsReturn securityQuestionsReturn) {
        this.securityQuestionsReturn = securityQuestionsReturn;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SecurityQuestionsRestResponse.class.getSimpleName() + "[", "]")
                .add("responseHeader=" + responseHeader)
                .add("securityQuestionsReturn=" + securityQuestionsReturn)
                .toString();
    }
}
