package tests.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;

import clients.StationAPIClient;
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

public class PositiveStationAPITests {

    private final StationAPIClient client = new StationAPIClient();

    @Feature("GET_VERSION")
    @DisplayName("Should Get Valid (Non-Empty) Version Of The Station When Sending Only Required Fields.")
    @Description("1. Send POST request with 'getVersion' command to an existing station id.\n 2. Expect 200 response of StationGetVersionResponse type and that contains non-blank or empty version string.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetValidVersionForExistingStation(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.GET_VERSION);
        StationGetVersionResponse response = client.getStationVersionAndValidateResponse(stationId, request);

        assertThat(response.result(), is(not(blankOrNullString())));
    }

    @Feature("GET_INTERVAL")
    @DisplayName("Should Get Valid (Number) Interval For The Station When Sending Only Required Fields.")
    @Description("1. Send POST request with 'getInterval' command to an existing station id.\n 2. Expect 200 response of StationGetIntervalResponse type that contains interval of type integer.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetValidIntervalForExistingStation(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.GET_INTERVAL);
        StationGetIntervalResponse response = client.getStationIntervalAndValidateResponse(stationId, request);

        assertThat(response.result(), isA(Integer.class));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Be Able To Set Interval To The Min Possible Value When Sending All Required Fields.")
    @Description("1. Send POST request with 'setValues' command and the smallest positive integer (1) in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: OK.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldSetMinIntervalForExistingStation(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.SET_VALUES)
                .withPayload(1);
        StationSetValuesResponse setIntervalResponse = client.setStationValuesAndValidateResponse(stationId, request);

        assertThat(setIntervalResponse.result(), equalTo(SetResult.OK));
    }

    @Feature("SET_VALUES")
    @DisplayName("Should Be Able To Set Interval To The Max Possible Value When Sending All Required Fields.")
    @Description("1. Send POST request with 'setValues' command and the max positive integer value (2147483647) in payload to an existing station id.\n 2. Expect 200 response of StationSetValuesResponse type that contains result: OK.")
    @Severity(SeverityLevel.NORMAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldSetMaxIntervalForExistingStation(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.SET_VALUES)
                .withPayload(Integer.MAX_VALUE);
        StationSetValuesResponse setIntervalResponse = client.setStationValuesAndValidateResponse(stationId, request);

        assertThat(setIntervalResponse.result(), equalTo(SetResult.OK));
    }

    @Feature("SET_VALUES")
    @Feature("GET_INTERVAL")
    @DisplayName("Should Get Previously Set Interval When Sending All Required Fields.")
    @Description("1. Send POST request with 'setValues' command and positive integer in payload to an existing station id.\n 2. Send POST request with 'getInterval' command to the same station id.\n 3. Expect 200 response of StationGetIntervalResponse type that contains interval set in Step 1.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGetCorrectIntervalAfterSettingIt(int stationId) {
        StationTestRequest setIntervalRequest = new StationTestRequest()
                .withCommand(CommandType.SET_VALUES)
                .withPayload(1);
        client.setStationValuesAndValidateResponse(stationId, setIntervalRequest);

        StationTestRequest getIntervalRequest = new StationTestRequest()
                .withCommand(CommandType.GET_INTERVAL);
        StationGetIntervalResponse getIntervalResponse = client.getStationIntervalAndValidateResponse(stationId, getIntervalRequest);

        assertThat(getIntervalResponse.result(), equalTo(setIntervalRequest.payload()));
    }

    @Feature("GET_VERSION")
    @DisplayName("Should Get 200 Get Version Response When Including (Optional) Payload field.")
    @Description("1. Send POST request with 'getVersion' command and positive integer in payload to an existing station id.\n 2. Expect 200 response of StationGetVersionResponse type and that contains non-blank or empty version string.")
    // high severity since FE client is very likely to be sending all existing fields in the request
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet200GetVersionResponseWithPayloadInRequest(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.GET_VERSION)
                .withPayload(1);
        StationGetVersionResponse response = client.getStationVersionAndValidateResponse(stationId, request);

        assertThat(response.result(), is(not(blankOrNullString())));
    }

    @Feature("GET_INTERVAL")
    @DisplayName("Should Get 200 Get Interval Response When Including (Optional) Payload field.")
    @Description("1. Send POST request with 'getInterval' command and positive integer in payload to an existing station id.\n 2. Expect 200 response of StationGetIntervalResponse type that contains an interval of type integer.")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("auto")
    @ParameterizedTest(name = "Station ID: {argumentsWithNames}")
    @CsvFileSource(resources = "/available_stations.csv", numLinesToSkip = 1)
    public void shouldGet200GetIntervalResponseWithPayloadInRequest(int stationId) {
        StationTestRequest request = new StationTestRequest()
                .withCommand(CommandType.GET_INTERVAL)
                .withPayload(1);
        StationGetIntervalResponse response = client.getStationIntervalAndValidateResponse(stationId, request);

        assertThat(response.result(), isA(Integer.class));
    }

}
