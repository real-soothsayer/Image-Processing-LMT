package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hint extends JDialog implements ActionListener{

	private List<String> messages;
	private List<String> images;
	
	private int indActuel = 0;
	
	private final JPanel contentPanel = new JPanel();
	private BufferedReader fluxInterne;
	private BufferedWriter fluxExterne;
	
	private JButton btnAnnuler;
	private JButton precedent;
	private JButton suivant;
	
	private JTextPane dtrpnTest;
	private JLabel lblNewLabel;
	
	private JCheckBox chckbxNePlusAfficher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Hint dialog = new Hint();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public Hint() throws IOException {
		messages = new ArrayList<String>();
		images = new ArrayList<String>();
		fluxInterne = new BufferedReader(new FileReader("./aide/astuces/astuce.hint"));
	
		String message = "";
		String image = "";
		String nextLine = fluxInterne.readLine();
		while (nextLine != null) {
			image = "notImage";
			if (nextLine.equals("image")) {
				nextLine = fluxInterne.readLine();
				image = nextLine;
				nextLine = fluxInterne.readLine();
			}
			while (nextLine != null && !nextLine.equals("---")) {
				message += nextLine + "\r\n";
				nextLine = fluxInterne.readLine();
			}
			
			messages.add(message);
			images.add(image);
			nextLine = fluxInterne.readLine();
			message = "";
		}
		
		fluxInterne.close();
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Astuces");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			if (!images.get(0).equals("notImage")) {
				lblNewLabel = new JLabel("");
				lblNewLabel.setBackground(Color.WHITE);
				lblNewLabel.setIcon(new ImageIcon("./aide/astuces/" + images.get(0)));
				contentPanel.add(lblNewLabel, BorderLayout.CENTER);
			}
		}
		{
			chckbxNePlusAfficher = new JCheckBox("Ne plus afficher au demarrage");
			chckbxNePlusAfficher.setBackground(Color.WHITE);
			chckbxNePlusAfficher.addActionListener(this);
			contentPanel.add(chckbxNePlusAfficher, BorderLayout.SOUTH);
			
		}
		{
			dtrpnTest = new JTextPane();
			dtrpnTest.setFont(new Font("Sakkal Majalla", Font.BOLD, 18));
			dtrpnTest.setText(messages.get(0));
			dtrpnTest.setEditable(false);
			contentPanel.add(dtrpnTest, BorderLayout.NORTH);
			
			setBounds(100, 100, 580, 400);
		}
		{
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnAnnuler = new JButton("Annuler");
				btnAnnuler.setActionCommand("Cancel");
				btnAnnuler.addActionListener(this);
				buttonPane.add(btnAnnuler);
				getRootPane().setDefaultButton(btnAnnuler);
			}
			{
				precedent = new JButton("<< Pr\u00E9cedent");
				precedent.addActionListener(this);
				buttonPane.add(precedent);
			}
			{
				suivant = new JButton("Suivant >>");
				suivant.addActionListener(this);
				buttonPane.add(suivant);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(precedent)) {
			indActuel--;
			if (indActuel < 0) {
				indActuel += messages.size();
			}
			dtrpnTest.setText(messages.get(indActuel));
			
			contentPanel.remove(lblNewLabel);
			lblNewLabel = new JLabel("");
			lblNewLabel.setBackground(Color.WHITE);
			if (!images.get(indActuel).equals("notImage")) {
				lblNewLabel.setIcon(new ImageIcon("./aide/astuces/" + images.get(indActuel)));
			}
			contentPanel.add(lblNewLabel, BorderLayout.CENTER);
			contentPanel.revalidate();
		} else if (e.getSource().equals(btnAnnuler)) {
			this.dispose();
		} else if (e.getSource().equals(suivant)) {
			indActuel = (++indActuel) % messages.size();
			dtrpnTest.setText(messages.get(indActuel));
			
			contentPanel.remove(lblNewLabel);
			lblNewLabel = new JLabel("");
			lblNewLabel.setBackground(Color.WHITE);
			if (!images.get(indActuel).equals("notImage")) {
				lblNewLabel.setIcon(new ImageIcon("./aide/astuces/" + images.get(indActuel)));
			}
			contentPanel.add(lblNewLabel, BorderLayout.CENTER);
			contentPanel.revalidate();
		} else if (e.getSource().equals(chckbxNePlusAfficher)) {
			try {
				fluxExterne = new BufferedWriter(new FileWriter("./aide/astuces/afficherHint.txt"));
				if (chckbxNePlusAfficher.isSelected()) {
					fluxExterne.write("non");
				} else {
					fluxExterne.write("oui");
				}
				fluxExterne.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("test");
		}
	}
}
