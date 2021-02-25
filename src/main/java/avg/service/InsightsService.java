package avg.service;

import avg.gateway.InsightsGateway;
import avg.model.EntryDto;
import avg.model.Insight;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsightsService {

    private final InsightsGateway insightsGateway;

    public InsightsService(InsightsGateway insightsGateway) {
        this.insightsGateway = insightsGateway;
    }

    public void put(EntryDto entryDto) {
        log.debug("Adding entry {}", entryDto);
        entryDto.setTimestamp(roundToMinutes(entryDto.getTimestamp()));
        insightsGateway.put(entryDto);
    }

    public Insight avg(String type, long from, long to) {
        log.debug("Calculating avg for {} between {} and {}", type, from, to);
        return insightsGateway.avg(type, roundToMinutes(from), roundToMinutes(to));
    }

    private long roundToMinutes(long timestamp) {
        return timestamp / 60 * 60;
    }
}
