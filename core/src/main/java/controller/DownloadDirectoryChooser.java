package controller;

import gui.DownloadFrame;
import logic.CsvConverter;
import logic.DataParser;
import logic.Downloader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class DownloadDirectoryChooser {
    private DownloadFrame frame;
    private JTextArea logout;
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
        String data = Downloader.getData(
                LinkAssamblerController.getLink(frame), logout);
        DataParser dataParser = new DataParser();
        Map<LocalDateTime, Double> dataMap = dataParser.parseToMap(data);
        String csvData = CsvConverter.of(dataMap);
        String savePath = fileChooser.getSelectedFile().getAbsolutePath() +
                "\\" +
                dataParser.getParameter() +
                dataParser.getTimeStart().toLocalDate().toString().replace(':', ' ') +
                dataParser.getTimeEnd().toLocalDate().toString().replace(':', ' ') +
                ".csv";
        try (FileWriter fileWriter = new FileWriter(savePath)) {
            fileWriter.write(csvData);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}