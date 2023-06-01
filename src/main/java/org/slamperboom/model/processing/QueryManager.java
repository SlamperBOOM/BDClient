package org.slamperboom.model.processing;

import org.slamperboom.model.IManagerModel;
import org.slamperboom.model.data.RequestInfo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QueryManager {
    private final IManagerModel model;
    private Connection connection;
    private Statement statement;

    public QueryManager(IManagerModel model){
        this.model = model;
    }

    public void connectToDataBase(String password) throws SQLException {
        connection = DriverManager
                .getConnection("jdbc:postgresql://sblab03.nsu.ru:5432/v_shinkevich","v_shinkevich", password);
    }

    public void closeConnection() throws SQLException {
        if(connection != null) {
            connection.close();
        }
    }

    public String getPrimaryKeyName(String tableName){
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet set = metaData.getPrimaryKeys(null, null, tableName);
            set.next();
            return set.getString("COLUMN_NAME");
        }catch (SQLException e){
            return "";
        }
    }

    public List<QueryRow> manualQuery(String queryURL) {
        try {
            RequestInfo requestInfo = model.getRequestsHistory().findRequest(queryURL);
            if(requestInfo != null){
                return requestInfo.getResponse();
            }
            ResultSet set = execSelect(queryURL);
            ResultSetMetaData meta = set.getMetaData();
            List<QueryRow> rows = new ArrayList<>();
            List<String> headers = new ArrayList<>();
            for (int i = 1; i <= meta.getColumnCount(); ++i) {
                headers.add(meta.getColumnLabel(i));
            }
            rows.add(new QueryRow(headers));
            while(set.next()) {
                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= meta.getColumnCount(); ++i) {
                    columns.add(getField(meta.getColumnType(i), i, set));
                }
                rows.add(new QueryRow(columns));
            }
            model.getRequestsHistory().addRequest(new RequestInfo(rows, queryURL, LocalDateTime.now()));
            set.close();
            return rows;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean manualUpdate(String queryURL){
        try {
            return execUpdate(queryURL) > 0;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean updateData(String tableName, String recordID, String columnName, String value){
        try {
            String builder = "update " +
                    "\"" + tableName + "\" " +
                    "set " + "\"" + columnName + "\"=" +
                    convertValueToCorrectType(tableName, columnName, value) + " " +
                    "where \"" + getPrimaryKeyName(tableName) + "\"=" + recordID;
            return execUpdate(builder) > 0;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteRecord(String tableName, String recordID){
        try{
            String builder = "delete from " +
                    "\"" + tableName + "\" " +
                    "where \"" + getPrimaryKeyName(tableName) + "\"=" +
                    recordID;
            return execUpdate(builder) > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean insertRecord(String tableName, List<String> values){
        try{
            StringBuilder builder = new StringBuilder();
            builder.append("insert into ");
            builder.append("\"").append(tableName).append("\" values (");

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet set = metaData.getColumns(null, null, tableName, null);
            //размер set совпадает с размером values
            for(String val : values){
                set.next();
                if(val.equals("")) {
                    builder.append("null,");
                }else{
                    builder.append(convertValueToCorrectType(tableName, set.getString("COLUMN_NAME"), val)).append(",");
                }
            }
            if(!set.isClosed()){
                set.close();
            }
            builder.deleteCharAt(builder.length()-1).append(");");
            return execUpdate(builder.toString()) > 0;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    private ResultSet execSelect(String queryURL) throws SQLException {
        statement = connection.createStatement();
        return statement.executeQuery(queryURL);
    }

    private int execUpdate(String queryURL) throws SQLException {
        statement = connection.createStatement();
        return statement.executeUpdate(queryURL);
    }

    private String convertValueToCorrectType(String tableName, String columnName, String value) throws SQLException {
        if(value.equalsIgnoreCase("default")){
            return value;
        }
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet set = metaData.getColumns(null, null, tableName, null);

        int columnType = Types.VARCHAR;
        while(set.next()){
            if(set.getString("COLUMN_NAME").equals(columnName)){
                columnType = set.getInt("DATA_TYPE");
                break;
            }
        }
        if(!set.isClosed()){
            set.close();
        }
        switch (columnType){
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.SMALLINT: {
                return value;
            }
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.TIMESTAMP_WITH_TIMEZONE:
            case Types.TIMESTAMP:
            case Types.TIME:
            case Types.BIT:
            case Types.DATE: {
                return "'"+value+"'";
            }
            default: {
                return "";
            }
        }
    }

    private String getField(int columnType, int index, ResultSet set) throws SQLException {
        switch (columnType){
            case Types.NUMERIC:
            case Types.DOUBLE:{
                return Double.toString(set.getDouble(index));
            }
            case Types.FLOAT:{
                return Float.toString(set.getFloat(index));
            }
            case Types.BIGINT:{
                return Long.toString(set.getLong(index));
            }
            case Types.INTEGER:{
                return Integer.toString(set.getInt(index));
            }
            case Types.SMALLINT:{
                return Short.toString(set.getShort(index));
            }
            case Types.CHAR:
            case Types.VARCHAR:{
                return set.getString(index);
            }
            case Types.DATE:{
                Timestamp timestamp = set.getTimestamp(index);
                LocalDate date = timestamp.toLocalDateTime().toLocalDate();
                return date.toString();
            }
            case Types.TIMESTAMP_WITH_TIMEZONE:
            case Types.TIMESTAMP:{
                Timestamp stamp = set.getTimestamp(index);
                if(stamp == null){
                    return null;
                }else{
                    return stamp.toString();
                }
            }
            case Types.TIME:{
                return set.getTime(index).toString();
            }
            case Types.BIT:{
                return Boolean.toString(set.getBoolean(index));
            }
            default: {
                return null;
            }
        }
    }
}
