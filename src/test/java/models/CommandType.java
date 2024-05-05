package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CommandType {

    @JsonProperty("getVersion")
    GET_VERSION("getVersion"),
    @JsonProperty("getInterval")
    GET_INTERVAL("getInterval"),
    @JsonProperty("setValues")
    SET_VALUES("setValues");

    private String value;

    CommandType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
