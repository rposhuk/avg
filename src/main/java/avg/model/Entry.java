package avg.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "entries")
public final class Entry {

    @MongoId
    private ObjectId id;

    private long timestamp;
    private String type;
    private double[] values;
}
