package avg.gateway;

import avg.model.EntryDto;
import avg.model.Insight;

import java.util.List;

public interface InsightsGateway {

    EntryDto put(EntryDto entryDto);

    Insight avg(String type, long from, long to);

}
