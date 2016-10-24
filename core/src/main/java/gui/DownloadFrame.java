package gui;

import controller.LinkAssamblerController;
import logic.DataParserCSV;
import logic.Downloader;
import lombok.Getter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
public class DownloadFrame implements ActionListener {
    private JFrame downloadFrame;
    private Map<String, Boolean> checkBoxItems = new HashMap<>();
    private JTextField latitudeField;
    private JTextField longitudeField;
    private JTextField timeStartField;
    private JTextField timeEndField;
    private JTextField fieldStart;
    private JTextField fieldEnd;
    private JTextArea link;
    private Font mainFont = new Font("Verdana", Font.ROMAN_BASELINE, 17);
    private DownloadFrame thisFrame = this;

    @Override
    public void actionPerformed(ActionEvent event) {
        downloadFrame = new JFrame("Download data");
        downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWight = 500;
        int windowHeight = 500;
        downloadFrame.setMinimumSize(new Dimension(windowWight, windowHeight));

        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel commonPanel = new JPanel();
        JTextArea infoText = createTextArea(
                "Введите дату начала и конца наблюдений (не раньше 24.02.2000), а также дипазон широты и долготы. " +
                        "Затем нажмите кнопку \"Сгенерировать ссылку\" и скачайте файл по полученной ссылке. " +
                        "При необходимости авторизации введите логин master2017" +
                        " и пароль Magistracy2017");
        infoText.setSize(windowWight - 50, windowHeight - 50);
        northPanel.add(infoText);

        //устанавливаем другой диспетчер компановки
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        //размещаем панели в окне
        downloadFrame.getContentPane().add(BorderLayout.NORTH, northPanel);
        downloadFrame.getContentPane().add(BorderLayout.CENTER, commonPanel);
        commonPanel.add(createJCheckBoxPanel());

        commonPanel.add(createPanel("Широта", false));
        latitudeField = fieldStart;
        commonPanel.add(createPanel("Долгота", false));
        longitudeField = fieldStart;
        commonPanel.add(createPanel("Диапазон времени", true));
        timeStartField = fieldStart;
        timeStartField.setText("24.02.2000 00:00");
        timeEndField = fieldEnd;
        downloadFrame.getContentPane().add(BorderLayout.SOUTH, southPanel);

        JButton buttonLink = new JButton("Сгенерировать ссылку");
        buttonLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonLink.addActionListener((e) -> link.setText(LinkAssamblerController.getLink(thisFrame)));

        southPanel.add(buttonLink);

        link = createTextArea(
                "Здесь будет ссылка");
        link.setSize(windowWight - 50, windowHeight - 50);
        link.setAlignmentX(Component.CENTER_ALIGNMENT);
        southPanel.add(link);

        JButton buttonDownload = new JButton("Загрузить данные");
        buttonDownload.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDownload.addActionListener(new DownloadButtonListener());
        southPanel.add(buttonDownload);

        downloadFrame.setVisible(true);
    }

    private JPanel createJCheckBoxPanel() {
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        Properties prop = new Properties();
        System.out.println(new File("items.properties").getAbsolutePath());
        try {
            prop.load(getClass().getResourceAsStream("items.properties"));
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

    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(mainFont);
        return textArea;
    }

    private JPanel createPanel(String fieldName, boolean isDate) {
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

        this.fieldStart = field1;
        this.fieldEnd = field2;
        return panel;
    }

    private JFormattedTextField generateDateField(String formatter) {
        JFormattedTextField ftf = new JFormattedTextField();
        try {
            MaskFormatter mf = new MaskFormatter(formatter);
            mf.setPlaceholderCharacter('_');
            ftf = new JFormattedTextField(mf);
            return ftf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ftf;
    }

    private class DownloadButtonListener implements ActionListener, Runnable {

        @Override
        public void actionPerformed(ActionEvent event) {
            Thread thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv", "*.*");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.getCurrentDirectory();
            fileChooser.setFileFilter(filter);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String data = Downloader.getData(
                        LinkAssamblerController.getLink(thisFrame), link);
                DataParserCSV dataParser = new DataParserCSV();
                data = dataParser.parseAndConvertData(data);
                try (FileWriter fileWriter = new FileWriter(
                        fileChooser.getSelectedFile().getAbsolutePath() +
                                "\\" + dataParser.getParameter() + dataParser.getTimeStart().toString() + ".csv")) {
                    fileWriter.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}