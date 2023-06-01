package org.slamperboom.model;

import org.slamperboom.graphics.IView;
import org.slamperboom.graphics.View;
import org.slamperboom.model.data.Requests;
import org.slamperboom.model.processing.PredefinedQueryFactory;
import org.slamperboom.model.processing.QueryManager;
import org.slamperboom.model.processing.QueryRow;
import org.slamperboom.model.processing.predefinedQueries.PredefinedQuery;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Model implements IModel, IManagerModel {
    private final IView view;
    private final Requests requests;
    private final QueryManager manager;

    public Model(){
        view = new View(this);

        requests = new Requests();
        manager = new QueryManager(this);
        view.failedConnection();
    }

    @Override
    public void start(){
        view.startingConnection();
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            int repeatCount = 5;
            for(int i=0; i<repeatCount; ++i) {
                String password = view.getPassword();
                if(password == null){
                    break;
                }
                try {
                    manager.connectToDataBase(password);
                    return true;
                }catch (SQLException ignored){
                }
            }
            return false;
        });
        try {
            Boolean result = future.get();
            if(Boolean.TRUE.equals(result)){
                view.connected();
            }else{
                view.failedConnection();
            }
        } catch (InterruptedException | ExecutionException e) {
            future.cancel(true);
            e.printStackTrace();
        }
    }

    @Override
    public void exit(){
        try {
            manager.closeConnection();
        }catch (SQLException e){
            System.out.println("Не удалось корректно закрыть соединение с базой данных");
            e.printStackTrace();
        }
    }

    @Override
    public List<QueryRow> makeManualSelect(String query) {
        return manager.manualQuery(query);
    }

    @Override
    public boolean makeManualUpdate(String query) {
        boolean status = manager.manualUpdate(query);
        if(status){
            String[] words = query.split(" ");
            String tableName;
            String[] split;
            if(words[0].equalsIgnoreCase("update")){
                split = words[1].split("\"");
            }else{
                split = words[2].split("\"");
            }
            tableName = split[split.length - 1];
            requests.deleteRequestsWithTable(tableName);
        }
        return status;
    }

    @Override
    public List<QueryRow> getTableData(String tableName) {
        String queryURL = "select * from \""+tableName+"\" order by \""+manager.getPrimaryKeyName(tableName)+"\"";
        return manager.manualQuery(queryURL);
    }

    @Override
    public List<QueryRow> makePredefinedQuery(int queryID, String... conditions) {
        PredefinedQuery predefinedQuery = PredefinedQueryFactory.getPredefinedQuery(queryID);
        if(predefinedQuery == null){
            return new ArrayList<>();
        }
        String url = predefinedQuery.getQueryURL(conditions);
        return manager.manualQuery(url);
    }

    @Override
    public boolean makeUpdate(String tableName, String recordID, String columnName, String value) {
        boolean status = manager.updateData(tableName, recordID, columnName, value);
        if(status){
            requests.deleteRequestsWithTable(tableName);
        }
        return status;
    }

    @Override
    public boolean makeDelete(String tableName, String recordID) {
        boolean status = manager.deleteRecord(tableName, recordID);
        if(status){
            requests.deleteRequestsWithTable(tableName);
        }
        return status;
    }

    @Override
    public boolean makeInsert(String tableName, List<String> values) {
        boolean status = manager.insertRecord(tableName, values);
        if(status){
            requests.deleteRequestsWithTable(tableName);
        }
        return status;
    }

    @Override
    public boolean exportData(List<QueryRow> rows) {
        File exportFile = new File("output.txt");
        try{
            exportFile.createNewFile();
        }catch (IOException e){
            return false;
        }
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))){
            for (QueryRow row : rows) {
                for (int j = 0; j < row.getLength(); ++j) {
                    if(row.getColumn(j).matches("\\d*\\.\\d+")){//дробное число
                        writer.write(row.getColumn(j).replace(".", ","));
                    }else{
                        writer.write(row.getColumn(j));
                    }
                    writer.write("\t");
                }
                writer.newLine();
            }
            return true;
        }catch (IOException e){
            return false;
        }
    }

    @Override
    public void setCaching(boolean caching) {
        requests.setCaching(caching);
    }

    @Override
    public Requests getRequestsHistory() {
        return requests;
    }
}
