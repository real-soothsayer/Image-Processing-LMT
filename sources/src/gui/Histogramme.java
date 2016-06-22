package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Histogramme extends JDialog implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double[] image;

	/**
	 * Launch the application.
	 *//*
	public static void main(String[] args) {
		try {
			short[][] test = {{4, 8, 6}, {4, 5, 0}, {1, 2, 9}};
			Histogramme dialog = new Histogramme(test);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public Histogramme(short[][] image) {
		this.image = new double[image.length*image[0].length];
		int n = 0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				this.image[n] = image[i][j];
				n++;
			}
		}
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}

		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			
			
	        HistogramDataset dataset = new HistogramDataset();
//	      dataset.setType(HistogramType.RELATIVE_FREQUENCY);
	        dataset.addSeries("", this.image, 256, 0.0, 255.0);
//	      dataset.addSeries("", this.image, 128);
	        JFreeChart chart = ChartFactory.createHistogram(
	                "Histogramme de l'image A", 
	                null, 
	                null, 
	                dataset, 
	                PlotOrientation.VERTICAL, 
	                false, 
	                false, 
	                false
	            );
	      
	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        chartPanel.setMouseZoomable(true, false);
	        
	        panel.add(chartPanel, BorderLayout.CENTER);
	        
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}

}
