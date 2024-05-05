package tests.functional;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import clients.StationAPIClient;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.CommandType;
import models.SetResult;
import models.StationGetIntervalResponse;
import models.StationGetVersionResponse;
import models.StationSetValuesResponse;
import models.StationTestRequest;

public class NegativeStationAPITests {

    private final StationAPIClient client = new StationAPIClient();

    @Feature("GET_VERSION")
    @DisplayName("Should Get Empty Version For Non-Existing Station.")
    @Description("1. Send POST request with 'getVersion' command to a non-existing station id (0, negative and positive).\n 2. Expect 200 response of StationGetVersionResponse type and that contains empty result.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @ValueSource(ints = {-1, 0, 6})
    public void shouldGetEmptyVersionResponseForNonExistingStations(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.GET_VERSION);
        StationGetVersionResponse response = client.getStationVersionAndValidateResponse(stationId, request);

        assertThat(response.result(), is(emptyOrNullString()));
    }

    @Feature("GET_INTERVAL")
    @DisplayName("Should Get Empty Interval For Non-Existing Station.")
    @Description("1. Send POST request with 'getInterval' command to a non-existing station id (0, negative and positive).\n 2. Expect 200 response of StationGetIntervalResponse type that is empty.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @ValueSource(ints = {-1, 0, 6})
    public void shouldGetEmptyIntervalResponseForNonExistingStations(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.GET_INTERVAL);
        StationGetIntervalResponse response = client.getStationIntervalAndValidateResponse(stationId, request);

        assertThat(response.result(), is(0));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get Empty Set Values Result For Non-Existing Station.")
    @Description("1. Send POST request with 'setValues' command and positive integer in payload to a non-existing station id (0, negative and positive).\n 2. Expect 200 response of StationSetValuesResponse type that contains empty result.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @ValueSource(ints = {-1, 0, 6})
    public void shouldGetEmptySetValuesResponseForNonExistingStations(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.SET_VALUES)
                .withPayload(1);
        StationSetValuesResponse response = client.setStationValuesAndValidateResponse(stationId, request);

        assertThat(response.result(), is(nullValue()));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get FAILED Set Values Result When Sending 0 In Payload.")
    @Description("1. Send POST request with 'setValues' command and 0 in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: FAILED.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetFailedSetValuesResponseForZeroPayload(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.SET_VALUES)
                .withPayload(0);
        StationSetValuesResponse response = client.setStationValuesAndValidateResponse(stationId, request);

        assertThat(response.result(), equalTo(SetResult.FAILED));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get FAILED Set Values Result When Sending Negative Payload.")
    @Description("1. Send POST request with 'setValues' command and negative number in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: FAILED.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetFailedSetValuesResponseForNegativePayload(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "setValues");
        invalidRequest.put("payload", Integer.MIN_VALUE);
        StationSetValuesResponse response = client.postModifiedSetRequestToStationAndExtractResponse(stationId, invalidRequest);

        assertThat(response.result(), equalTo(SetResult.FAILED));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get FAILED Set Values Result When Sending Invalid Payload Type (Float).")
    @Description("1. Send POST request with 'setValues' command and floating point number in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: FAILED.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetFailedSetValuesResponseForFloatPayload(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "setValues");
        invalidRequest.put("payload", 1.5);
        StationSetValuesResponse response = client.postModifiedSetRequestToStationAndExtractResponse(stationId, invalidRequest);

        assertThat(response.result(), equalTo(SetResult.FAILED));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get FAILED Set Values Result When Sending Invalid Payload Type (String).")
    @Description("1. Send POST request with 'setValues' command and string in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: FAILED.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetFailedSetValuesResponseForStringPayload(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "setValues");
        invalidRequest.put("payload", "1");
        StationSetValuesResponse response = client.postModifiedSetRequestToStationAndExtractResponse(stationId, invalidRequest);

        assertThat(response.result(), equalTo(SetResult.FAILED));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get FAILED Set Values Result When Sending Invalid Payload Type (Boolean).")
    @Description("1. Send POST request with 'setValues' command and boolean (true or false) in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: FAILED.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetFailedSetValuesResponseForBooleanPayload(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "setValues");
        invalidRequest.put("payload", true);
        StationSetValuesResponse response = client.postModifiedSetRequestToStationAndExtractResponse(stationId, invalidRequest);

        assertThat(response.result(), equalTo(SetResult.FAILED));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending Invalid Command String.")
    @Description("1. Send POST request with unexpected command string (not in getVersion, getInterval, setValues) and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseForInvalidCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "random_non_existing_command");
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending Invalid Command Type (Integer).")
    @Description("1. Send POST request with integer in command and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetEmpty400ValuesResponseForIntegerCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", 0);
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending Invalid Command Type (Float).")
    @Description("1. Send POST request with floating point number in command and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseForFloatCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", 1.5);
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending Invalid Command Type (Boolean).")
    @Description("1. Send POST request with boolean (true or false) in command and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseForBooleanCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", false);
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending Empty Command String.")
    @Description("1. Send POST request with empty string in command and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseForEmptyCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "");
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending Blank Command String.")
    @Description("1. Send POST request with blank (contains only whitespaces) string in command and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseForBlankCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", " ");
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get '400 Bad Request' Set Values Response When Sending No Command.")
    @Description("1. Send POST request with no command field and positive integer in payload to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseWithoutCommand(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("payload", 1);

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Get FAILED Set Values Response When Sending No Payload.")
    @Description("1. Send POST request with no payload field and 'setValues' command to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetFailedSetValuesResponseWithoutPayload(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("command", "setValues");
        StationSetValuesResponse response = client.postModifiedSetRequestToStationAndExtractResponse(stationId, invalidRequest);

        assertThat(response.result(), equalTo(SetResult.FAILED));
    }

    @Feature("SET_VALUES")
    @Feature("GET_INTERVAL")
    @Feature("GET_VERSION")
    @DisplayName("Should Get '400 Bad Request' Response When Sending Empty Request.")
    @Description("1. Send POST request with empty body to an existing station id.\n 2. Expect 400 response.")
    @Severity(SeverityLevel.TRIVIAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400ResponseWithEmptyRequestBody(int stationId) {
        Map<String, Object> invalidRequest = new HashMap<>();

        client.postModifiedRequestToStation(stationId, invalidRequest)
              .then()
              .assertThat()
              .statusCode(SC_BAD_REQUEST);
    }

    // here I'm purely assuming, as I don't have the requirements, but my assumption is:
    // since client might add some fields of its own and send them to the server, server must be able to handle such requests
    @Feature("SET_VALUES")
    @DisplayName("Should Get OK Set Values Response When Sending Request With An Unexpected Field.")
    @Description("1. Send POST request with 'setValues' command, positive integer in payload and additional non-existing field to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: OK.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet400SetValuesResponseWithAdditionalFieldInRequest(int stationId) {
        Map<String, Object> modifiedRequest = new HashMap<>();
        modifiedRequest.put("command", CommandType.SET_VALUES.value());
        modifiedRequest.put("payload", 1);
        modifiedRequest.put("random_frontend_field", "random_value");
        StationSetValuesResponse response = client.postModifiedSetRequestToStationAndExtractResponse(stationId, modifiedRequest);

        // and since server returned 200, I'm expecting that values were set correctly
        assertThat(response.result(), equalTo(SetResult.OK));
    }

    @Feature("GET_VERSION")
    @DisplayName("Should Get 200 Get Version Response When Sending Request With An Unexpected Field.")
    @Description("1. Send POST request with 'getVersion' command and additional non-existing field to an existing station id.\n 2. Expect 200 response of StationGetVersionResponse type.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet200GetVersionResponseWithAdditionalFieldInRequest(int stationId) {
        Map<String, Object> modifiedRequest = new HashMap<>();
        modifiedRequest.put("command", CommandType.GET_VERSION.value());
        modifiedRequest.put("random_frontend_field", "random_value");

        StationGetVersionResponse response = client.postModifiedVersionRequestToStationAndExtractResponse(stationId, modifiedRequest);
        assertThat(response.result(), is(not(emptyOrNullString())));
    }

    @Feature("GET_INTERVAL")
    @DisplayName("Should Get 200 Get Interval Response When Sending Request With An Unexpected Field.")
    @Description("1. Send POST request with 'getInterval' command and additional non-existing field to an existing station id.\n 2. Expect 200 response of StationGetIntervalResponse type.")
    @Severity(SeverityLevel.MINOR)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet200GetIntervalResponseWithAdditionalFieldInRequest(int stationId) {
        Map<String, Object> modifiedRequest = new HashMap<>();
        modifiedRequest.put("command", CommandType.GET_INTERVAL.value());
        modifiedRequest.put("random_frontend_field", "random_value");

        StationGetIntervalResponse response = client.postModifiedIntervalRequestToStationAndExtractResponse(stationId, modifiedRequest);
        assertThat(response.result(), isA(Integer.class));
    }

    // just an example of how manual / not yet automated tests can look like in this set up
    @Feature("GET_INTERVAL")
    @DisplayName("Should Get 4xx Client Error When Sending A Request With A Not Allowed HTTP Method.")
    @Description("Checks that unused methods are disabled on the server.")
    @Severity(SeverityLevel.MINOR)
    @Tag("manual")
    @Test
    public void shouldGet4xxResponseWithNotAllowedMethod() {
        Allure.step("Send GET/PUT/PATCH/DELETE request to /v1/tests/{existingStationID}", step -> {});
        Allure.step("Expect a 4xx error status code in response (405 Method Not Allowed is preferred)", step -> {});
    }

}
