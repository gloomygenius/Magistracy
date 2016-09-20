package gui;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloadFrame implements ActionListener {
    private JFrame downloadWindow;
    private JFormattedTextField latStart;
    private JFormattedTextField latEnd;
    private JFormattedTextField lonStart;
    private JFormattedTextField lonEnd;
    private JFormattedTextField timeStart;
    private JFormattedTextField timeEnd;
    private JFormattedTextField fieldStart;
    private JFormattedTextField fieldEnd;
    private JTextArea link;
    private Font mainFont = new Font("Verdana", Font.ROMAN_BASELINE, 17);

    @Override
    public void actionPerformed(ActionEvent event) {
        downloadWindow = new JFrame("Download data");
        downloadWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWight = 500;
        int windowHeight = 500;
        downloadWindow.setMinimumSize(new Dimension(windowWight, windowHeight));

        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel commonPanel = new JPanel();
        JTextArea infoText = createTextArea(
                "Введите дату начала и конца наблюдений (не раньше 24.02.2000), а также дипазон широты и долготы. " +
                        "Затем нажмите кнопку \"Сгенерировать ссылку\" и скачайте файл по полученной ссылке. " +
                        "При необходимости авторизации введите логин master17" +
                        " и пароль magistracy2017");
        infoText.setSize(windowWight - 50, windowHeight - 50);
        northPanel.add(infoText);

        //устанавливаем другой диспетчер компановки
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        //размещаем панели в окне
        downloadWindow.getContentPane().add(BorderLayout.NORTH, northPanel);
        downloadWindow.getContentPane().add(BorderLayout.CENTER, commonPanel);

        commonPanel.add(createPanel("Диапазон широты", latStart, latEnd, "##########"));
        latStart = fieldStart;
        latEnd = fieldEnd;
        commonPanel.add(createPanel("Диапазон долготы", lonStart, lonEnd, "##########"));
        lonStart = fieldStart;
        lonEnd = fieldEnd;
        commonPanel.add(createPanel("Диапазон времени", timeStart, timeEnd, "##.##.#### ##:##"));
        timeStart = fieldStart;
        timeStart.setText("24.02.2000 00:00");
        timeEnd = fieldEnd;
        downloadWindow.getContentPane().add(BorderLayout.SOUTH, southPanel);

        JButton buttonLink = new JButton("Сгенерировать ссылку");
        buttonLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonLink.addActionListener(new ButtonListener());
        southPanel.add(buttonLink);

        link = createTextArea(
                "Здесь будет ссылка");
        link.setSize(windowWight - 50, windowHeight - 50);
        link.setAlignmentX(Component.CENTER_ALIGNMENT);
        southPanel.add(link);


        downloadWindow.setVisible(true);

    }

    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(mainFont);
        return textArea;
    }

    private JPanel createPanel(String fieldName, JFormattedTextField listener1, JFormattedTextField listener2, String fieldFormat) {
        Dimension sizeOfField = new Dimension(100, 24);
        JPanel panel = new JPanel();
        JLabel name = new JLabel(fieldName);
        JLabel dash = new JLabel("  -  ");
        JFormattedTextField field1 = getDateField(fieldFormat);
        JFormattedTextField field2 = getDateField(fieldFormat);
        field1.setPreferredSize(sizeOfField);
        field2.setPreferredSize(sizeOfField);
        panel.add(name);
        panel.add(field1);
        panel.add(dash);
        panel.add(field2);
        this.fieldStart = field1;
        this.fieldEnd = field2;
        return panel;
    }

    private JFormattedTextField getDateField(String formatter) {
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
    class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){

            link.setText(
                    "Временной интервал: "+ timeStart.getText()+" - "+ timeEnd.getText()+"\n"+
                    "Диапазон широты: "+ latStart.getText()+" - "+ latEnd.getText()+"\n"+
                    "Диапазон долготы: "+ lonStart.getText()+" - "+ lonEnd.getText());
        }
    }
}