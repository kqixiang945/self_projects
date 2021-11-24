package org.summerchill.hdfs;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRecord implements Record {

    private static final int RECORD_AVERGAE_COLUMN_NUMBER = 16;

    private List<Column> columns;

    private int byteSize;
    // 首先是Record本身需要的内存
    private int memorySize;

    public DefaultRecord() {
        this.columns = new ArrayList<Column>(RECORD_AVERGAE_COLUMN_NUMBER);
    }

    @Override
    public Column getColumn(int i) {
        if (i < 0 || i >= columns.size()) {
            return null;
        }
        return columns.get(i);
    }

    @Override
    public int getColumnNumber() {
        return this.columns.size();
    }

    @Override
    public int getByteSize() {
        return byteSize;
    }

    public int getMemorySize(){
        return memorySize;
    }

    @Override
    public void addColumn(Column column) {
        columns.add(column);
    }

    @Override
    public void setColumn(int i, final Column column) {
        if (i < 0) {
            throw DataXException.asDataXException(HdfsWriterErrorCode.ARGUMENT_ERROR, "不能给index小于0的column设置值");
        }

        if (i >= columns.size()) {
            expandCapacity(i + 1);
        }
        this.columns.set(i, column);
    }

    private void expandCapacity(int totalSize) {
        if (totalSize <= 0) {
            return;
        }

        int needToExpand = totalSize - columns.size();
        while (needToExpand-- > 0) {
            this.columns.add(null);
        }
    }

    @Override
    public String toString() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("size", this.getColumnNumber());
        json.put("data", this.columns);
        return JSON.toJSONString(json);
    }

}