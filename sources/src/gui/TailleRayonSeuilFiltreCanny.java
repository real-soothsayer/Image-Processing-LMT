package gui;

import image.IImage;
import image.IPixel;
import image.Pixel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class TailleRayonSeuilFiltreCanny extends JDialog implements ActionListener, DocumentListener {
	private JTextField textField;
	private JTextField sigma;
	private JTextField seuilInf;
	private JTextField seuilSup;
	
	private JLabel label = new JLabel("5 x 5");
	
	private JButton okButton;
	private JButton cancelButton;

	private ImageNiveauGris img;
	private IImage imgFiltree;
	private Dessiner panelD;
	private JFrame main;
	private JButton btnAfficherLeMasque;
	/**
	 * Launch the application.
	 *//*
	public static void main(String[] args) {
		try {
			TailleRayonSeuilFiltreCanny dialog = new TailleRayonSeuilFiltreCanny(null, null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public TailleRayonSeuilFiltreCanny(ImageNiveauGris img, IImage imgFiltree, Dessiner panelD, JFrame main) {
		setModal(true);

		this.img = img;
		this.imgFiltree = imgFiltree;
		this.panelD = panelD;
		this.main = main;
		
		setTitle("Caract\u00E9ristiques du filtre de canny");
		setBounds(100, 100, 526, 159);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(this);
				{
					btnAfficherLeMasque = new JButton("Afficher le masque gaussien");
					btnAfficherLeMasque.addActionListener(this);
					btnAfficherLeMasque.setBackground(Color.BLACK);
					buttonPane.add(btnAfficherLeMasque);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(panel, BorderLayout.NORTH);
			panel.setLayout(new FlowLayout());
			{
				JLabel lblSigmapourFiltre = new JLabel("Sigma (pour filtre gaussien)");
				panel.add(lblSigmapourFiltre);
			}
			{
				sigma = new JTextField();
				panel.add(sigma);
				sigma.setText("1.3");
				sigma.setColumns(10);
			}
			{
				JLabel lblTailleDuFiltre = new JLabel("Taille du filtre gaussien");
				lblTailleDuFiltre.setHorizontalAlignment(SwingConstants.LEFT);
				panel.add(lblTailleDuFiltre);
			}
			{
				textField = new JTextField();
				textField.getDocument().addDocumentListener(this);
				textField.setText("5");
				textField.setColumns(10);
				panel.add(textField);
			}
			{
				panel.add(label);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			{
				JLabel lblSeuilInfrieur = new JLabel("Seuil inf\u00E9rieur");
				panel.add(lblSeuilInfrieur);
			}
			{
				seuilInf = new JTextField();
				seuilInf.setText("40");
				panel.add(seuilInf);
				seuilInf.setColumns(10);
			}
			{
				JLabel lblSeuilSuprieur = new JLabel("Seuil sup\u00E9rieur");
				panel.add(lblSeuilSuprieur);
			}
			{
				seuilSup = new JTextField();
				seuilSup.setText("100");
				panel.add(seuilSup);
				seuilSup.setColumns(10);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cancelButton)) {
			this.dispose();
		} else if (e.getSource().equals(okButton)) {
			int seuilInferieur = Integer.parseInt(seuilInf.getText());
			int seuilSuperieur = Integer.parseInt(seuilSup.getText());
			
			if (seuilSuperieur > seuilInferieur) {
				int hauteurMasque = Integer.parseInt(textField.getText());
				int largeurMasque = Integer.parseInt(textField.getText());
				
				//preparation du masque du filtre gaussien
				double[][] masque = this.getMasque();
				
				//1- application du filtre gaussien
				double imgCon[][] = img.convolution(masque);
	
				int nbLign = imgCon.length;
				int nbCol = imgCon[0].length;
				IPixel[][] imageFP = new IPixel[nbLign][nbCol];
				//conversion en un tableau de IPixel
				for (int i = 0; i < nbLign; i++) {
					for (int j = 0; j < nbCol; j++) {
						imageFP[i][j] = new Pixel();
						short valeur = (short) Math.round(imgCon[i][j]);
						if (valeur < 0) {
							valeur = 0;
						} else if (valeur > 255) {
							valeur = 255;
						}
						imageFP[i][j].setValue(valeur);
					}
				}

				ImageNiveauGris imageLissee = new ImageNiveauGris();
				imageLissee.setImage(imageFP);
				imageLissee.setNombreColonnes(nbCol);
				imageLissee.setNombreLignes(nbLign);
				
				double[][] gradientVertical = imageLissee.convolution(new double[][]{{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}});
				double[][] gradientHorizontal = imageLissee.convolution(new double[][]{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}});
				//2- application du gradient
				for (int i = 0; i < nbLign; i++) {
					for (int j = 0; j < nbCol; j++) {
						imgCon[i][j] = Math.sqrt(Math.pow(gradientVertical[i][j], 2) + Math.pow(gradientHorizontal[i][j], 2));
					}
				}

				for (int i = 0; i < nbLign; i++) {
					for (int j = 0; j < nbCol; j++) {
						imageFP[i][j] = new Pixel();
						short valeur = (short) Math.round(imgCon[i][j]);
						double angle = Math.PI/2;
						if (gradientHorizontal[i][j] != 0) {
							angle = Math.atan2(gradientVertical[i][j], gradientHorizontal[i][j]);
						}
						
						if (isMaximum(imgCon, i, j, angle)) { //pour faire la suppression des non-maxima on teste d'abord si c'est un maximum
							if (valeur < 0) {
								valeur = 0;
							} else if (valeur > 255) {
								valeur = 255;
							}
							imageFP[i][j].setValue(valeur);
						} else {
							imageFP[i][j].setValue((short) 0);
						}
					}
				}
				
				// double seuillage
				int seuilInf = Integer.parseInt(this.seuilInf.getText());
				int seuilSup = Integer.parseInt(this.seuilSup.getText());
				for (int i = 0; i < nbLign; i++) {
					for (int j = 0; j < nbCol; j++) {
						if (imageFP[i][j].getValue() < seuilInf) {
							imageFP[i][j].setValue((short) 0);
						} else if (imageFP[i][j].getValue() > seuilSup) {
							imageFP[i][j].setValue((short) 255);
						}
					}
				}
				
				// hysteresis
				for (int k = 0; k < (nbLign + nbCol)/4; k++) {
					for (int i = 0; i < nbLign; i++) {
						for (int j = 0; j < nbCol; j++) {
							if (imageFP[i][j].getValue() >= seuilInf && imageFP[i][j].getValue() <= seuilSup) {
								if (imageFP[i][j].getValue() != 0 && imageFP[i][j].getValue() != 255) {
									if (imageFP[i-1][j-1].getValue() == 255 || imageFP[i-1][j].getValue() == 255 || imageFP[i-1][j+1].getValue() == 255 || imageFP[i][j-1].getValue() == 255 || imageFP[i][j+1].getValue() == 255 || imageFP[i+1][j-1].getValue() == 255 || imageFP[i+1][j].getValue() == 255 || imageFP[i+1][j+1].getValue() == 255) {
										imageFP[i][j].setValue((short) 255);
									}
								}
							}
						}
					}
				}
				for (int i = 0; i < nbLign; i++) {
					for (int j = 0; j < nbCol; j++) {
						if (imageFP[i][j].getValue() != 0 && imageFP[i][j].getValue() != 255) {
							imageFP[i][j].setValue((short) 0);
						}
					}
				}
				
				imgFiltree.setImage(imageFP);
				imgFiltree.setNombreColonnes(nbCol);
				imgFiltree.setNombreLignes(nbLign);
				
				short[][] imageFS = new short[nbLign][nbCol];
				for (int i = 0; i < nbLign; i ++) {
					for (int j = 0; j < nbCol; j++) {
						imageFS[i][j] = imageFP[i][j].getValue();
					}
				}
				
				panelD.setImage(imageFS);
				panelD.repaint();
				main.repaint();
				
				this.dispose();
			}
		} else if (e.getSource().equals(btnAfficherLeMasque)) {
			try {
				MasqueGaussien masq = new MasqueGaussien(this.getMasqueInt());
				masq.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				masq.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private int[][] getMasqueInt() {
		int hauteurMasque = Integer.parseInt(textField.getText());
		int largeurMasque = Integer.parseInt(textField.getText());
		double masque[][] = new double[hauteurMasque][largeurMasque];
		
		int val[][] = new int[hauteurMasque][largeurMasque];
		
		double facteur = 0.0;
		
		for(int i = 0; i < hauteurMasque; i++) {
			for (int j = 0; j < largeurMasque; j++) {
				int x = j - largeurMasque/2;
				int y = i - hauteurMasque/2;
				double sig = Double.parseDouble(sigma.getText());
				masque[i][j] = (1/(2*Math.PI*sig*sig))*(Math.exp(-(x*x + y*y)/(2*sig*sig)));//val
				if (i == 0 && j == 0) {
					facteur = 1.0/masque[i][j];
				}
				val[i][j] = (int) Math.round(masque[i][j] * facteur);
			}
		}
		
		return val;
	}
	
	private double[][] getMasque() {
		int hauteurMasque = Integer.parseInt(textField.getText());
		int largeurMasque = Integer.parseInt(textField.getText());
		double masque[][] = new double[hauteurMasque][largeurMasque];
		
		int val[][] = getMasqueInt();
		
		int somme = 0;
		
		for(int i = 0; i < hauteurMasque; i++) {
			for (int j = 0; j < largeurMasque; j++) {
				somme += val[i][j];
			}
		}
		
		for (int i = 0; i < hauteurMasque; i++) {
			for (int j = 0; j < largeurMasque; j++) {
				masque[i][j] = (double) val[i][j]/somme;
			}
		}
		
		return masque;
	}
	
	private boolean isMaximum(double[][] im, int i, int j, double angle) {
			int nbLign = im.length;
			int nbCol = im[0].length;
		if (i < nbLign - 1  && i > 0 && j < nbCol - 1 && j > 0) {
			//pixel central
			double pixel = im[i][j];
			//pixel alentours
			double pixelHG = im[i-1][j-1];
			double pixelHM = im[i-1][j];
			double pixelHD = im[i-1][j+1];
			double pixelMG = im[i][j-1];
			double pixelMD = im[i][j+1];
			double pixelBG = im[i+1][j-1];
			double pixelBM = im[i+1][j];
			double pixelBD = im[i+1][j+1];
			
			boolean ret = false;
//			System.out.println(angle/Math.PI);
			if ((angle < Math.PI/8 && angle > -Math.PI/8) || (angle < -(7*Math.PI/8)) || (angle > (7*Math.PI/8))) { // direction horizontale
				if (pixelMG <= pixel && pixelMD <= pixel) {
					ret = true;
				}
			} else if ((angle <= (3*Math.PI/8) && angle >= Math.PI/8) || (angle <= -(5*Math.PI/8) && angle >= -(7*Math.PI/8))) { //direction +45 degres par rapport a l'horizontale
				if (pixelHD <= pixel && pixelBG <= pixel) {
					ret = true;
				}
			} else if ((angle >= -(3*Math.PI/8) && angle <= -Math.PI/8) || (angle >= 5*Math.PI/8 && angle <= 7*Math.PI/8)) { //direction -45 degres par rapport a l'horizontale
				if (pixelHG <= pixel && pixelBD <= pixel) {
					ret = true;
				}
			} else if ((angle > (3*Math.PI/8) && angle < (5*Math.PI/2)) || (angle < -(3*Math.PI/8) && angle > -(5*Math.PI/2))) { // direction verticale
				if (pixelHM <= pixel && pixelBM <= pixel) {
					ret = true;
				}
			}
			
			return ret;
		} else {
			return false;
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		String taille = textField.getText();
		label.setText(taille + " x " + taille);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		String taille = textField.getText();
		label.setText(taille + " x " + taille);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

}
