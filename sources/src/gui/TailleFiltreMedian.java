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

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.Dimension;

public class TailleFiltreMedian extends JDialog implements ActionListener{

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
	
	public static void main(String args[]) {
		TailleFiltreMedian tf = new TailleFiltreMedian(null, null, null, null);
		tf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}

	public TailleFiltreMedian(ImageNiveauGris img, IImage imgFiltree, Dessiner panelD, JFrame main) {
		setModal(true);

		this.img = img;
		this.imgFiltree = imgFiltree;
		this.panelD = panelD;
		this.main = main;
		
		setTitle("taille du filtre");
		setBounds(100, 100, 215, 109);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			tailleFiltre = new JSpinner();
			tailleFiltre.setPreferredSize(new Dimension(50, 20));
			tailleFiltre.setModel(new SpinnerNumberModel(new Integer(3), new Integer(1), null, new Integer(2)));
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
			IImage imgFil = img.filtreMedian((Integer) tailleFiltre.getValue());

			int nbLign = imgFil.getNombreLignes();
			int nbCol = imgFil.getNombreColonnes();
			IPixel[][] imageFP = new IPixel[nbLign][nbCol];
			for (int i = 0; i < nbLign; i++) {
				for (int j = 0; j < nbCol; j++) {
					imageFP[i][j] = new Pixel();
					imageFP[i][j].setValue(imgFil.getImage()[i][j].getValue());
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
