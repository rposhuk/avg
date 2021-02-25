package avg.gateway;

import avg.model.Entry;
import avg.model.EntryDto;
import avg.model.Insight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
public class MongoInsightsGateway implements InsightsGateway {

    private final MongoTemplate mongoTemplate;

    public MongoInsightsGateway(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public EntryDto put(EntryDto entryDto) {
        mongoTemplate.upsert(
                Query.query(Criteria.where("type").is(entryDto.getType()).and("timestamp").is(entryDto.getTimestamp())),
                new Update().addToSet("values", entryDto.getValue()),
                Entry.class
        );
        return entryDto;
    }

    @Override
    public Insight avg(String type, long from, long to) {
        TypedAggregation<Entry> aggregation = newAggregation(Entry.class,
                match(Criteria.where("type").is(type)),
                match(Criteria.where("timestamp").gte(from).andOperator(Criteria.where("timestamp").lte(to))),
                unwind("values"),
                group().avg("values").as("value")
                        .count().as("processedCount")
                        .addToSet("type").as("type")
        );
        Instant start = Instant.now();
        Insight result = mongoTemplate.aggregate(aggregation, Insight.class).getUniqueMappedResult();
        log.info("Avg aggregation took {}", Duration.between(start, Instant.now()));
        return result;
    }
}
