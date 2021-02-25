package avg.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@ConditionalOnProperty(value = "gateway.mongo.enabled", havingValue = "true")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "local";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
