package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import dataProvider.DataProviders;
import niveauGris.ImageNiveauGris;

public class MainClass {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) {
		
				//test du filtre passe haut
				String file2 = "data\\force.pgm";
				try {
					ImageNiveauGris ng =  new ImageNiveauGris(DataProviders.getImage(file2));
					ng=(ImageNiveauGris) ng.filtrePasseHaut(10);

					try {

						DataProviders.saveImage(ng, "data\\filtrePasseHaut.pgm");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(ng.getNombreLignes()+"--------"+ng.getNombreColonnes());
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
		
		// fft inverse
		try {
				ImageNiveauGris ng =  new ImageNiveauGris(DataProviders.getImage("data\\test.pgm"));
				ng.setImage(ng.inverseFFT());
				ng.setNombreColonnes(ng.getImage()[0].length);
				ng.setNombreLignes(ng.getImage().length);
								
				DataProviders.saveImage(ng, "data\\0ft.pgm");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		System.out.println("FIN");
	}

}
