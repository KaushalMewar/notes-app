package io.km.notes.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ErrorResponse {

    // Getters and Setters
    private List<Error> errors;

    // Constructors
    public ErrorResponse(List<Error> errors) {
        this.errors = errors;
    }

    @Setter
    @Getter
    public static class Error {
        // Getters and Setters
        private String code;
        private String title;
        private String detail;

        // Constructors
        public Error(String code, String title, String detail) {
            this.code = code;
            this.title = title;
            this.detail = detail;
        }

    }
}
