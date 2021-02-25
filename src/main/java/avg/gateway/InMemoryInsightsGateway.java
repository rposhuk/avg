package avg.gateway;

import avg.model.EntryDto;
import avg.model.Insight;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class InMemoryInsightsGateway implements InsightsGateway {

    private List<EntryDto> storage = new ArrayList<>();

    @Override
    public EntryDto put(EntryDto entryDto) {
        storage.add(entryDto);
        return entryDto;
    }

    @Override
    public Insight avg(String type, long from, long to) {
        DoubleSummaryStatistics stat = storage.stream()
                .filter(e -> e.getType().equals(type))
                .filter(e -> e.getTimestamp() >= from && e.getTimestamp() <= to)
                .mapToDouble(EntryDto::getValue)
                .summaryStatistics();
        return Insight.of(type, stat.getAverage(), Math.toIntExact(stat.getCount()));
    }
}
