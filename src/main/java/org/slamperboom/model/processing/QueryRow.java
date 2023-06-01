package org.slamperboom.model.processing;

import java.util.List;

public class QueryRow{
    private final List<String> columns;

    public QueryRow(List<String> columns) {
        this.columns = columns;
    }

    public int getLength(){
        return columns.size();
    }

    public String getColumn(int index){
        return columns.get(index);
    }

    public void setColumn(int index, String val){
        columns.remove(index);
        columns.add(index, val);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(String column : columns){
            builder.append(column).append(" ");
        }
        return builder.toString();
    }
}
