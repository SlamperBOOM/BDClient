package org.slamperboom.graphics;

import org.slamperboom.model.processing.QueryRow;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

public class QueryInfoWindow extends JFrame {
    private final List<QueryRow> rows;
    private final JTable table;
    private final int calculatedWidth;
    private final String tableName;
    protected boolean isEditable;

    public QueryInfoWindow(MainWindow window, List<QueryRow> rows, boolean isEditable, String tableName){
        this.rows = rows;
        this.tableName = tableName;
        this.isEditable = isEditable;
        //1-я строчка - названия столбцов
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        calculatedWidth = Math.min(100 + rows.get(0).getLength()*150, screenWidth);
        String[][] tableRows = new String[rows.size()][rows.get(0).getLength()];
        for(int i=0; i<rows.size(); ++i){
            for(int j=0; j<rows.get(i).getLength(); ++j){
                tableRows[i][j] = rows.get(i).getColumn(j);
            }
        }
        table = new JTable(Arrays.copyOfRange(tableRows, 1, tableRows.length), tableRows[0]);
        table.setRowHeight(30);
        table.setPreferredSize(new Dimension(rows.get(0).getLength()*150-20, table.getRowHeight()*table.getRowCount()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        scrollPane.setPreferredSize(new Dimension(calculatedWidth-100, 500));
        setSize( calculatedWidth, 700);
        setTitle(tableName);
        setLocation((screenWidth-getSize().width)/2, window.getLocation().y);
        setResizable(false);
        setContentPane(isEditable ?
                getEditableContentPane(scrollPane, window)
                : getNotEditableContentPane(scrollPane, window));
        setBackground(Color.PINK);
    }

    private JTabbedPane getEditableContentPane(JScrollPane scrollPane, MainWindow window){
        JTabbedPane tabbedPane = new JTabbedPane();
        //панель с данными
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(Color.PINK);
        GridBagConstraints constraints = new GridBagConstraints();
        dataPanel.setPreferredSize(new Dimension(calculatedWidth-50, 520));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 5;
        constraints.weighty = 0.5;
        dataPanel.add(scrollPane, constraints);

        JButton deleteButton = new JButton("Удалить выбранную запись");
        deleteButton.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Запись будет удалена. Вы уверены?", "Подтверждение",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                window.makeDelete(tableName, rows.get(table.getSelectedRow()+1).getColumn(0), this);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridheight = 1;
        dataPanel.add(deleteButton, constraints);

        tabbedPane.add(dataPanel, "Данные");
        //панель добавление записи
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBackground(Color.PINK);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        Font font = new Font("TimesRoman", Font.PLAIN, 14);
        List<JTextField> fields = new ArrayList<>();
        for(int i=0; i<rows.get(0).getLength(); ++i){
            JLabel label = new JLabel(rows.get(0).getColumn(i) + ": ");
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setFont(font);
            constraints.gridx = 0;
            constraints.gridy = i;
            addPanel.add(label, constraints);

            JTextField field = new JTextField(15);
            field.setFont(font);
            constraints.gridx = 1;
            constraints.gridy = i;
            fields.add(field);
            addPanel.add(field, constraints);
        }
        constraints.gridy = rows.get(0).getLength();
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        addPanel.add(new JPanel(), constraints);

        JButton addButton = new JButton("Добавить запись");
        addButton.addActionListener(e ->{
            List<String> columns = new ArrayList<>();
            for (JTextField field : fields) {
                columns.add(field.getText());
            }
            window.makeInsert(tableName, columns, this);
        });

        constraints.gridy = rows.get(0).getLength()+1;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        addPanel.add(addButton, constraints);
        tabbedPane.add(addPanel, "Добавление записи");

        table.getModel().addTableModelListener(e -> {

            String columnName = rows.get(0).getColumn(e.getColumn());
            String recordID = rows.get(e.getFirstRow()+1).getColumn(0);
            String value = (String)((TableModel)e.getSource()).getValueAt(e.getFirstRow(), e.getColumn());

            window.makeUpdate(tableName, recordID, columnName, value);
        });
        return tabbedPane;
    }

    private JPanel getNotEditableContentPane(JScrollPane scrollPane, MainWindow window){
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(Color.PINK);
        GridBagConstraints constraints = new GridBagConstraints();
        dataPanel.setPreferredSize(new Dimension(calculatedWidth-50, 520));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 5;
        constraints.weighty = 0.5;
        dataPanel.add(scrollPane, constraints);

        table.setCellEditor(new TableCellEditor() {
            JComponent component = new JTextField();
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return (component);
            }

            @Override
            public Object getCellEditorValue() {
                return ((JTextField)component).getText();
            }

            @Override
            public boolean isCellEditable(EventObject anEvent) {
                return false;
            }

            @Override
            public boolean shouldSelectCell(EventObject anEvent) {
                return true;
            }

            @Override
            public boolean stopCellEditing() {
                return false;
            }

            @Override
            public void cancelCellEditing() {

            }

            @Override
            public void addCellEditorListener(CellEditorListener l) {

            }

            @Override
            public void removeCellEditorListener(CellEditorListener l) {

            }
        });

        JButton exportButton = new JButton("Экспорт в Excel");
        exportButton.addActionListener(e -> {
            if(window.exportData(rows)){
                JOptionPane.showMessageDialog(this, "Данные успешно экспортированы в файл output.txt в формате Excel. " +
                                "Можете скопировать эти данные в таблицу",
                        "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this, "Не удалось экспортировать данные в файл",
                        "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridheight = 1;
        dataPanel.add(exportButton, constraints);
        return dataPanel;
    }

    public void showWindow(){
        setVisible(true);
    }
}
