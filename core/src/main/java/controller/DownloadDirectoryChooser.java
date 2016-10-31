package controller;

import controller.LinkAssamblerController;
import gui.DownloadFrame;
import logic.DataParserCSV;
import logic.Downloader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("Duplicates")
public class DownloadDirectoryChooser {
    private DownloadFrame frame;
    private JTextArea logout;
    private String data;
    private JFileChooser fileChooser = new JFileChooser();

    public DownloadDirectoryChooser(DownloadFrame frame, JTextArea logout) {
        this.frame = frame;
        this.logout = logout;
    }

    public void show() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv", "*.*");

        fileChooser.getCurrentDirectory();
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            new Thread(this::startDownload).start();

        }
    }

    private void startDownload() {
        data = Downloader.getData(
                LinkAssamblerController.getLink(frame), logout);
        DataParserCSV dataParser = new DataParserCSV();
        data = dataParser.parseAndConvertData(data);
        try (FileWriter fileWriter = new FileWriter(
                fileChooser.getSelectedFile().getAbsolutePath()
                        + "\\" + dataParser.getParameter()
                        + dataParser.getTimeStart().toString().replace(':', ' ') + ".csv")) {
            fileWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
