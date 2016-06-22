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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.SwingConstants;
import javax.swing.Box;

import dessin.Dessiner;
import niveauGris.ImageNiveauGris;

import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Convolution extends JDialog implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField caseHauteur;
	private JPanel panel_1;
	private JButton okButton;
	private JButton cancelButton;
	private JButton btnValider;
	
	private JLabel taille;
	private JPanel panel;
	
	private JTextField[][] text;

	private ImageNiveauGris image;
	private IImage imageConvoluee;
	private JLabel lblLargeurDuMasque;
	private JTextField caseLargeur;
	private Dessiner panelD;
	private JFrame main;
	/*
	public static void main(String[] args) {
		try {
			Convolution dialog = new Convolution(null, null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	/**
	 * Create the dialog.
	 */
	public Convolution(ImageNiveauGris img, IImage imgConvoluee, Dessiner panelD, JFrame main) {
		setModal(true);
		setTitle("Masque de convolution");
		this.image = img;
		this.imageConvoluee = imgConvoluee;
		this.panelD = panelD;
		this.main = main;
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(this);
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
			final JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				JLabel lblTailleDuMasque = new JLabel("hauteur");
				panel.add(lblTailleDuMasque);
			}
			{
				caseHauteur = new JTextField();
				caseHauteur.setText("");
				caseHauteur.addKeyListener(this);
				panel.add(caseHauteur);
				caseHauteur.setColumns(10);
			}
			{
				lblLargeurDuMasque = new JLabel("largeur");
				panel.add(lblLargeurDuMasque);
			}
			{
				caseLargeur = new JTextField();
				caseLargeur.setText("");
				caseLargeur.addKeyListener(this);
				panel.add(caseLargeur);
				caseLargeur.setColumns(10);
			}
			{
				taille = new JLabel("0x0");
				panel.add(taille);
			}
			{
				btnValider = new JButton("valider");
				btnValider.addActionListener(this);
				panel.add(btnValider);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				panel_1 = new JPanel();
				scrollPane.setViewportView(panel_1);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					Component horizontalGlue = Box.createHorizontalGlue();
					panel_1.add(horizontalGlue, BorderLayout.EAST);
				}
				{
					Component verticalGlue = Box.createVerticalGlue();
					panel_1.add(verticalGlue, BorderLayout.SOUTH);
				}
				{
					panel = new JPanel();
					panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					panel_1.add(panel, BorderLayout.CENTER);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)) {
			int hauteurMasque = Integer.parseInt(caseHauteur.getText());
			int largeurMasque = Integer.parseInt(caseLargeur.getText());
			double[][] masque = new double[hauteurMasque][largeurMasque];
			for(int i = 0; i < hauteurMasque; i++) {
				for (int j = 0; j < largeurMasque; j++) {
					masque[i][j] = Float.parseFloat(text[i][j].getText());
				}
			}

			double imgCon[][] = image.convolution(masque);

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

			imageConvoluee.setImage(imageFP);
			imageConvoluee.setNombreColonnes(nbCol);
			imageConvoluee.setNombreLignes(nbLign);
			
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
		} else if (e.getSource().equals(cancelButton)) {
			this.dispose();
		} else if (e.getSource().equals(btnValider)) {
			panel_1.remove(panel);
			panel = new JPanel();
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			panel_1.add(panel, BorderLayout.CENTER);
			int h = Integer.parseInt(caseHauteur.getText());
			int l = Integer.parseInt(caseLargeur.getText());
			
			if (h < 1 || l < 1) {
				JOptionPane.showMessageDialog(null,
						"le masque doit avoir au moins une taille de 1x1",
						"attention", JOptionPane.WARNING_MESSAGE);
			} else if ((h % 2 != 1) || (l % 2 != 1)) {
				JOptionPane.showMessageDialog(null,
						"le masque doit avoir de taille impaire 1x3, 5x3, 5x5...",
						"attention", JOptionPane.WARNING_MESSAGE);
			} else {
				panel.setLayout(new GridLayout(h, l, 0, 0));
				text = new JTextField[h][l];
				for(int i = 0; i < h; i++) {
					for (int j = 0; j < l; j++) {
						text[i][j] = new JTextField("0");
						text[i][j].setHorizontalAlignment(SwingConstants.CENTER);
						text[i][j].setColumns(4);
						
						final int iF = i;
						final int jF = j;
						text[i][j].addFocusListener(new FocusListener() {
			
							private String valeur;
			
							@Override
							public void focusGained(FocusEvent arg0) {
								// TODO Auto-generated method stub
								valeur = text[iF][jF].getText();
								text[iF][jF].setText("");
							}
			
							@Override
							public void focusLost(FocusEvent arg0) {
								// TODO Auto-generated method stub
								if (text[iF][jF].getText().isEmpty())
									text[iF][jF].setText(valeur);
							}
			
						});
						
						panel.add(text[i][j]);
					}
				}
			}
			
			this.revalidate();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			taille.setText(caseHauteur.getText() + "x" + caseLargeur.getText());	
		} catch (Exception e1) {
			
		}
	}

}
