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
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;
import java.awt.Color;

public class TailleRayonFiltre extends JDialog implements ActionListener, DocumentListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	private JButton cancelButton;
	private JButton okButton;
	
	private JLabel lblX = new JLabel("3 x 3");
	private JLabel lblTaille;
	private JPanel panel;
	private JLabel lblSigma;
	private JTextField sigma;

	private ImageNiveauGris img;
	private IImage imgFiltree;
	private Dessiner panelD;
	private JFrame main;
	private JButton btnAffichierLeMasque;
	/*
	public static void main(String args[]) {
		TailleRayonFiltre tf = new TailleRayonFiltre(null, null, null, null);
		tf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}*/

	public TailleRayonFiltre(ImageNiveauGris img, IImage imgFiltree, Dessiner panelD, JFrame main) {
		setModal(true);

		this.img = img;
		this.imgFiltree = imgFiltree;
		this.panelD = panelD;
		this.main = main;
		
		setTitle("parametres du filtre");
		setBounds(100, 100, 328, 161);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			textField = new JTextField();
			textField.getDocument().addDocumentListener(this);
			{
				lblTaille = new JLabel("Taille");
				lblTaille.setHorizontalAlignment(SwingConstants.LEFT);
				contentPanel.add(lblTaille);
			}
			textField.setText("3");
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			contentPanel.add(lblX);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(this);
				{
					btnAffichierLeMasque = new JButton("Affichier le masque gaussien");
					btnAffichierLeMasque.addActionListener(this);
					btnAffichierLeMasque.setBackground(Color.BLACK);
					buttonPane.add(btnAffichierLeMasque);
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
			panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				lblSigma = new JLabel("Sigma");
				panel.add(lblSigma);
			}
			{
				sigma = new JTextField();
				sigma.setText("1.3");
				panel.add(sigma);
				sigma.setColumns(10);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cancelButton)) {
			this.dispose();
		} else if (e.getSource().equals(okButton)) {
			double[][] masque = this.getMasque();
			
			double imgCon[][] = img.convolution(masque);

			int nbLign = imgCon.length;
			int nbCol = imgCon[0].length;
			IPixel[][] imageFP = new IPixel[nbLign][nbCol];
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
		} else if (e.getSource().equals(btnAffichierLeMasque)) {
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

	@Override
	public void insertUpdate(DocumentEvent e) {
		String taille = textField.getText();
		lblX.setText(taille + " x " + taille);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		String taille = textField.getText();
		lblX.setText(taille + " x " + taille);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

}
