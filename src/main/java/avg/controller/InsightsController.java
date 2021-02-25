package avg.controller;

import avg.model.Insight;
import avg.service.InsightsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsightsController {

    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping("/{eventType}/average")
    public Insight get(@PathVariable String eventType, @RequestParam long from, @RequestParam long to) {
        return insightsService.avg(eventType, from, to);
    }
}
