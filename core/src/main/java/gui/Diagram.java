package gui;

import javax.swing.*;

import logic.DataParserCSV;
import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Setter
public class Diagram implements Runnable {
    File file;
    JFrame frame;

    private static JPanel getDiagram(HashMap<LocalDateTime, Double> map, String parameter) {
        JPanel panel = new JPanel();
        XYSeries series = new XYSeries(parameter);

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            series.add(((LocalDateTime) pair.getKey()).toEpochSecond(ZoneOffset.UTC), (Double) pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        XYDataset xyDataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory
                .createXYLineChart(parameter, "x", "y",
                        xyDataset,
                        PlotOrientation.VERTICAL,
                        true, true, true);
        panel.add(new ChartPanel(chart));
        return panel;
    }

    @Override
    public void run() {
        HashMap<LocalDateTime, Double> map = DataParserCSV.parseFromCSV(file);
        frame.getContentPane().add(BorderLayout.CENTER, Diagram.getDiagram(map, "Parameter"));
        frame.validate();
    }
}