package configs;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomAllureFilter {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    private CustomAllureFilter() {}

    public static CustomAllureFilter customAllureFilter() {
        return InitFilter.customAllureFilter;
    }

    public AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;

    }

    private static class InitFilter {
        private static final CustomAllureFilter customAllureFilter = new CustomAllureFilter();
    }
}
