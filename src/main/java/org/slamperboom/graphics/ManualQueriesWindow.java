package org.slamperboom.graphics;

import org.slamperboom.model.processing.QueryRow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ManualQueriesWindow extends JFrame {

    public ManualQueriesWindow(MainWindow window){
        setSize(600, 500);
        setTitle("Manual SQL");
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add(getSelectPanel(window), "SELECT");
        tabbedPane.add(getUpdatePanel(window), "UPDATE");

        setContentPane(tabbedPane);
        setLocation(window.getLocation().x+50, window.getLocation().y);
    }

    private JPanel getSelectPanel(MainWindow window){
        JPanel selectPanel = new JPanel(new GridBagLayout());
        selectPanel.setBackground(Color.PINK);
        GridBagConstraints constraints = new GridBagConstraints();

        JTextArea selectArea = new JTextArea(20, 50);
        selectArea.setBackground(Color.pink);
        selectArea.setLineWrap(true);
        selectArea.setWrapStyleWord(true);
        selectArea.setEditable(true);
        selectArea.setFont(new Font("TimesRoman", Font.PLAIN, 16));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 10;
        constraints.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(selectArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(getSize().width-30, getSize().height-100));
        selectPanel.add(scrollPane, constraints);

        JButton selectButton = new JButton("Выполнить запрос");
        selectButton.setPreferredSize(new Dimension(100, 30));
        selectButton.addActionListener(e -> {
            List<QueryRow> rows = window.makeManualSelect(selectArea.getText());
            if(rows.isEmpty()){
                JOptionPane.showMessageDialog(this, "Не удалось получить данные", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            QueryInfoWindow queryInfoWindow = new QueryInfoWindow(window, rows, false, "Query");
            queryInfoWindow.showWindow();
        });
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridheight = 1;
        selectPanel.add(selectButton, constraints);
        selectPanel.setSize(550, 460);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                scrollPane.setPreferredSize(new Dimension(getSize().width-30, getSize().height-150));
            }
        });
        return selectPanel;
    }

    private JPanel getUpdatePanel(MainWindow window){
        JPanel updatePanel = new JPanel(new GridBagLayout());
        updatePanel.setBackground(Color.PINK);
        GridBagConstraints constraints = new GridBagConstraints();

        JTextArea updateArea = new JTextArea(20, 50);
        updateArea.setBackground(Color.PINK);
        updateArea.setLineWrap(true);
        updateArea.setWrapStyleWord(true);
        updateArea.setEditable(true);
        updateArea.setFont(new Font("TimesRoman", Font.PLAIN, 16));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 10;
        constraints.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(updateArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(getSize().width-30, getSize().height-100));
        updatePanel.add(scrollPane, constraints);

        JButton selectButton = new JButton("Выполнить запрос");
        selectButton.setPreferredSize(new Dimension(100, 30));
        selectButton.addActionListener(e -> window.makeManualUpdate(updateArea.getText()));
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridheight = 1;
        updatePanel.add(selectButton, constraints);
        updatePanel.setSize(550, 460);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                scrollPane.setPreferredSize(new Dimension(getSize().width-30, getSize().height-150));
            }
        });

        return updatePanel;
    }

    public void showWindow(){
        setVisible(true);
    }
}
