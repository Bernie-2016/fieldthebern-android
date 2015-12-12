package com.berniesanders.fieldthebern.models;


import java.util.List;



/**
 * ErrorResponse
 * <p/>
 * JSON Format
 * {
 * "errors": [
 * {
 * "id": "DASHERIZED_CAPITALIZED_ERROR_NAME",
 * "title": "User friendly error name",
 * "detail": "Value of Error.message",
 * "status": "HTTP_CODE_IN_INTEGER_FORMAT"
 * }
 * ]
 * }
 */
public class ErrorResponse {
    public List<Error> errors;

    public static class Error {
        public String id;
        public String title;
        public String detail;
        public int status;

        @Override
        public String toString() {
            return title + " " + detail;
        }
    }


    public String getAllDetails() {
        StringBuilder sb = new StringBuilder();
        for (Error error : errors) {
            sb.append(error.detail);
            sb.append("\n");
        }
        //remove the extra blank line
        sb.delete(sb.lastIndexOf("\n"), sb.length());
        return sb.toString();
    }
}
