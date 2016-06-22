package dataProvider;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import image.ElementStructurant;
import image.IImage;
import image.IPixel;
import image.Pixel;

public class DataProviders {
	
	public static String getType(String file) throws FileNotFoundException {

		Scanner sc =  new Scanner(new FileInputStream(file));
		String ret = sc.nextLine();
		sc.close();
		
		if (ret.equals("P1")) {
			return "pbm";
		} else if (ret.equals("P2")) {
			return "pgm";
		} else if (ret.equals("P3")) {
			return "ppm";
		}
		return null;
	}

	public static IPixel[][] getImage(String file) throws Exception {
		Scanner sc =  new Scanner(new FileInputStream(file));

		sc.nextLine();
		sc.nextLine();
		
		String taille = sc.nextLine();
		StringTokenizer st = new StringTokenizer(taille, " ");
		
		int nbCol = Integer.parseInt(st.nextToken());
		int nbLig = Integer.parseInt(st.nextToken());
		System.out.println("nbcol = "+nbCol+" et nbLig = "+nbLig);
		sc.nextLine();
		
		Pixel image[][] = new Pixel[nbLig][nbCol];
		short value = 0;
		for (int i = 0; i < nbLig; i++) {
			for (int j = 0; j < nbCol; j++) {
				value = (short) Integer.parseInt(sc.nextLine());
				if (value <= 255 && value >= 0) {
					image[i][j] = new Pixel();
					image[i][j].setValue(value);
				} else {
					throw new Exception("les donnees du fichier sont inexploitables");
				}
			}
		}
		
		sc.close();
		
		return image;
	}
	
	public static void saveImage(IImage img, String file) throws Exception{
		
		if (img == null) {
			throw new Exception("image nulle");
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		bw.write("P2");
		bw.newLine();
		bw.write("# CREATOR : Ariane-Arthur-Sidoin");
		bw.newLine();
		
		bw.write(img.getNombreColonnes()+"");
		bw.write(" ");
		bw.write(img.getNombreLignes()+"");
		bw.newLine();
		bw.write("255");
		bw.newLine();
		
		int value;
		for (int i = 0; i < img.getNombreLignes(); i++) {
			for (int j = 0; j < img.getNombreColonnes(); j++) {
				value = img.getImage()[i][j].getValue();
				bw.write(value+"");
				bw.newLine();
			}
		}
		
		bw.close();
	}
	
	public static void afficherImage(IPixel[][] img){
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				System.out.print(img[i][j].getValue()+ "  ");
			}
			System.out.println();
		}
	}
	
	public static ElementStructurant getEltStruct(String file) throws FileNotFoundException{
		ElementStructurant elt = new ElementStructurant();
		
		Scanner sc = new Scanner(new FileInputStream(file));
		StringTokenizer st = new StringTokenizer(sc.nextLine(), " ");
		
		elt.setNbLigne(Integer.parseInt(st.nextToken()));
		elt.setNbColonne(Integer.parseInt(st.nextToken()));
		
		st = new StringTokenizer(sc.nextLine(), " ");
		
		elt.setxCentre(Integer.parseInt(st.nextToken()));
		elt.setyCentre(Integer.parseInt(st.nextToken()));
		
		short e[][] = new short[elt.getNbLigne()][elt.getNbColonne()];
		for(int i = 0; i < elt.getNbLigne(); i++){
			st = new StringTokenizer(sc.nextLine(), " ");
			
			for(int j = 0; j < elt.getNbColonne(); j++){
				e[i][j] = (short) Integer.parseInt(st.nextToken());
			}
		}
		elt.setElt(e);
		sc.close();
		
		return elt;
	}
	
	public static void afficherEltStruct(short[][] img){
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				System.out.print(img[i][j]+ "  ");
			}
			System.out.println();
		}
	}
}
