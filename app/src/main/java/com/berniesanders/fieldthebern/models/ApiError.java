package com.berniesanders.fieldthebern.models;

import java.util.Map;

/**
 *
 */
public class ApiError {
    Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }
}

/*
{
  "errors": [
    {
      "id": "DASHERIZED_CAPITALIZED_ERROR_NAME",
      "title": "User friendly error name",
      "detail": "Value of Error.message",
      "status": "HTTP_CODE_IN_INTEGER_FORMAT"
    }
  ]
}
 */
