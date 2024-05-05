package configs;

import org.aeonbits.owner.ConfigFactory;

public class OwnerConfig {

    public static final Properties CONFIG = ConfigFactory.create(
            Properties.class,
            System.getenv(),
            System.getProperties()
    );

}
