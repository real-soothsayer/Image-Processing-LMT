package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;
import image.IImage;
import image.IPixel;
import image.Pixel;

public class Rayon extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JButton cancelButton;
	private JButton okButton;
	private ImageNiveauGris img;
	private IImage imgFiltree;
	private Dessiner panelD;
	private JFrame main;
	private JSpinner tailleFiltre;
	private String passeHautBas;
	/*
	public static void main(String args[]) {
		Angle tf = new Angle(null, null, null, null);
		tf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}*/

	public Rayon(ImageNiveauGris img, IImage imgFiltree, Dessiner panelD, JFrame main, String passeHautBas) {
		setModal(true);

		this.img = img;
		this.imgFiltree = imgFiltree;
		this.panelD = panelD;
		this.main = main;
		this.passeHautBas = passeHautBas;
		
		setTitle("rayon du filtre");
		setBounds(100, 100, 215, 109);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			tailleFiltre = new JSpinner();
			tailleFiltre.setPreferredSize(new Dimension(50, 20));
			int taille = 2;
			int max = Math.max(img.getNombreColonnes(), img.getNombreLignes());
			while(taille<max){
				taille *= 2;
			}
			int rayonMax = taille/2;
			tailleFiltre.setModel(new SpinnerNumberModel(new Integer(rayonMax/2), new Integer(0), new Integer(rayonMax), new Integer(1)));
			contentPanel.add(tailleFiltre);
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
			IImage imgRot = null;
			if (passeHautBas.equals("bas")) {
				imgRot = img.filtrePasseBas((Integer) tailleFiltre.getValue());
			} else if (passeHautBas.equals("haut")) {
				imgRot = img.filtrePasseHaut((Integer) tailleFiltre.getValue());
			}
				
			int nbLign = imgRot.getNombreLignes();
			int nbCol = imgRot.getNombreColonnes();
			IPixel[][] imageFP = new IPixel[nbLign][nbCol];
			for (int i = 0; i < nbLign; i++) {
				for (int j = 0; j < nbCol; j++) {
					imageFP[i][j] = new Pixel();
					imageFP[i][j].setValue(imgRot.getImage()[i][j].getValue());
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
