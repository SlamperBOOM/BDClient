package org.slamperboom.graphics;

import org.slamperboom.model.processing.ParameterForPredefinedCondition;
import org.slamperboom.model.processing.PredefinedQueryFactory;
import org.slamperboom.model.processing.QueryRow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QueriesWindow extends JFrame {

    public QueriesWindow(MainWindow window){
        JTabbedPane tabbedPane = new JTabbedPane();

        List<List<ParameterForPredefinedCondition>> conditionsTitles = PredefinedQueryFactory.getConditionsLabels();
        List<String> titles = PredefinedQueryFactory.getTitles();
        for(int i=0; i<conditionsTitles.size(); ++i){
            tabbedPane.add(createQueryPanel(conditionsTitles.get(i), titles.get(i), window, i+1),
                    titles.get(i));
        }
        setContentPane(tabbedPane);
        setSize(900, 550);
        setLocation(window.getLocation().x+50, window.getLocation().y);
        setResizable(false);
        setTitle("Predefined Queries");
        setBackground(Color.PINK);
    }

    private JPanel createQueryPanel(List<ParameterForPredefinedCondition> conditions, String title,
                                    MainWindow window, int queryID){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.PINK);
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("TimesRoman", Font.BOLD, 24));
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(titleLabel, constraints);

        JPanel spacePanel1 = new JPanel();
        spacePanel1.setBackground(Color.PINK);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(spacePanel1, constraints);

        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        Font font = new Font("TimesRoman", Font.PLAIN, 14);
        List<JComboBox<String>> fields = new ArrayList<>();
        for(int i=0; i<conditions.size(); ++i){
            JLabel label = new JLabel(conditions.get(i).getName() + ": ");
            label.setFont(font);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            constraints.gridx = 0;
            constraints.gridy = i+2;
            panel.add(label, constraints);

            JComboBox<String> field = new JComboBox<>(conditions.get(i).getVariants());
            field.setEditable(conditions.get(i).getVariants().length == 0);
            field.setFont(font);
            constraints.gridx = 1;
            constraints.gridy = i+2;
            fields.add(field);
            panel.add(field, constraints);
        }
        JPanel spacePanel2 = new JPanel();
        spacePanel2.setBackground(Color.PINK);
        constraints.gridx = 0;
        constraints.gridy = conditions.size()+2;
        constraints.gridwidth = 2;
        panel.add(spacePanel2, constraints);

        JButton button = new JButton("Выполнить запрос");
        button.setFont(font);
        button.addActionListener(e -> {
            List<String> conds = new ArrayList<>();
            for (JComboBox<String> field : fields) {
                String val = (String) field.getSelectedItem();
                conds.add(Objects.requireNonNullElse(val, ""));
            }
            String[] array = new String[conds.size()];
            array = conds.toArray(array);
            List<QueryRow> rows = window.makePredefinedQuery(queryID, array);
            if(rows.isEmpty()){
                JOptionPane.showMessageDialog(this, "Не удалось выполнить запрос или не нашлось подходящих данных." +
                                " Проверьте параметры",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            QueryInfoWindow queryInfoWindow = new QueryInfoWindow(window, rows, false, "Query");
            queryInfoWindow.showWindow();
        });
        constraints.gridx = 0;
        constraints.gridy = conditions.size()+3;
        panel.add(button, constraints);

        return panel;
    }

    public void showWindow(){
        setVisible(true);
    }
}
