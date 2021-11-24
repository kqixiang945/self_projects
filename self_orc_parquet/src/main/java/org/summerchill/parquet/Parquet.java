package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_17:08
 */
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;
import java.util.List;
import java.util.Map;

public class Parquet {
    private List<SimpleGroup> data;
    private List<Type> schemaFields;
    private Map<String, String> keyValueMetadata;
    private MessageType schema;

    public Parquet(List<SimpleGroup> data, List<Type> schemaFields, Map<String, String> keyValueMetadata, MessageType schema) {
        this.data = data;
        this.schemaFields = schemaFields;
        this.keyValueMetadata = keyValueMetadata;
        this.schema = schema;
    }

    public List<SimpleGroup> getData() {
        return data;
    }

    public List<Type> getSchemaFields() { return schemaFields; }

    public MessageType getSchema() { return schema; }

    public Map<String, String> getKeyValueMetadata() {
        return keyValueMetadata;
    }
}