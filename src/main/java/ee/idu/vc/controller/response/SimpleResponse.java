package ee.idu.vc.controller.response;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

public class SimpleResponse implements JsonResponse {
    private Set<String> errorFields = new HashSet<>();
    private Set<String> errorMessages = new HashSet<>();

    public SimpleResponse() {}

    public SimpleResponse(String errorField, String errorMessage) {
        addError(errorField, errorMessage);
    }

    public SimpleResponse(String errorMessage) {
        addErrorMessage(errorMessage);
    }

    public SimpleResponse(BindingResult result) {
        for (FieldError fieldError : result.getFieldErrors()) {
            addErrorField(fieldError.getField());
            addErrorMessage(fieldError.getDefaultMessage());
        }
        List<String> errorFields = new ArrayList<>(result.getFieldErrors().size());
        List<String> errorMessages = new ArrayList<>(result.getFieldErrors().size());
    }

    public void addErrorField(String fieldName) {
        errorFields.add(fieldName);
    }

    public void addErrorFields(String ... fieldNames) {
        for (String fieldName : fieldNames) addErrorField(fieldName);
    }

    public void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    public void addErrorMessages(String ... messages) {
        for (String message : messages) addErrorMessage(message);
    }

    public void addError(String fieldName, String message) {
        addErrorField(fieldName);
        addErrorMessage(message);
    }

    public boolean hasErrors() {
        return errorFields.size() > 0 || errorMessages.size() > 0;
    }

    public Set<String> getErrorFields() {
        return errorFields;
    }

    public Set<String> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public boolean success() {
        return !hasErrors();
    }
}