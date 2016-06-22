package gui;
import image.ElementStructurant;
import image.IImage;
import image.IPixel;
import image.Pixel;
import imageBinaire.ImageBinaire;

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

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class TailleTypeMorphologie extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JButton cancelButton;
	private JButton okButton;
	private ImageBinaire img;
	private IImage imgFiltree;
	private Dessiner panelD;
	private JFrame main;
	private String type;
	private JSpinner tailleEltStruct;
	private JComboBox comboBox;
	/*
	public static void main(String args[]) {
		TailleFiltre tf = new TailleFiltre(null, null, null, null);
		tf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}*/

	public TailleTypeMorphologie(ImageBinaire img, IImage imgFiltree, Dessiner panelD, JFrame main, String type) {
		setModal(true);

		this.img = img;
		this.imgFiltree = imgFiltree;
		this.panelD = panelD;
		this.main = main;
		this.type = type;
		
		setTitle("Element structurant");
		setBounds(100, 100, 215, 109);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			comboBox = new JComboBox();
			comboBox.setModel(new DefaultComboBoxModel(new String[] {"Etoile", "Carré", "Ligne", "Colonne"}));
			contentPanel.add(comboBox);
		}
		{
			tailleEltStruct = new JSpinner();
			tailleEltStruct.setPreferredSize(new Dimension(50, 20));
			tailleEltStruct.setModel(new SpinnerNumberModel(new Integer(3), new Integer(1), null, new Integer(2)));
			contentPanel.add(tailleEltStruct);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(this);
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cancelButton)) {
			this.dispose();
		} else if (e.getSource().equals(okButton)) {
			ElementStructurant eltStruct = new ElementStructurant((Integer) tailleEltStruct.getValue(), (String) comboBox.getSelectedItem());
			
			IImage imgMorph = null;
			if (this.type.equals("dil")) {
				imgMorph = img.Dilatation(eltStruct);
			} else if (this.type.equals("ero")) {
				imgMorph = img.Erosion(eltStruct);
			} else if (this.type.equals("ouv")) {
				imgMorph = img.Ouverture(eltStruct);
			} else if (this.type.equals("fer")) {
				imgMorph = img.Fermeture(eltStruct);
			} else if (this.type.equals("gra")) {
				imgMorph = img.GradiantMorphologique(eltStruct);
			}
			
			int nbLign = imgMorph.getNombreLignes();
			int nbCol = imgMorph.getNombreColonnes();
			IPixel[][] imageFP = new IPixel[nbLign][nbCol];
			for (int i = 0; i < nbLign; i++) {
				for (int j = 0; j < nbCol; j++) {
					imageFP[i][j] = new Pixel();
					imageFP[i][j].setValue(imgMorph.getImage()[i][j].getValue());
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
	}
}
