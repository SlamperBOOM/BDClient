package org.slamperboom.graphics;

import org.slamperboom.graphics.panels.MainWindowPanel;
import org.slamperboom.model.processing.QueryRow;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainWindow extends JFrame {
    private final View view;
    private final MainWindowPanel mainPanel;

    public MainWindow(View view){
        this.view = view;

        mainPanel = new MainWindowPanel(this);
        setTitle("Airport app");
        setContentPane(mainPanel);
        setSize(400, 400);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
                super.windowClosing(e);
            }
        });
        setLocation(200, 100);
        setVisible(true);
    }

    public void connect(){
        view.connect();
    }

    public void setCaching(boolean caching){
        view.setCaching(caching);
    }

    public List<QueryRow> makeManualSelect(String query) {
        return view.makeManualSelect(query);
    }

    public void makeManualUpdate(String query) {
        boolean result = view.makeManualUpdate(query);
        if(result){
            JOptionPane.showMessageDialog(this, "Изменение проведено успешно", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "Не удалось произвести изменение таблицы", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public List<QueryRow> getTableData(String tableName) {
        return view.getTableData(tableName);
    }

    public List<QueryRow> makePredefinedQuery(int queryID, String... conditions) {
        return view.makePredefinedQuery(queryID, conditions);
    }

    public void makeUpdate(String tableName, String recordID, String columnName, String value) {
        boolean result = view.makeUpdate(tableName, recordID, columnName, value);
        if(!result){
            JOptionPane.showMessageDialog(this, "Не удалось обновить запись. Изменения не сохранены", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void makeDelete(String tableName, String recordID, QueryInfoWindow window) {
        boolean result = view.makeDelete(tableName, recordID);
        if(result){
            QueryInfoWindow newWindow = new QueryInfoWindow(this, getTableData(tableName), window.isEditable, tableName);
            newWindow.setLocation(window.getLocation());
            window.dispose();
            newWindow.showWindow();
        }else{
            JOptionPane.showMessageDialog(window, "Не удалось удалить запись", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void makeInsert(String tableName, List<String> values, QueryInfoWindow window) {
        boolean result = view.makeInsert(tableName, values);
        if(result){
            QueryInfoWindow newWindow = new QueryInfoWindow(this, getTableData(tableName), window.isEditable, tableName);
            newWindow.setLocation(window.getLocation());
            window.dispose();
            newWindow.showWindow();
        }else{
            JOptionPane.showMessageDialog(window, "Не удалось добавить запись", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean exportData(List<QueryRow> rows){
        return view.exportData(rows);
    }

    protected void showStartingConnection() {
        mainPanel.startingConnection();
    }

    protected void showConnected() {
        mainPanel.connected();
    }

    protected void showFailedConnection() {
        mainPanel.failedConnection();
    }

    private void exit(){
        view.exit();
        dispose();
        System.exit(0);
    }
}
