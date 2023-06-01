package org.slamperboom.graphics.panels;

import org.slamperboom.graphics.MainWindow;
import org.slamperboom.graphics.ManualQueriesWindow;
import org.slamperboom.graphics.QueriesWindow;
import org.slamperboom.graphics.QueryInfoWindow;
import org.slamperboom.model.Tables;
import org.slamperboom.model.processing.QueryRow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class MainWindowPanel extends JPanel {
    private final MainWindow window;

    public MainWindowPanel(MainWindow window){
        this.window = window;
        setBackground(Color.PINK);
        setPreferredSize(new Dimension(window.getWidth()-40, window.getHeight()-100));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel title = new JLabel("Airport DB");
        title.setFont(new Font("TimesRoman", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(250, 60));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(title, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(getConnectionPanel(), constraints);
    }

    public void startingConnection() {
        remove(1);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(getConnectionPanel(), constraints);
        updateUI();
    }

    public void connected() {
        remove(1);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(getConnectedPanel(), constraints);
        updateUI();
    }

    public void failedConnection() {
        remove(1);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(getFailedConnection(), constraints);
        updateUI();
    }

    private JPanel getConnectionPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel label = new JLabel("Подключение...");
        label.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(label, constraints);

        panel.setPreferredSize(new Dimension(window.getWidth()-40, window.getHeight()-150));
        return panel;
    }

    private JPanel getFailedConnection(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JButton button = new JButton("Подключиться");
        button.setPreferredSize(new Dimension(200, 30));
        button.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        button.addActionListener(e -> {
            Thread thread = new Thread(window::connect);
            thread.start();
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(button, constraints);
        panel.setPreferredSize(new Dimension(window.getWidth()-40, window.getHeight()-150));

        return panel;
    }

    private JPanel getConnectedPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Font font = new Font("TimesRoman", Font.PLAIN, 14);

        JLabel tableNameLabel = new JLabel("Имя таблицы: ");
        tableNameLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.1;
        panel.add(tableNameLabel, constraints);

        JComboBox<String> tableNameBox = new JComboBox<>(Tables.getTablesName());
        tableNameBox.setFont(font);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        panel.add(tableNameBox, constraints);

        JButton showTableDataButton = new JButton("Показать данные в таблице");
        showTableDataButton.setFont(font);
        showTableDataButton.setPreferredSize(new Dimension(250, 30));
        showTableDataButton.addActionListener(e -> {
            List<QueryRow> rows = window.getTableData((String)tableNameBox.getSelectedItem());
            if(rows.isEmpty()){
                JOptionPane.showMessageDialog(window ,"Не удалось получить данные", "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            }
            QueryInfoWindow queryInfoWindow = new QueryInfoWindow(window, rows, true, (String)tableNameBox.getSelectedItem());
            queryInfoWindow.showWindow();
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(showTableDataButton, constraints);

        JButton showQueriesWindow = new JButton("Окно запросов");
        showQueriesWindow.setFont(font);
        showQueriesWindow.setPreferredSize(new Dimension(150, 30));
        showQueriesWindow.addActionListener(e -> {
            QueriesWindow queriesWindow = new QueriesWindow(window);
            queriesWindow.showWindow();
        });
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(showQueriesWindow, constraints);

        JButton manualQueriesButton = new JButton("Manual SQL");
        manualQueriesButton.setFont(font);
        manualQueriesButton.setPreferredSize(new Dimension(150, 30));
        manualQueriesButton.addActionListener(e -> {
            ManualQueriesWindow manualQueriesWindow = new ManualQueriesWindow(window);
            manualQueriesWindow.showWindow();
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(manualQueriesButton, constraints);

        //сделать checkbox
        JCheckBox cachingChecker = new JCheckBox("Кэширование запросов");
        cachingChecker.setBackground(Color.PINK);
        cachingChecker.setFont(font);
        cachingChecker.setSelected(true);
        cachingChecker.addItemListener(e -> switchMode(e.getStateChange() == ItemEvent.SELECTED));
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(cachingChecker, constraints);

        panel.setPreferredSize(new Dimension(window.getWidth()-40, window.getHeight()-150));

        return panel;
    }

    private void switchMode(boolean state){
        window.setCaching(state);
    }
}
