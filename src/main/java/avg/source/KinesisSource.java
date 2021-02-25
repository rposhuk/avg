package avg.source;

import avg.model.EntryDto;
import avg.service.InsightsService;
import avg.util.ConvUtils;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

public class KinesisSource implements Source, ApplicationRunner {

    private final InsightsService insightsService;
    private final AmazonKinesis kinesis;
    private final String streamName = "assignments";

    public KinesisSource(InsightsService insightsService, AmazonKinesis kinesis) {
        this.insightsService = insightsService;
        this.kinesis = kinesis;
    }

    @Override
    public void accept(EntryDto message) {
        System.out.println(message.toString());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        GetShardIteratorRequest readShardsRequest = new GetShardIteratorRequest();
        readShardsRequest.setStreamName(streamName);
        readShardsRequest.setShardIteratorType(ShardIteratorType.LATEST);
        readShardsRequest.setShardId("ASSIGNMENTS_SHARD_ID");

        GetShardIteratorResult readIterator = kinesis.getShardIterator(readShardsRequest);

        GetRecordsRequest recordsRequest = new GetRecordsRequest();
        recordsRequest.setShardIterator(readIterator.getShardIterator());
        recordsRequest.setLimit(25);

        GetRecordsResult recordsResult = kinesis.getRecords(recordsRequest);
        while (!recordsResult.getRecords().isEmpty()) {
            recordsResult.getRecords().stream()
                    .map(e -> ConvUtils.toEntry(new String(e.getData().array())))
                    .filter(Objects::nonNull)
                    .forEach(this::accept);

            recordsRequest.setShardIterator(recordsResult.getNextShardIterator());
            recordsResult = kinesis.getRecords(recordsRequest);
        }
    }
}
