package avg.model;

import lombok.Data;

@Data
public final class EntryDto {

    private long timestamp;
    private String type;
    private double value;

    public static EntryDto of(long timestamp, String type, double value) {
        EntryDto entryDto = new EntryDto();
        entryDto.setTimestamp(timestamp);
        entryDto.setType(type);
        entryDto.setValue(value);
        return entryDto;
    }
}
