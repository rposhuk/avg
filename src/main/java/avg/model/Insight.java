package avg.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Insight {

    private String type;
    private double value;
    private int processedCount;

    public static Insight of(String type, double value, int count) {
        Insight message = new Insight();
        message.setType(type);
        message.setValue(value);
        message.setProcessedCount(count);
        return message;
    }
}
