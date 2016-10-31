package gui;

import controller.DownloadDirectoryChooser;
import controller.LinkAssamblerController;
import lombok.Getter;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
public class DownloadFrame {
    private JFrame downloadFrame;
    private Map<String, Boolean> checkBoxItems = new HashMap<>();
    private Map<String, JTextField> textFieldMap = new HashMap<>();
    private JTextArea linkArea;
    private Font mainFont = new Font("Verdana", Font.ROMAN_BASELINE, 17);
    private DownloadFrame thisFrame = this;
    private Dimension screenSize = new Dimension(500, 500);

    void show() {
        generateFrameContainer();
        addInfoTextToFrame();
        addCommonPanel();
        addSouthPanel();
        downloadFrame.setVisible(true);
    }

    private void generateFrameContainer() {
        downloadFrame = new JFrame("Download data");
        downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        downloadFrame.setMinimumSize(screenSize);
    }

    private void addInfoTextToFrame() {
        JPanel northPanel = new JPanel();
        String text = "Введите дату начала и конца наблюдений (не раньше 24.02.2000), а также дипазон широты и " +
                "долготы. Затем нажмите кнопку \"Сгенерировать ссылку\" и скачайте файл по полученной ссылке. " +
                "При необходимости авторизации введите логин master2017 и пароль Magistracy2017";
        JTextArea infoText = createTextArea(text);
        infoText.setSize((int) (screenSize.getWidth() - 50), (int) (screenSize.getHeight() - 50));
        northPanel.add(infoText);
        downloadFrame.getContentPane().add(BorderLayout.NORTH, northPanel);
    }

    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(mainFont);
        return textArea;
    }

    private void addCommonPanel() {
        JPanel commonPanel = new JPanel();
        //устанавливаем другой диспетчер компановки
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        commonPanel.add(createJCheckBoxPanel());
        commonPanel.add(createFormPanel());
        downloadFrame.getContentPane().add(BorderLayout.CENTER, commonPanel);
    }

    private void addSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        downloadFrame.getContentPane().add(BorderLayout.SOUTH, southPanel);

        JButton buttonLink = new JButton("Сгенерировать ссылку");
        buttonLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonLink.addActionListener((e) -> linkArea.setText(LinkAssamblerController.getLink(thisFrame)));
        southPanel.add(buttonLink);

        linkArea = createTextArea("Здесь будет ссылка");
        linkArea.setSize((int) (screenSize.getWidth() - 50), (int) (screenSize.getHeight() - 50));
        linkArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        southPanel.add(linkArea);

        JButton buttonDownload = new JButton("Загрузить данные");
        buttonDownload.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDownload.addActionListener(e -> new DownloadDirectoryChooser(this, linkArea).show());
        southPanel.add(buttonDownload);
    }


    private JPanel createFormPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createPanelWithTextFields("Широта", false));
        panel.add(createPanelWithTextFields("Долгота", false));
        panel.add(createPanelWithTextFields("Диапазон времени", true));
        textFieldMap.get("Диапазон времени 1").setText("24.02.2000 00:00");

        return panel;
    }

    private JPanel createJCheckBoxPanel() {
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/gui/items.properties"));
            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);
                JCheckBox checkBox = new JCheckBox(value);
                checkBox.addItemListener((e) -> checkBoxItems.put(key, e.getStateChange() == ItemEvent.SELECTED));
                checkBoxPanel.add(checkBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checkBoxPanel;
    }


    private JPanel createPanelWithTextFields(String fieldName, boolean isDate) {
        Dimension sizeOfField = new Dimension(100, 24);
        JPanel panel = new JPanel();
        JLabel name = new JLabel(fieldName);
        JLabel dash = new JLabel("  -  ");
        JTextField field1;
        JTextField field2;
        if (isDate) {
            field1 = generateDateField("##.##.#### ##:##");
            field2 = generateDateField("##.##.#### ##:##");
            panel.add(name);
            panel.add(field1);
            panel.add(dash);
            panel.add(field2);
        } else {
            field1 = new JTextField();
            field2 = new JTextField();
            panel.add(name);
            panel.add(field1);
        }
        field1.setPreferredSize(sizeOfField);
        field2.setPreferredSize(sizeOfField);
        textFieldMap.put(fieldName + " 1", field1);
        textFieldMap.put(fieldName + " 2", field2);
        return panel;
    }

    private JFormattedTextField generateDateField(String formatter) {
        JFormattedTextField formattedTextField = new JFormattedTextField();
        try {
            MaskFormatter mf = new MaskFormatter(formatter);
            mf.setPlaceholderCharacter('_');
            formattedTextField = new JFormattedTextField(mf);
            return formattedTextField;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTextField;
    }
}