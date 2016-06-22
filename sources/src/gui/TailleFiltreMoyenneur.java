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

public class TailleFiltreMoyenneur extends JDialog implements ActionListener, DocumentListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	private JButton cancelButton;
	private JButton okButton;
	
	private JLabel lblX = new JLabel("3 x 3");
	private ImageNiveauGris img;
	private IImage imgFiltree;
	private Dessiner panelD;
	private JFrame main;
	/*
	public static void main(String args[]) {
		TailleFiltre tf = new TailleFiltre(null, null, null, null);
		tf.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}*/

	public TailleFiltreMoyenneur(ImageNiveauGris img, IImage imgFiltree, Dessiner panelD, JFrame main) {
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
			textField = new JTextField();
			textField.getDocument().addDocumentListener(this);
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
			int hauteurMasque = Integer.parseInt(textField.getText());
			int largeurMasque = Integer.parseInt(textField.getText());
			double[][] masque = new double[hauteurMasque][largeurMasque];
			double valeurM = 1.0/(hauteurMasque*largeurMasque);
			for(int i = 0; i < hauteurMasque; i++) {
				for (int j = 0; j < largeurMasque; j++) {
					masque[i][j] = valeurM;
				}
			}

			double imgCon[][] = img.convolution(masque);

			int nbLign = imgCon.length;
			int nbCol = imgCon[0].length;
			IPixel[][] imageFP = new IPixel[nbLign][nbCol];
			for (int i = 0; i < nbLign; i++) {
				for (int j = 0; j < nbCol; j++) {
					imageFP[i][j] = new Pixel();
					short val = (short) Math.round(imgCon[i][j]);
					if (val < 0) {
						val = 0;
					} else if (val > 255) {
						val = 255;
					}
					imageFP[i][j].setValue(val);
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
