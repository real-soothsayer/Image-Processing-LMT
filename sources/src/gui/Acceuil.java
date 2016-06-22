package gui;

import image.IImage;
import image.IPixel;
import imageBinaire.ImageBinaire;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JRadioButton;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Dimension;

import javax.swing.border.MatteBorder;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import niveauGris.ImageNiveauGris;
import dataProvider.DataProviders;
import dessin.Dessiner;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class Acceuil extends JFrame implements ActionListener, ChangeListener, MouseListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final ButtonGroup buttonGroupNombre = new ButtonGroup();
	private final ButtonGroup buttonGroupType = new ButtonGroup();
	
	private JRadioButton radioButtonUne;
	private JRadioButton radioButtonDeux;
	private JRadioButton radioButtonGris;
	private JRadioButton radioButtonBin;
	
	private JMenuItem mntmImageA;
	private JMenuItem mntmImageB;
	private JMenuItem mntmEnregistrer;
	private JMenuItem mntmQuitter;
	private JMenuItem mntmZoomP;
	private JMenuItem mntmZoomM;
	private JMenuItem mntmInversion;
	private JMenuItem mntmBinarisation;
	private JMenuItem mntmRotation;
	private JMenuItem mntmConvolution;
	private JMenuItem mntmHorizontal;
	private JMenuItem mntmVertical;
	private JMenuItem mntmParEtalementDhistogramme;
	private JMenuItem mntmParEgalisationDhistrogramme;
	private JMenuItem mntmParAddition;
	private JMenuItem mntmSobel;
	private JMenuItem mntmPrewitt;
	private JMenuItem mntmCanny;
	private JMenuItem mntmMoyenneur;
	private JMenuItem mntmGaussien;
	private JMenuItem mntmMedian;
	private JMenuItem mntmPasseHaut;
	private JMenuItem mntmPasseBas;
	private JMenuItem mntmHough;
	private JMenuItem mntmFourrierfft;
	private JMenuItem mntmFourrierInversefft;
	private JMenuItem mntmErosion;
	private JMenuItem mntmDilatation;
	private JMenuItem mntmOuverture;
	private JMenuItem mntmFermeture;
	private JMenuItem mntmGradientMorphologique;
	private JMenuItem mntmAddition;
	private JMenuItem mntmSoustractiondectectionDe;
	private JMenuItem mntmHistogramme;
	private JMenuItem mntmAPropos;
	private JMenuItem mntmAide;
	
	private JMenu mnFlip;
	private JMenu mnContraste;
	private JMenu mnFiltre;
	private JMenu mnTransformee;
	private JMenu mnMorphologie;
	private JMenu mnOperations;
	
	private Dessiner sousPanelA;
	private Dessiner sousPanelB = new Dessiner();
	private Dessiner sousPanelFin;
	
	private IPixel[][] imageA;
	private IPixel[][] imageB;
	private IImage imageFin;
	
	private JLabel titre;
	private JLabel remarque;
	
	private JPanel panelCentral = new JPanel();
	private JPanel panelA = new JPanel();
	private JPanel panelB = new JPanel();
	private JPanel panelFin;
	
	private GridBagConstraints gbc_panelA = new GridBagConstraints();
	private GridBagConstraints gbc_panelB = new GridBagConstraints();
	private JToolBar toolBar;
	private JButton iconQuitter;
	private JButton iconPlus;
	private JButton iconMoins;
	private JButton iconEnregistrer;
	private JButton iconAide;
	private JSeparator separator_4;
	private JSeparator separator_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Acceuil frame = new Acceuil();
					frame.setVisible(true);

					Hint dialog = null;
					try {
						BufferedReader br = new BufferedReader(new FileReader("./aide/astuces/afficherHint.txt"));
						String decis = br.readLine();
						br.close();
						
						if (decis.equals("oui")) {
							dialog = new Hint();
							dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							dialog.setLocationRelativeTo(null);
							dialog.setVisible(true);
							
						}
					} catch (IOException e) {
						e.printStackTrace();
						dialog.setVisible(false);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Acceuil() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setIconImage(new ImageIcon("./icones/logo2.png").getImage());

		setExtendedState(Frame.MAXIMIZED_BOTH);
		setTitle("Traitement d'images");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 621, 302);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		mnFichier.setMnemonic('f');
		menuBar.add(mnFichier);
		
		JMenu mnCharger = new JMenu("Charger");
		mnCharger.setMnemonic('c');
		mnFichier.add(mnCharger);
		
		mntmImageA = new JMenuItem("Image A");
		mntmImageA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mntmImageA.addActionListener(this);
		mntmImageA.setMnemonic('a');
		mnCharger.add(mntmImageA);
		
		mntmImageB = new JMenuItem("Image B");
		mntmImageB.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		mntmImageB.addActionListener(this);
		mntmImageB.setMnemonic('b');
		mnCharger.add(mntmImageB);
		
		mntmEnregistrer = new JMenuItem("Enregistrer");
		mntmEnregistrer.setIcon(new ImageIcon(".\\icones\\disk.png"));
		mntmEnregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmEnregistrer.addActionListener(this);
		mntmEnregistrer.setMnemonic('e');
		mnFichier.add(mntmEnregistrer);
		
		JSeparator separator = new JSeparator();
		mnFichier.add(separator);
		
		mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.setIcon(new ImageIcon(".\\icones\\cross.png"));
		mntmQuitter.addActionListener(this);
		mntmQuitter.setMnemonic('q');
		mnFichier.add(mntmQuitter);
		
		JMenu mnEdition = new JMenu("Edition");
		mnEdition.setMnemonic('e');
		menuBar.add(mnEdition);
		
		mntmZoomP = new JMenuItem("Zoom +");
		mntmZoomP.setIcon(new ImageIcon(".\\icones\\binocular--plus.png"));
		mntmZoomP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK));
		mntmZoomP.addActionListener(this);
		mntmZoomP.setMnemonic('+');
		mnEdition.add(mntmZoomP);
		
		mntmZoomM = new JMenuItem("Zoom -");
		mntmZoomM.setIcon(new ImageIcon(".\\icones\\binocular--minus.png"));
		mntmZoomM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK));
		mntmZoomM.addActionListener(this);
		mntmZoomM.setMnemonic('-');
		mnEdition.add(mntmZoomM);
		
		JMenu mnTraitements = new JMenu("Traitements");
		mnTraitements.setMnemonic('t');
		menuBar.add(mnTraitements);
		
		mntmInversion = new JMenuItem("Inversion");
		mntmInversion.setIcon(new ImageIcon(".\\icones\\gradient.png"));
		mntmInversion.addActionListener(this);
		mntmInversion.setMnemonic('i');
		mnTraitements.add(mntmInversion);
		
		mntmBinarisation = new JMenuItem("Binarisation");
		mntmBinarisation.setIcon(new ImageIcon(".\\icones\\blue-document-binary.png"));
		mntmBinarisation.addActionListener(this);
		mntmBinarisation.setMnemonic('b');
		mnTraitements.add(mntmBinarisation);
		
		mntmRotation = new JMenuItem("Rotation");
		mntmRotation.setIcon(new ImageIcon(".\\icones\\arrow-circle-double-135.png"));
		mntmRotation.addActionListener(this);
		mntmRotation.setMnemonic('r');
		mnTraitements.add(mntmRotation);
		
		mntmConvolution = new JMenuItem("Convolution");
		mntmConvolution.addActionListener(this);
		mntmConvolution.setMnemonic('c');
		mnTraitements.add(mntmConvolution);
		
		mnFlip = new JMenu("Flip");
		mnFlip.setMnemonic('f');
		mnTraitements.add(mnFlip);
		
		mntmHorizontal = new JMenuItem("Horizontal");
		mntmHorizontal.addActionListener(this);
		mntmHorizontal.setMnemonic('h');
		mnFlip.add(mntmHorizontal);
		
		mntmVertical = new JMenuItem("Vertical");
		mntmVertical.addActionListener(this);
		mntmVertical.setMnemonic('v');
		mnFlip.add(mntmVertical);
		
		mnContraste = new JMenu("Contraste");
		mnContraste.setMnemonic('o');
		mnContraste.setName("");
		mnTraitements.add(mnContraste);
		
		mntmParEtalementDhistogramme = new JMenuItem("Par etalement d'histogramme");
		mntmParEtalementDhistogramme.addActionListener(this);
		mntmParEtalementDhistogramme.setMnemonic('t');
		mnContraste.add(mntmParEtalementDhistogramme);
		
		mntmParEgalisationDhistrogramme = new JMenuItem("Par egalisation d'histrogramme");
		mntmParEgalisationDhistrogramme.addActionListener(this);
		mntmParEgalisationDhistrogramme.setMnemonic('g');
		mnContraste.add(mntmParEgalisationDhistrogramme);
		
		mntmParAddition = new JMenuItem("Par addition");
		mntmParAddition.addActionListener(this);
		mntmParAddition.setMnemonic('d');
		mnContraste.add(mntmParAddition);
		
		mnFiltre = new JMenu("Filtre");
		mnFiltre.setMnemonic('l');
		mnTraitements.add(mnFiltre);
		
		mntmSobel = new JMenuItem("Sobel");
		mntmSobel.addActionListener(this);
		mntmSobel.setMnemonic('s');
		mnFiltre.add(mntmSobel);
		
		mntmPrewitt = new JMenuItem("Prewitt");
		mntmPrewitt.addActionListener(this);
		mntmPrewitt.setMnemonic('p');
		mnFiltre.add(mntmPrewitt);
		
		mntmCanny = new JMenuItem("Canny");
		mntmCanny.addActionListener(this);
		mntmCanny.setMnemonic('c');
		mnFiltre.add(mntmCanny);
		
		JSeparator separator_1 = new JSeparator();
		mnFiltre.add(separator_1);
		
		mntmMoyenneur = new JMenuItem("Moyenneur");
		mntmMoyenneur.addActionListener(this);
		mntmMoyenneur.setMnemonic('m');
		mnFiltre.add(mntmMoyenneur);
		
		mntmGaussien = new JMenuItem("Gaussien");
		mntmGaussien.addActionListener(this);
		mntmGaussien.setMnemonic('g');
		mnFiltre.add(mntmGaussien);
		
		mntmMedian = new JMenuItem("Median");
		mntmMedian.addActionListener(this);
		mntmMedian.setMnemonic('e');
		mnFiltre.add(mntmMedian);
		
		JSeparator separator_2 = new JSeparator();
		mnFiltre.add(separator_2);
		
		mntmPasseHaut = new JMenuItem("Passe haut");
		mntmPasseHaut.addActionListener(this);
		mntmPasseHaut.setMnemonic('h');
		mnFiltre.add(mntmPasseHaut);
		
		mntmPasseBas = new JMenuItem("Passe bas");
		mntmPasseBas.addActionListener(this);
		mntmPasseBas.setMnemonic('b');
		mnFiltre.add(mntmPasseBas);
		
		mnTransformee = new JMenu("Transformee");
		mnTransformee.setMnemonic('t');
		mnTraitements.add(mnTransformee);
		
		mntmHough = new JMenuItem("Hough");
		mntmHough.addActionListener(this);
		mntmHough.setMnemonic('h');
		mnTransformee.add(mntmHough);
		
		mntmFourrierfft = new JMenuItem("Fourrier (FFT)");
		mntmFourrierfft.addActionListener(this);
		mntmFourrierfft.setMnemonic('f');
		mnTransformee.add(mntmFourrierfft);
		
		mntmFourrierInversefft = new JMenuItem("Fourrier inverse (FFT-1)");
		mntmFourrierInversefft.addActionListener(this);
		mntmFourrierInversefft.setMnemonic('i');
		mnTransformee.add(mntmFourrierInversefft);
		
		mnMorphologie = new JMenu("Morphologie");
		mnMorphologie.setMnemonic('m');
		mnTraitements.add(mnMorphologie);
		
		mntmErosion = new JMenuItem("Erosion");
		mntmErosion.addActionListener(this);
		mntmErosion.setMnemonic('e');
		mnMorphologie.add(mntmErosion);
		
		mntmDilatation = new JMenuItem("Dilatation");
		mntmDilatation.addActionListener(this);
		mntmDilatation.setMnemonic('d');
		mnMorphologie.add(mntmDilatation);
		
		mntmOuverture = new JMenuItem("Ouverture");
		mntmOuverture.addActionListener(this);
		mntmOuverture.setMnemonic('o');
		mnMorphologie.add(mntmOuverture);
		
		mntmFermeture = new JMenuItem("Fermeture");
		mntmFermeture.addActionListener(this);
		mntmFermeture.setMnemonic('f');
		mnMorphologie.add(mntmFermeture);
		
		mntmGradientMorphologique = new JMenuItem("Gradient morphologique");
		mntmGradientMorphologique.addActionListener(this);
		mntmGradientMorphologique.setMnemonic('g');
		mnMorphologie.add(mntmGradientMorphologique);
		
		mnOperations = new JMenu("Operations");
		mnOperations.setMnemonic('o');
		mnTraitements.add(mnOperations);
		
		mntmAddition = new JMenuItem("Addition");
		mntmAddition.setIcon(new ImageIcon(".\\icones\\plus-white.png"));
		mntmAddition.addActionListener(this);
		mntmAddition.setMnemonic('a');
		mnOperations.add(mntmAddition);
		
		mntmSoustractiondectectionDe = new JMenuItem("Soustraction (dectection de mouvement)");
		mntmSoustractiondectectionDe.setIcon(new ImageIcon(".\\icones\\minus-white.png"));
		mntmSoustractiondectectionDe.addActionListener(this);
		mntmSoustractiondectectionDe.setMnemonic('s');
		mnOperations.add(mntmSoustractiondectectionDe);
		
		JSeparator separator_3 = new JSeparator();
		mnTraitements.add(separator_3);
		
		mntmHistogramme = new JMenuItem("Histogramme");
		mntmHistogramme.setIcon(new ImageIcon(".\\icones\\application-wave.png"));
		mntmHistogramme.addActionListener(this);
		mntmHistogramme.setMnemonic('h');
		mnTraitements.add(mntmHistogramme);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue);
		
		JMenu mnAide = new JMenu("Aide");
		mnAide.setMnemonic('a');
		menuBar.add(mnAide);
		
		mntmAPropos = new JMenuItem("A propos");
		mntmAPropos.addActionListener(this);
		mntmAPropos.setMnemonic('p');
		mnAide.add(mntmAPropos);
		
		mntmAide = new JMenuItem("Aide");
		mntmAide.setIcon(new ImageIcon(".\\icones\\question.png"));
		mntmAide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmAide.addActionListener(this);
		mntmAide.setMnemonic('a');
		mnAide.add(mntmAide);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setOpaque(false);
		panel.add(panel_3, BorderLayout.EAST);
		
		JPanel panel_5 = new JPanel();
		panel_5.setPreferredSize(new Dimension(150, 70));
		panel_5.setOpaque(false);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Choisir le nombre d'images", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 128)));
		panel_3.add(panel_5);
		panel_5.setLayout(new GridLayout(2, 1, 0, 0));
		
		radioButtonUne = new JRadioButton("Une image");
		radioButtonUne.addChangeListener(this);
		buttonGroupNombre.add(radioButtonUne);
		radioButtonUne.setOpaque(false);
		radioButtonUne.setForeground(new Color(0, 0, 128));
		radioButtonUne.setFont(new Font("Ravie", Font.PLAIN, 11));
		panel_5.add(radioButtonUne);
		
		radioButtonDeux = new JRadioButton("Deux images");
		radioButtonDeux.addChangeListener(this);
		buttonGroupNombre.add(radioButtonDeux);
		radioButtonDeux.setSelected(true);
		radioButtonDeux.setOpaque(false);
		radioButtonDeux.setForeground(new Color(0, 0, 128));
		radioButtonDeux.setFont(new Font("Ravie", Font.PLAIN, 11));
		panel_5.add(radioButtonDeux);
		
		JPanel panel_4 = new JPanel();
		panel_4.setPreferredSize(new Dimension(240, 70));
		panel_4.setOpaque(false);
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Choisir le type d'image", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 128)));
		panel_3.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		radioButtonGris = new JRadioButton("Image en niveaux de gris");
		radioButtonGris.addChangeListener(this);
		buttonGroupType.add(radioButtonGris);
		radioButtonGris.setSelected(true);
		radioButtonGris.setOpaque(false);
		radioButtonGris.setForeground(new Color(0, 0, 128));
		radioButtonGris.setFont(new Font("Ravie", Font.PLAIN, 11));
		panel_4.add(radioButtonGris);
		
		radioButtonBin = new JRadioButton("Image binaire");
		radioButtonBin.addChangeListener(this);
		buttonGroupType.add(radioButtonBin);
		radioButtonBin.setOpaque(false);
		radioButtonBin.setForeground(new Color(0, 0, 128));
		radioButtonBin.setFont(new Font("Ravie", Font.PLAIN, 11));
		panel_4.add(radioButtonBin);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new MatteBorder(0, 0, 0, 2, (Color) new Color(0, 0, 0)));
		panel_6.setOpaque(false);
		panel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		titre = new JLabel("");
		titre.setFont(new Font("Algerian", Font.BOLD, 14));
		titre.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(titre);
		
		toolBar = new JToolBar();
		toolBar.setBackground(new Color(147, 112, 219));
		panel_6.add(toolBar, BorderLayout.NORTH);
		
		iconEnregistrer = new JButton("");
		iconEnregistrer.addActionListener(this);
		iconEnregistrer.setToolTipText("Enregistrer l'image");
		iconEnregistrer.setOpaque(false);
		iconEnregistrer.setIcon(new ImageIcon(".\\icones\\disk.png"));
		toolBar.add(iconEnregistrer);
		
		iconQuitter = new JButton(new ImageIcon(".\\icones\\cross.png"));
		iconQuitter.addActionListener(this);
		iconQuitter.setToolTipText("quitter");
		iconQuitter.setOpaque(false);
		toolBar.add(iconQuitter);
		
		separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setPreferredSize(new Dimension(0, 0));
		toolBar.add(separator_4);
		
		iconPlus = new JButton("");
		iconPlus.addActionListener(this);
		iconPlus.setToolTipText("Zoom +");
		iconPlus.setOpaque(false);
		iconPlus.setIcon(new ImageIcon(".\\icones\\binocular--plus.png"));
		toolBar.add(iconPlus);
		
		iconMoins = new JButton("");
		iconMoins.addActionListener(this);
		iconMoins.setToolTipText("Zoom -");
		iconMoins.setOpaque(false);
		iconMoins.setIcon(new ImageIcon(".\\icones\\binocular--minus.png"));
		toolBar.add(iconMoins);
		
		separator_5 = new JSeparator();
		separator_5.setPreferredSize(new Dimension(0, 0));
		separator_5.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator_5);
		
		iconAide = new JButton("");
		iconAide.addActionListener(this);
		iconAide.setToolTipText("Aide");
		iconAide.setOpaque(false);
		iconAide.setIcon(new ImageIcon(".\\icones\\question.png"));
		toolBar.add(iconAide);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(248, 248, 255));
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		remarque = new JLabel("Remarque: ");
		remarque.setFont(new Font("Tahoma", Font.ITALIC, 11));
		panel_1.add(remarque);
		
		panelCentral.setBackground(Color.WHITE);
		contentPane.add(panelCentral, BorderLayout.CENTER);
		GridBagLayout gbl_panelCentral = new GridBagLayout();
		gbl_panelCentral.columnWidths = new int[]{0, 0, 0};
		gbl_panelCentral.rowHeights = new int[]{0, 0, 0};
		gbl_panelCentral.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelCentral.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panelCentral.setLayout(gbl_panelCentral);
		
		gbc_panelA.insets = new Insets(0, 0, 5, 5);
		gbc_panelA.gridheight = 1;
		gbc_panelA.fill = GridBagConstraints.BOTH;
		gbc_panelA.gridx = 0;
		gbc_panelA.gridy = 0;
		panelA.setOpaque(false);
		panelCentral.add(panelA, gbc_panelA);
		panelA.setLayout(new BorderLayout(0, 0));
		
		JLabel lblImageA = new JLabel("Image A");
		lblImageA.setOpaque(true);
		lblImageA.setForeground(new Color(0, 0, 128));
		lblImageA.setFont(new Font("Andalus", Font.BOLD, 14));
		lblImageA.setHorizontalAlignment(SwingConstants.CENTER);
		panelA.add(lblImageA, BorderLayout.NORTH);

		sousPanelA = new Dessiner();
		sousPanelA.addMouseListener(this);
		sousPanelA.setToolTipText("Cliquer pour charger l'image A");
		sousPanelA.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sousPanelA.setBorder(new LineBorder(new Color(148, 0, 211)));
		panelA.add(sousPanelA, BorderLayout.CENTER);
		
		gbc_panelB.insets = new Insets(0, 0, 0, 5);
		gbc_panelB.fill = GridBagConstraints.BOTH;
		gbc_panelB.gridx = 0;
		gbc_panelB.gridy = 1;
		panelB.setOpaque(false);
		panelCentral.add(panelB, gbc_panelB);
		panelB.setLayout(new BorderLayout(0, 0));
		
		JLabel lblImageB = new JLabel("Image B");
		lblImageB.setOpaque(true);
		lblImageB.setForeground(new Color(0, 0, 128));
		lblImageB.setFont(new Font("Andalus", Font.BOLD, 14));
		lblImageB.setHorizontalAlignment(SwingConstants.CENTER);
		panelB.add(lblImageB, BorderLayout.NORTH);
		
		sousPanelB.addMouseListener(this);
		sousPanelB.setToolTipText("cliquer pour charger l'image B");
		sousPanelB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sousPanelB.setBorder(new LineBorder(new Color(148, 0, 211)));
		panelB.add(sousPanelB, BorderLayout.CENTER);
		
		panelFin = new JPanel();
		panelFin.setOpaque(false);
		GridBagConstraints gbc_panelFin = new GridBagConstraints();
		gbc_panelFin.gridheight = 2;
		gbc_panelFin.fill = GridBagConstraints.BOTH;
		gbc_panelFin.gridx = 1;
		gbc_panelFin.gridy = 0;
		panelCentral.add(panelFin, gbc_panelFin);
		panelFin.setLayout(new BorderLayout(0, 0));
		
		JLabel lblImageFinale = new JLabel("Image finale");
		lblImageFinale.setOpaque(true);
		lblImageFinale.setForeground(new Color(0, 0, 128));
		lblImageFinale.setFont(new Font("Andalus", Font.BOLD, 14));
		lblImageFinale.setHorizontalAlignment(SwingConstants.CENTER);
		panelFin.add(lblImageFinale, BorderLayout.NORTH);
		
		sousPanelFin = new Dessiner();
		sousPanelFin.addMouseListener(this);
		sousPanelFin.setToolTipText("Cliquer pour enregistrer");
		sousPanelFin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sousPanelFin.setBorder(new LineBorder(new Color(148, 0, 211)));
		panelFin.add(sousPanelFin, BorderLayout.CENTER);
		
		mntmFourrierInversefft.setVisible(false);
		radioButtonUne.setSelected(true);
		radioButtonGris.setSelected(true);
		uneImageNiveauGris();
	}
	
	private short[][] convertionIPixelShort(IPixel[][] img) {
		short[][] image = new short[img.length][img[0].length];
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				image[i][j] = img[i][j].getValue();
			}
		}

		return image;
	}
	/*
	private ImageNiveauGris convertionIPixelImageAdapter(IPixel[][] img) {
		ImageNiveauGris image = new ImageNiveauGris(img);
		return image;
	}*/
	
	private short[][] convertionIImageShort(IImage img) {
		return this.convertionIPixelShort(img.getImage());
	}
	
	private ImageNiveauGris convertionIPixelImageNiveauGris(IPixel[][] img) {
		return (new ImageNiveauGris(img));
	}
	
	private ImageBinaire convertionIPixelImageBinaire(IPixel[][] img) {
		return (new ImageBinaire(img));
	}
	
	private void charger(String image) throws Exception {
		UIManager.put("FileChooser.openButtonText","Choisir");
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"images non compressés (.pgm)", "pgm");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("charger l'image " + image);
		chooser.setCurrentDirectory(new File(".\\data"));
		
		int returnVal = chooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String chemin = chooser.getSelectedFile().getPath();
			
			if(DataProviders.getType(chemin).equals("pgm")) {
				if (image.equals("A")) {
					this.imageA = DataProviders.getImage(chemin);
					
					sousPanelA.setImage(this.convertionIPixelShort(imageA));
					sousPanelA.repaint();
				} else if (image.equals("B")) {
					this.imageB = DataProviders.getImage(chemin);	
					
					sousPanelB.setImage(this.convertionIPixelShort(imageB));
					sousPanelB.repaint();
				}
				this.repaint();
			} else {
				JOptionPane.showMessageDialog(this, "veuillez choisir un fichier pgm", "erreur de fichier", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void enregistrer() throws Exception {
		UIManager.put("FileChooser.saveButtonText","enregistrer");
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"images non compressés (.pgm)", "pgm");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("enregistrer l'image");
		chooser.setCurrentDirectory(new File(".\\data"));
		
		int returnVal = chooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String chemin = chooser.getSelectedFile().getPath();
			DataProviders.saveImage(imageFin, chemin);
		}
	}
	
	private void deuxImages() {
		mntmImageB.setVisible(true);
		mntmInversion.setVisible(false);
		mntmBinarisation.setVisible(false);
		mntmConvolution.setVisible(false);
		mntmRotation.setVisible(false);
		mnFiltre.setVisible(false);
		mnFlip.setVisible(false);
		mnContraste.setVisible(false);
		mnMorphologie.setVisible(false);
		mnTransformee.setVisible(false);
		mnOperations.setVisible(true);
		mntmHistogramme.setVisible(false);
		
		panelCentral.remove(panelB);
		panelCentral.remove(panelA);
		gbc_panelA.insets = new Insets(0, 0, 5, 5);
		gbc_panelA.gridheight = 1;
		panelCentral.add(panelA, gbc_panelA);
		panelCentral.add(panelB, gbc_panelB);
		panelCentral.revalidate();
	}
	
	private void uneImage() {
		mntmImageB.setVisible(false);
		mntmInversion.setVisible(true);
		mntmRotation.setVisible(true);
		mnFlip.setVisible(true);
		mnOperations.setVisible(false);
		mntmHistogramme.setVisible(true);

		panelCentral.remove(panelB);
		panelCentral.remove(panelA);
		gbc_panelA.insets = new Insets(0, 0, 0, 5);
		gbc_panelA.gridheight = 2;
		panelCentral.add(panelA, gbc_panelA);
		panelCentral.revalidate();
	}
	
	private void uneImageNiveauGris() {
		this.uneImage();
		mntmBinarisation.setVisible(true);
		mntmConvolution.setVisible(true);
		mnFiltre.setVisible(true);
		mnContraste.setVisible(true);
		mnMorphologie.setVisible(false);
		mnTransformee.setVisible(true);
	}
	
	private void uneImageBinaire() {
		this.uneImage();
		mntmBinarisation.setVisible(false);
		mntmConvolution.setVisible(false);
		mnFiltre.setVisible(false);
		mnContraste.setVisible(false);
		mnMorphologie.setVisible(true);
		mnTransformee.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(mntmImageA)) {
			try {
				this.charger("A");
				remarque.setText("Remarque : image A chargee");
				remarque.setForeground(Color.GREEN);
				
				mntmFourrierInversefft.setVisible(false);
			} catch (FileNotFoundException e1) {
				remarque.setText("Remarque : erreur de chargement de A. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de chargement de A. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmImageB)) {
			try {
				this.charger("B");
				remarque.setText("Remarque : image B chargee");
				remarque.setForeground(Color.GREEN);

				mntmFourrierInversefft.setVisible(false);
			} catch (FileNotFoundException e1) {
				remarque.setText("Remarque : erreur de chargement de B. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de chargement de B. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmEnregistrer) || e.getSource().equals(iconEnregistrer)) {
			try {
				this.enregistrer();
				remarque.setText("Remarque : enregistrement reussi");
				remarque.setForeground(Color.GREEN);
			} catch (IOException e1) {
				remarque.setText("Remarque : erreur d'enregistrement. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur d'enregistrement. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} 
		} else if (e.getSource().equals(mntmQuitter) || e.getSource().equals(iconQuitter)) {
			Object[] options = { "OUI", "NON" };
			int choix = JOptionPane.showOptionDialog(null,
					"Voulez-vous vraiment quitter?", "attention",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			if (choix == 0) {
				System.exit(0);
			}
		} else if (e.getSource().equals(mntmZoomP) || e.getSource().equals(iconPlus)) {
			sousPanelA.zoom();
			sousPanelB.zoom();
			sousPanelFin.zoom();
			this.repaint();
		} else if (e.getSource().equals(mntmZoomM) || e.getSource().equals(iconMoins)) {
			sousPanelA.dezoom();
			sousPanelB.dezoom();
			sousPanelFin.dezoom();
			this.repaint();
		} else if (e.getSource().equals(mntmInversion)) {
			try {
				if (radioButtonBin.isSelected()) {
					ImageNiveauGris img = new ImageNiveauGris(imageA);
					imageFin = img.inverser();
				} else if (radioButtonGris.isSelected()) {
					ImageBinaire img = new ImageBinaire(imageA);
					imageFin = img.inverser();
				}
				titre.setText("Inversion de l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur d'inversion. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmBinarisation)) {
			try {
				imageFin = ImageNiveauGris.seuillage(this.convertionIPixelImageNiveauGris(imageA), (short) 128);
				titre.setText("Binarisation de l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de binarisation de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmRotation)) {
			try {
				if (radioButtonBin.isSelected()) {
					imageFin = new ImageNiveauGris();
				} else if (radioButtonGris.isSelected()) {
					imageFin = new ImageBinaire();
				}
				Angle rot = new Angle(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this);
				rot.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				rot.setVisible(true);
				titre.setText("Rotation de l'image A");
				
				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de rotation de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmConvolution)) {
			try {
				imageFin = new ImageNiveauGris();
				Convolution conv = new Convolution(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this);
				conv.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				conv.setVisible(true);
				titre.setText("Convolution de l'image A");
				
				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de convolution de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmVertical)) {
			try {
				if (radioButtonBin.isSelected()) {
					ImageNiveauGris img = new ImageNiveauGris(imageA);
					imageFin = img.flip(1);
				} else if (radioButtonGris.isSelected()) {
					ImageBinaire img = new ImageBinaire(imageA);
					imageFin = img.flip(1);
				}
				titre.setText("Inversion de l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur d'inversion. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmHorizontal)) {
			try {
				if (radioButtonBin.isSelected()) {
					ImageNiveauGris img = new ImageNiveauGris(imageA);
					imageFin = img.flip(0);
				} else if (radioButtonGris.isSelected()) {
					ImageBinaire img = new ImageBinaire(imageA);
					imageFin = img.flip(0);
				}
				titre.setText("Inversion de l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur d'inversion. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmParEtalementDhistogramme)) {
			try {
				imageFin = (this.convertionIPixelImageNiveauGris(imageA)).etalementHisto();
				titre.setText("Amélioration du contraste de l'image A par étalement de son histogramme");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de realisation du contraste de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmParEgalisationDhistrogramme)) {
			try {
				imageFin = (this.convertionIPixelImageNiveauGris(imageA)).egalisationHisto();
				titre.setText("Amélioration du contraste de l'image A par égalisation de son histogramme");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de realisation du contraste de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmParAddition)) {
			try {
				imageFin = (this.convertionIPixelImageNiveauGris(imageA)).addition(this.convertionIPixelImageNiveauGris(imageA));
				titre.setText("Amélioration du contraste par addition de l'image A avec elle même");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de realisation du contraste de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmAddition)) {
			try {
				imageFin = (this.convertionIPixelImageNiveauGris(imageA)).addition(this.convertionIPixelImageNiveauGris(imageB));
				titre.setText("Addition de l'image A et de l'image B");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur d'addition des images. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmSoustractiondectectionDe)) {
			try {
				imageFin = (this.convertionIPixelImageNiveauGris(imageA)).soustraction(this.convertionIPixelImageNiveauGris(imageB));
				titre.setText("Soustraction de l'image A par l'image B");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de soustraction des images. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmSobel)) {
			try {
				imageFin = new ImageNiveauGris();
				Seuillage sob = new Seuillage(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this, "sobel");
				sob.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				sob.setVisible(true);
				titre.setText("Filtre de sobel de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre de Sobel. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmPrewitt)) {
			try {
				imageFin = new ImageNiveauGris();
				Seuillage pre = new Seuillage(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this, "prewitt");
				pre.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				pre.setVisible(true);
				titre.setText("Filtre de prewitt de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre de Prewitt. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmCanny)) {
			try {
				imageFin = new ImageNiveauGris();
				TailleRayonSeuilFiltreCanny can = new TailleRayonSeuilFiltreCanny(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this);
				can.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				can.setVisible(true);
				titre.setText("Filtre de canny de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre de canny. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmMoyenneur)) {
			try {
				imageFin = new ImageNiveauGris();
				TailleFiltreMoyenneur moy = new TailleFiltreMoyenneur(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this);
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Filtre moyenneur de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre moyenneur. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmGaussien)) {
			try {
				imageFin = new ImageNiveauGris();
				TailleRayonFiltre gau = new TailleRayonFiltre(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this);
				gau.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				gau.setVisible(true);
				titre.setText("Application du filtre gaussien à l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre gaussien. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmMedian)) {
			try {
				imageFin = new ImageNiveauGris();
				TailleFiltreMedian moy = new TailleFiltreMedian(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this);
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Filtre median l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre median. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmPasseBas)) {
			try {
				imageFin = new ImageNiveauGris();
				Rayon moy = new Rayon(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this, "bas");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Filtre passe bas sur l'image A");

				mntmFourrierInversefft.setVisible(true);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation du filtre passe bas. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmPasseHaut)) {
			try {
				imageFin = new ImageNiveauGris();
				Rayon moy = new Rayon(this.convertionIPixelImageNiveauGris(imageA), imageFin, sousPanelFin, this, "haut");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Filtre passe haut sur l'image A");

				mntmFourrierInversefft.setVisible(true);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation du filtre passe haut. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmHough)) {
			try {
				imageFin = (this.convertionIPixelImageNiveauGris(imageA)).transformeeHough();
				titre.setText("Transformee de hough appliquee a l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de realisation de la transformee de hough. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmFourrierfft)) {
			try {
				ImageNiveauGris img = new ImageNiveauGris(imageA);
				imageFin = this.convertionIPixelImageNiveauGris(img.FFT());

				titre.setText("Transformee de fourrier de l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(true);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de transformee de fourrier. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmFourrierInversefft)) {
			try {
				ImageNiveauGris img = new ImageNiveauGris(imageA);
				imageFin = this.convertionIPixelImageNiveauGris(img.inverseFFT());

				titre.setText("Transformee de fourrier inverse sur l'image A");
				sousPanelFin.setImage(this.convertionIImageShort(imageFin));
				sousPanelFin.repaint();
				this.repaint();

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de transformee de fourrier inverse. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmDilatation)) {
			try {
				imageFin = new ImageBinaire();
				TailleTypeMorphologie moy = new TailleTypeMorphologie(this.convertionIPixelImageBinaire(imageA), imageFin, sousPanelFin, this, "dil");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Dilatation de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de la dilatation de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmErosion)) {
			try {
				imageFin = new ImageBinaire();
				TailleTypeMorphologie moy = new TailleTypeMorphologie(this.convertionIPixelImageBinaire(imageA), imageFin, sousPanelFin, this, "ero");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Erosion de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de l'erosion de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmOuverture)) {
			try {
				imageFin = new ImageBinaire();
				TailleTypeMorphologie moy = new TailleTypeMorphologie(this.convertionIPixelImageBinaire(imageA), imageFin, sousPanelFin, this, "ouv");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Ouverture de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de l'ouverture de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmFermeture)) {
			try {
				imageFin = new ImageBinaire();
				TailleTypeMorphologie moy = new TailleTypeMorphologie(this.convertionIPixelImageBinaire(imageA), imageFin, sousPanelFin, this, "fer");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Fermeture de l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de la fermeture de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmGradientMorphologique)) {
			try {
				imageFin = new ImageBinaire();
				TailleTypeMorphologie moy = new TailleTypeMorphologie(this.convertionIPixelImageBinaire(imageA), imageFin, sousPanelFin, this, "gra");
				moy.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				moy.setVisible(true);
				titre.setText("Gradient morphologique sur l'image A");

				mntmFourrierInversefft.setVisible(false);
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation du gradient morphologique de l'image. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmHistogramme)) {
			try {
				Histogramme his = new Histogramme(this.convertionIPixelShort(imageA));
				his.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				his.setVisible(true);
				titre.setText("Histogramme de l'image A");
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de réalisation de filtre gaussien. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(mntmAPropos)) {
			ImageIcon image = new ImageIcon(new ImageIcon(
					"./icones/logo2.png").getImage().getScaledInstance(
					120, 100, Image.SCALE_DEFAULT));
			JOptionPane.showMessageDialog(
					null,
					String.format("Traitements d'images\nVersion: 1.0\nCopyright GI2015 ENSP. Tous droits réservés.\n\nRéalisateurs:\n- LEKANE TATSAGOUM ARTHUR-CARNIS\n- MAFO DE HEDZO LEONIE ARIANE\n- TATSAWOUM FOMETIO SIDOIN"),
					"A propos du programme",
					JOptionPane.INFORMATION_MESSAGE, image);
		} else if (e.getSource().equals(mntmAide) || e.getSource().equals(iconAide)) {
			try {
				Desktop.getDesktop().open(new File("./aide/index.html"));
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						"erreur d'ouverture du fichier d'aide . " + ex.getMessage() + "\nEssayer de l'ouvrir manuellement",
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(radioButtonUne) || e.getSource().equals(radioButtonDeux) || e.getSource().equals(radioButtonBin) || e.getSource().equals(radioButtonGris)) {
			if (radioButtonUne.isSelected()) {
				if (radioButtonBin.isSelected()) {
					uneImageBinaire();
				} else if (radioButtonGris.isSelected()) {
					uneImageNiveauGris();
				}
			} else if (radioButtonDeux.isSelected()) {
				deuxImages();
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(sousPanelA)) {
			try {
				this.charger("A");
				remarque.setText("Remarque : image A chargee");
				remarque.setForeground(Color.GREEN);
			} catch (FileNotFoundException e1) {
				remarque.setText("Remarque : erreur de chargement de A. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de chargement de A. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(sousPanelB)) {
			try {
				this.charger("B");
				remarque.setText("Remarque : image B chargee");
				remarque.setForeground(Color.GREEN);
			} catch (FileNotFoundException e1) {
				remarque.setText("Remarque : erreur de chargement de B. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur de chargement de B. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(sousPanelFin)) {
			try {
				this.enregistrer();
				remarque.setText("Remarque : enregistrement reussi");
				remarque.setForeground(Color.GREEN);
			} catch (IOException e1) {
				remarque.setText("Remarque : erreur d'enregistrement. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			} catch (Exception e1) {
				remarque.setText("Remarque : erreur d'enregistrement. " + e1.getMessage());
				remarque.setForeground(Color.RED);
				e1.printStackTrace();
			}
		}
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
