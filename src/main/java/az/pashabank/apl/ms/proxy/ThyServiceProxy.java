package az.pashabank.apl.ms.proxy;

import az.pashabank.apl.ms.constants.URL;
import az.pashabank.apl.ms.model.thy.CheckTkRequest;
import az.pashabank.apl.ms.model.thy.CheckTkRestResponse;
import az.pashabank.apl.ms.model.thy.MemberOperationsRequest;
import az.pashabank.apl.ms.model.thy.MemberOperationsResponse;
import az.pashabank.apl.ms.model.thy.SecurityQuestionsRequest;
import az.pashabank.apl.ms.model.thy.SecurityQuestionsRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-thy", url = "${thy.rest.endpoint}")
public interface ThyServiceProxy {

    @PostMapping(value = URL.THY_POST_GET_MEMBER_DETAILS, consumes = "application/json", produces = "application/json")
    CheckTkRestResponse getMemberDetails(@RequestBody CheckTkRequest request);

    @PostMapping(value = URL.THY_POST_MEMBER_OPERATIONS, consumes = "application/json", produces = "application/json")
    MemberOperationsResponse createMember(@RequestBody MemberOperationsRequest request);

    @PostMapping(value = URL.THY_POST_GET_SPECIFIC_COMBOBOX, consumes = "application/json", produces = "application/json")
    SecurityQuestionsRestResponse getSecurityQuestions(@RequestBody SecurityQuestionsRequest request);

}
