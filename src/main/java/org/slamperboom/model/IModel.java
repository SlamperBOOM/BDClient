package org.slamperboom.model;

import org.slamperboom.model.processing.QueryRow;

import java.util.List;

public interface IModel {
    List<QueryRow> makeManualSelect(String query);
    boolean makeManualUpdate(String query);
    List<QueryRow> getTableData(String tableName);
    List<QueryRow> makePredefinedQuery(int queryID, String... conditions);
    boolean makeUpdate(String tableName, String recordID, String columnName, String value);
    boolean makeDelete(String tableName, String recordID);
    boolean makeInsert(String tableName, List<String> values);
    boolean exportData(List<QueryRow> rows);
    void setCaching(boolean caching);
    void start();
    void exit();
}
