package avg.source;

import avg.model.EntryDto;
import avg.service.InsightsService;
import avg.util.ConvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class FileSource implements Source, ApplicationRunner {

    private final InsightsService insightsService;
    private final String inputFileName;
    private final boolean skipImport;

    public FileSource(InsightsService insightsService, String inputFileName, boolean skipImport) {
        this.insightsService = insightsService;
        this.inputFileName = inputFileName;
        this.skipImport = skipImport;
    }

    @Override
    public void accept(EntryDto message) {
        log.debug("Received message {}", message);
        insightsService.put(message);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (skipImport) {
            log.info("Import skipped by source.file.skip-import parameter");
            return;
        }

        log.info("Import from file started");
        Instant start = Instant.now();
        Path path = Optional.ofNullable(getClass().getClassLoader().getResource(inputFileName))
                .map(this::toUri)
                .map(Paths::get)
                .orElseThrow(() -> new RuntimeException("Failed to parse " + this.inputFileName));
        Files.lines(path).
                map(ConvUtils::toEntry)
                .filter(Objects::nonNull)
                .forEach(this::accept);
        log.info("Import finished. Took {}", Duration.between(start, Instant.now()));
    }

    private URI toUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            log.warn("Url to uri conversation failed. Url: {}", url);
            return null;
        }
    }
}
