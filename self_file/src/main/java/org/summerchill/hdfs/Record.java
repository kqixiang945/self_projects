package org.summerchill.hdfs;

/**
 * @author kxh
 * @description
 * @date 20210625_16:08
 */
public interface Record {

    public void addColumn(Column column);

    public void setColumn(int i, final Column column);

    public Column getColumn(int i);

    public String toString();

    public int getColumnNumber();

    public int getByteSize();

    public int getMemorySize();

}