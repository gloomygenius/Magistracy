package controller;

import gui.Diagram;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@AllArgsConstructor
public class OpenFileChooser implements ActionListener {
    private JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int ret = fileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            Diagram diagram = new Diagram();
            diagram.setFile(fileChooser.getSelectedFile());
            diagram.setFrame(frame);
            Thread thread = new Thread(diagram);
            thread.start();
        }
    }
}