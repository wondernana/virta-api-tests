package clients;

import static configs.CustomAllureFilter.customAllureFilter;
import static configs.OwnerConfig.CONFIG;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import java.util.Map;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.StationGetIntervalResponse;
import models.StationGetVersionResponse;
import models.StationSetValuesResponse;
import models.StationTestRequest;

public class StationAPIClient {

    private final RequestSpecification requestSpec;
    private final ResponseSpecification successfulResponseSpec;

    public StationAPIClient() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(CONFIG.getBaseUri())
                .setBasePath(CONFIG.getBasePath())
                .setContentType(ContentType.JSON)
                .addFilter(customAllureFilter().withCustomTemplates())
                .build();

        successfulResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(SC_OK)
                .build();
    }
    
    public StationGetVersionResponse getStationVersionAndValidateResponse(int stationId, StationTestRequest request) {
        return postRequestToStation(stationId, request)
                .then()
                .spec(successfulResponseSpec)
                .log().ifError()
                // response extraction such as this also acts as a schema validation
                .extract().response().as(StationGetVersionResponse.class);
    }

    public StationGetIntervalResponse getStationIntervalAndValidateResponse(int stationId, StationTestRequest request) {
        return postRequestToStation(stationId, request)
                .then()
                .spec(successfulResponseSpec)
                .log().ifError()
                .extract().response().as(StationGetIntervalResponse.class);
    }

    public StationSetValuesResponse setStationValuesAndValidateResponse(int stationId, StationTestRequest request) {
        return postRequestToStation(stationId, request)
                .then()
                .spec(successfulResponseSpec)
                .log().ifError()
                .extract().response().as(StationSetValuesResponse.class);
    }

    public StationSetValuesResponse postModifiedSetRequestToStationAndExtractResponse(int stationId, Map<String, Object> request) {
        return postModifiedRequestToStation(stationId, request)
                .then()
                .spec(successfulResponseSpec)
                .extract().response().as(StationSetValuesResponse.class);
    }

    public StationGetIntervalResponse postModifiedIntervalRequestToStationAndExtractResponse(int stationId, Map<String, Object> request) {
        return postModifiedRequestToStation(stationId, request)
                .then()
                .spec(successfulResponseSpec)
                .extract().response().as(StationGetIntervalResponse.class);
    }

    public StationGetVersionResponse postModifiedVersionRequestToStationAndExtractResponse(int stationId, Map<String, Object> request) {
        return postModifiedRequestToStation(stationId, request)
                .then()
                .spec(successfulResponseSpec)
                .extract().response().as(StationGetVersionResponse.class);
    }

    public Response postModifiedRequestToStation(int stationId, Map<String, Object> request) {
        return given()
                .spec(requestSpec)
                .pathParam("stationId", stationId)
                .body(request)
                .when()
                .post()
                .thenReturn();
    }

    private Response postRequestToStation(int stationId, StationTestRequest request) {
        return given()
                .spec(requestSpec)
                .pathParam("stationId", stationId)
                .body(request)
                .when()
                .post()
                .thenReturn();
    }

}
