package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MasqueGaussien extends JDialog implements ActionListener{

	/**
	 * Create the dialog.
	 */
	public MasqueGaussien(int[][] masque) {
		this.setModal(true);
		this.setTitle("Masque gaussien");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			int h = masque.length;
			int l = masque[0].length;
			JPanel panel = new JPanel(new GridLayout(h, l, 0, 0));
			JTextField[][] text = new JTextField[h][l];
			for(int i = 0; i < h; i++) {
				for (int j = 0; j < l; j++) {
					text[i][j] = new JTextField(masque[i][j]+"");
					text[i][j].setHorizontalAlignment(SwingConstants.CENTER);
					text[i][j].setColumns(4);
					text[i][j].setEditable(false);
					
					panel.add(text[i][j]);
				}
			}

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(panel);
			
			getContentPane().add(scrollPane, BorderLayout.CENTER);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}

}
