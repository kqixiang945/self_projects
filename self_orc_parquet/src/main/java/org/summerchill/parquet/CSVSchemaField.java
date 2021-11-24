package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_17:07
 */
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

public class CSVSchemaField {
    String fieldName;
    String fieldPrimitiveType;

    public CSVSchemaField(String fieldName, String fieldPrimitiveType){
        this.fieldName = fieldName;
        this.fieldPrimitiveType = fieldPrimitiveType;
    }

    public PrimitiveType getPrimitiveType(){
        return new PrimitiveType(Type.Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.valueOf(fieldPrimitiveType), this.fieldName);
    }

}