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

import java.awt.GridLayout;

import javax.swing.JTextField;

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Seuillage extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	private JButton cancelButton;
	private JButton okButton;
	
	private ImageNiveauGris image;
	private IImage imageFinale;
	private Dessiner panel;
	private JFrame main;
	private String type;
	/**
	 * Create the dialog.
	 */
	public Seuillage(ImageNiveauGris image, IImage imageFinale, Dessiner panel, JFrame main, String type) {
		setModal(true);
		
		this.image = image;
		this.imageFinale = imageFinale;
		this.panel = panel;
		this.main = main;
		this.type = type;
		
		setTitle("Seuil (0 si pas de seuillage)");
		setBounds(100, 100, 263, 120);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		{
			textField = new JTextField();
			textField.setText("0");
			contentPanel.add(textField);
			textField.setColumns(10);
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
			short seuil = Short.parseShort(textField.getText());
			
			IImage imageFin = null;
			if (this.type.equals("sobel")) {
				imageFin = image.filtreSobel(seuil);
			} else if (this.type.equals("prewitt")) {
				imageFin = image.filtrePrewitt(seuil);
			}
			short[][] imageFS = new short[imageFin.getNombreLignes()][imageFin.getNombreColonnes()];
			for (int i = 0; i < imageFin.getNombreLignes(); i++) {
				for (int j = 0; j < imageFin.getNombreColonnes(); j++) {
					imageFS[i][j] = imageFin.getImage()[i][j].getValue();
				}
			}
			
			IPixel[][] imageFP = new IPixel[imageFin.getNombreLignes()][imageFin.getNombreColonnes()];
			for (int i = 0; i < imageFin.getNombreLignes(); i++) {
				for (int j = 0; j < imageFin.getNombreColonnes(); j++) {
					imageFP[i][j] = new Pixel();
					imageFP[i][j].setValue(imageFS[i][j]);
				}
			}
			this.imageFinale.setImage(imageFP);
			this.imageFinale.setNombreColonnes(imageFin.getNombreColonnes());
			this.imageFinale.setNombreLignes(imageFin.getNombreLignes());
			
			panel.setImage(imageFS);
			panel.repaint();
			main.repaint();
			
			this.dispose();
		}
	}

}
