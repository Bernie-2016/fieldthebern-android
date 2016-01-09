package com.berniesanders.fieldthebern.models;

import java.util.List;

/**
 *
 */
public class StatePrimaryResponse {

    private List<StatePrimary> data;

    public List<StatePrimary> getData() {
        return data;
    }

    public void setData(List<StatePrimary> data) {
        this.data = data;
    }

    public class StatePrimary {

        private String state;
        private String type;
        private String status;
        private String only_17;
        private String date;
        private String deadline;
        private String details;
        private String code;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOnly_17() {
            return only_17;
        }

        public void setOnly_17(String only_17) {
            this.only_17 = only_17;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
