package org.slamperboom.graphics;

import org.slamperboom.model.IModel;
import org.slamperboom.model.processing.QueryRow;

import javax.swing.*;
import java.util.List;

public class View implements IView{
    private final MainWindow window;
    private final IModel model;

    public View(IModel model) {
        window = new MainWindow(this);
        this.model = model;
    }

    protected void exit(){
        model.exit();
    }

    protected void connect(){
        model.start();
    }

    protected void setCaching(boolean caching){
        model.setCaching(caching);
    }

    protected List<QueryRow> makeManualSelect(String query) {
        return model.makeManualSelect(query);
    }

    protected boolean makeManualUpdate(String query) {
        return model.makeManualUpdate(query);
    }

    protected List<QueryRow> getTableData(String tableName) {
        return model.getTableData(tableName);
    }

    protected List<QueryRow> makePredefinedQuery(int queryID, String... conditions) {
        return model.makePredefinedQuery(queryID, conditions);
    }

    protected boolean makeUpdate(String tableName, String recordID, String columnName, String value) {
        return model.makeUpdate(tableName, recordID, columnName, value);
    }

    protected boolean makeDelete(String tableName, String recordID) {
        return model.makeDelete(tableName, recordID);
    }

    protected boolean makeInsert(String tableName, List<String> values) {
        return model.makeInsert(tableName, values);
    }

    protected boolean exportData(List<QueryRow> rows){
        return model.exportData(rows);
    }

    @Override
    public void startingConnection() {
        window.showStartingConnection();
    }

    @Override
    public void connected() {
        window.showConnected();
    }

    @Override
    public void failedConnection() {
        window.showFailedConnection();
    }

    @Override
    public String getPassword() {
        return JOptionPane.showInputDialog(window, "Введите пароль для подключения", "Вход", JOptionPane.QUESTION_MESSAGE);
    }
}
