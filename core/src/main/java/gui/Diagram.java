package gui;

import logic.CsvConverter;
import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;

@Setter
public class Diagram implements Runnable {
    File file;
    JFrame frame;

    private static JPanel getDiagram(Map<LocalDateTime, Double> map, String parameter) {
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
        Map<LocalDateTime, Double> map = CsvConverter.from(file);
        frame.getContentPane().add(BorderLayout.CENTER, Diagram.getDiagram(map, "Parameter"));
        frame.validate();
    }
}