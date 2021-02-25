package avg.util;

import avg.model.EntryDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ConvUtils {

    private static final int TIMESTAMP_INDEX = 0;
    private static final int TYPE_INDEX = 1;
    private static final int VALUE_INDEX = 2;

    private ConvUtils() {
    }

    public static EntryDto toEntry(String line) {
        try {
            String[] split = line.split(",");
            return EntryDto.of(Long.parseLong(split[TIMESTAMP_INDEX]),
                    split[TYPE_INDEX],
                    Double.parseDouble(split[VALUE_INDEX]));
        } catch (Exception e) {
            log.warn("Failed to parse line '{}'", line);
            return null;
        }
    }
}
