package avg.config;


import avg.gateway.InMemoryInsightsGateway;
import avg.gateway.InsightsGateway;
import avg.gateway.MongoInsightsGateway;
import avg.service.InsightsService;
import avg.source.FileSource;
import avg.source.KinesisSource;
import avg.source.Source;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnProperty(value = "gateway.memory.enabled", havingValue = "true", matchIfMissing = true)
    public InsightsGateway insightsMemoryGateway() {
        return new InMemoryInsightsGateway();
    }

    @Bean
    @ConditionalOnProperty(value = "gateway.mongo.enabled", havingValue = "true")
    public InsightsGateway insightsMongoGateway(MongoTemplate mongoTemplate) {
        return new MongoInsightsGateway(mongoTemplate);
    }

    @Bean
    public InsightsService insightsService(InsightsGateway insightsGateway) {
        return new InsightsService(insightsGateway);
    }

    @Bean
    @ConditionalOnProperty(value = "source.file.enabled", havingValue = "true", matchIfMissing = true)
    public Source fileSource(InsightsService insightsService,
                             @Value("${source.file.name}") String filename,
                             @Value("${source.file.skip-import}") boolean skipImport) {
        return new FileSource(insightsService, filename, skipImport);
    }

    @Bean
    @ConditionalOnProperty(value = "source.kinesis.enabled", havingValue = "true")
    public Source kinesisSource(InsightsService insightsService, AmazonKinesis amazonKinesis) {
        return new KinesisSource(insightsService, amazonKinesis);
    }

    @Bean
    @ConditionalOnProperty(value = "source.kinesis.enabled", havingValue = "true")
    public AmazonKinesis amazonKinesis() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("", "");
        return AmazonKinesisClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}
