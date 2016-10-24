package gui;

import controller.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui implements ActionListener {
    private JFrame frame;
    private Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Font mainFont = new Font("Verdana", Font.ROMAN_BASELINE, 17);

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.createGui();
    }

    private void createGui() {
        frame = new JFrame("NASA data miner. beta v0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set Windows style
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        frame.getContentPane().add(BorderLayout.NORTH, menuBar());
        frame.setSize(sSize);
        frame.setVisible(true);
    }

    private JMenuBar menuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.orange);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(mainFont);
        fileMenu.setMnemonic('F');
        JMenuItem openMenuItem = new JMenuItem("Open");

        openMenuItem.addActionListener(new FileChooser(frame));

        JMenuItem downloadMenuItem = new JMenuItem("Download");
        downloadMenuItem.addActionListener(new DownloadFrame());
        JMenuItem closeMenuItem = new JMenuItem("Close");
        fileMenu.add(openMenuItem);
        fileMenu.add(downloadMenuItem);
        fileMenu.add(closeMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    public void actionPerformed(ActionEvent event) {
        frame.repaint();
    }
}