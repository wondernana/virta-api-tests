package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// makes fields that were not explicitly set in tests non-serializable
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StationTestRequest {

    @JsonProperty
    private CommandType command;
    @JsonProperty
    private int payload;

    public StationTestRequest() {}

    public StationTestRequest withCommand(CommandType command) {
        this.command = command;
        return this;
    }

    public StationTestRequest withPayload(int payload) {
        this.payload = payload;
        return this;
    }

    public CommandType command() {
        return this.command;
    }

    public int payload() {
        return this.payload;
    }

}
