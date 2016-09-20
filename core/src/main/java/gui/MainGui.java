package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGui  implements ActionListener {
    JFrame frame;
    Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
    Font mainFont=new Font("Verdana", Font.ROMAN_BASELINE, 17);
    public static void main(String[] args) {
        MainGui gui = new MainGui();
        gui.createGui();
    }

    public void createGui() {
        frame = new JFrame("NASA data miner. beta v0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.orange);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(mainFont);
        fileMenu.setMnemonic('F');
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new FileDialogListener());
        JMenuItem downloadMenuItem = new JMenuItem("Download");
        downloadMenuItem.addActionListener(new DownloadFrame());
        JMenuItem closeMenuItem = new JMenuItem("Close");
        fileMenu.add(openMenuItem);
        fileMenu.add(downloadMenuItem);
        fileMenu.add(closeMenuItem);
        menuBar.add(fileMenu);
        //set Windows style
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        JPanel panel = new JPanel();
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.setSize(sSize);
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent event) {
        frame.repaint();
    }

    public class FileDialogListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            JFileChooser choose = new JFileChooser("./data");
            choose.showSaveDialog(frame);
            choose.getSelectedFile();
        }
    }
}