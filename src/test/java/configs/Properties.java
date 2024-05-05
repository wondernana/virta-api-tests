package configs;

import static org.aeonbits.owner.Config.LoadType.MERGE;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(MERGE)
@Sources({"system:env",
          "system:properties",
          "file:src/test/resources/test-data.properties"})

public interface Properties extends Config {

    @Key("baseUri")
    String getBaseUri();

    @Key("basePath")
    String getBasePath();

}
