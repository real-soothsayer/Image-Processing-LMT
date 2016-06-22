package niveauGris;

import java.util.ArrayList;
import java.util.Collections;

import fft.Complex;
import image.IImage;
import image.IPixel;
import image.Image;
import image.ImageAdapter;
import image.Pixel;

public class ImageNiveauGris extends ImageAdapter{
	
	public ImageNiveauGris() {
		
	}
	
	public ImageNiveauGris(IPixel[][] image) {
		this.image =  image;
		this.nombreColonnes = image[0].length;
		this.nombreLignes = image.length;
	}
	
	public IImage egalisationHisto() {
		
		//calcul de l'histogramme dans un tableau h
		int h[] = this.histogramme();
		//normalisation de l'histogramme
		double hn[] = new double[256];
		double nbrePix = nombreLignes * nombreColonnes;
		for (int i = 0; i < 256; i++) {
			hn[i] = (double)h[i] / nbrePix;
		}
		//densité de probabilité normalisé ou histogramme cumulé normalisé
		double c[] = new double[256];
		for (int i = 0; i < 256; i++) {
			c[i] = 0.0;
			for (int j = 0; j < i; j++) {
				c[i] += hn[j];
			}
		}
		//transformation de tous les niveaux de gris
		short[] modele = new short[256];
		for (short i = 0; i < 256; i++) {
			modele[i] = (short) Math.round(255 * c[i]);
		}
		//transformation de tous les pixels
		IImage oimg = this;
		IPixel[][] img = new IPixel[nombreLignes][nombreColonnes];
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				img[i][j] = new Pixel();
				img[i][j].setValue(modele[image[i][j].getValue()]);
			}
		}
		oimg.setImage(img);
		oimg.setNombreLignes(nombreLignes);
		oimg.setNombreColonnes(nombreColonnes);
		
		return oimg;
	}

	public IImage etalementHisto() {
		short min = 255;
		short max = 0;
		short val;
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				val = image[i][j].getValue();
				if (max < val) {
					max = val;
				}
				if (min > val) {
					min = val;
				}
			}
		}
//		System.out.println("min = " + min + "  max = " + max);
		short[] modele = new short[256];
		for (short i = 0; i < 256; i++) {
			modele[i] = (short) Math.round((255 * (i-min)) / (max-min));
//			System.out.println("i = " + i + "  modele[i] = " + modele[i]);
		}
		
		IImage oimg = this;
		IPixel[][] img = new IPixel[nombreLignes][nombreColonnes];
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				img[i][j] = new Pixel();
//				System.out.println("val = " + image[i][j].getValue() + "  modele[val] = " + modele[image[i][j].getValue()]);
				img[i][j].setValue(modele[image[i][j].getValue()]);
			}
		}
		oimg.setImage(img);
		oimg.setNombreLignes(nombreLignes);
		oimg.setNombreColonnes(nombreColonnes);
		
		return oimg;
	}

	public double[][] convolution(double[][] masque) {

		double[][] img = new double[nombreLignes][nombreColonnes];
		int hauteurMasque = masque.length;
		int largeurMasque = masque[0].length;
		int rayonVertical = hauteurMasque/2;
		int rayonHorizontal = largeurMasque/2;
		
		for (int i = 0; i < rayonVertical; i++) {
			for(int j = 0; j < nombreColonnes; j++) {
				img[i][j] = image[i][j].getValue();
			}
		}
		
		for (int l = rayonVertical; l < (nombreLignes - rayonVertical); l++) {
			for (int i = 0; i < rayonHorizontal; i++) {
				img[l][i] = image[l][i].getValue();
			}
			for(int i = rayonHorizontal; i < (nombreColonnes-rayonHorizontal); i++) {
				double pix = 0;
				for(int j = 0; j < hauteurMasque; j++) {
					for (int k = 0; k < largeurMasque; k++) {
						pix += masque[j][k] * image[l - rayonVertical + j][i - rayonHorizontal + k].getValue();
					}
				}
				img[l][i] = pix;
			}
			for (int i = (nombreColonnes-rayonHorizontal); i < nombreColonnes; i++) {
				img[l][i] = image[l][i].getValue();
			}
		}

		for (int i = (nombreLignes - rayonVertical); i < nombreLignes; i++) {
			for(int j = 0; j < nombreColonnes; j++) {
				img[i][j] = image[i][j].getValue();
			}
		}

		return img;
	}
	
	public IImage filtreSobel(short seuil) {

		double masque1[][] = {{1.0/4, 2.0/4, 1.0/4}, {0.0, 0.0, 0.0}, {-1.0/4, -2.0/4, -1.0/4}};
		double masque2[][] = {{1.0/4, 0.0, -1.0/4}, {2.0/4, 0.0, -2.0/4}, {1.0/4, 0.0, -1.0/4}};
		
		double[][] sobelVertical = this.convolution(masque1);
		double[][] sobelHorizontal = this.convolution(masque2);
		
		IImage oimgInt = this;
		IPixel[][] img = new IPixel[nombreLignes][nombreColonnes];
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				img[i][j] = new Pixel();
				short val = (short) Math.sqrt(sobelVertical[i][j] * sobelVertical[i][j] + sobelHorizontal[i][j] * sobelHorizontal[i][j]);
				if (val < 0) {
					val = 0;
				} else if (val > 255) {
					val = 255;
				}
				img[i][j].setValue(val);
			}
		}
		oimgInt.setImage(img);
		oimgInt.setNombreLignes(nombreLignes);
		oimgInt.setNombreColonnes(nombreColonnes);
		
		if (seuil != 0) {
			IImage oimg = ImageNiveauGris.seuillage(oimgInt, seuil);
			return oimg;
		} else {
			return oimgInt;
		}

	}
	
	public IImage filtrePrewitt(short seuil) {
		double masque1[][] = {{1.0/3, 1.0/3, 1.0/3}, {0.0, 0.0, 0.0}, {-1.0/3, -1.0/3, -1.0/3}};
		double masque2[][] = {{1.0/3, 0.0, -1.0/3}, {1.0/3, 0.0, -1.0/3}, {1.0/3, 0.0, -1.0/3}};
		
		double[][] sobelVertical = this.convolution(masque1);
		double[][] sobelHorizontal = this.convolution(masque2);
		
		IImage oimgInt = this;
		IPixel[][] img = new IPixel[nombreLignes][nombreColonnes];
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				img[i][j] = new Pixel();
				short val = (short) Math.sqrt(sobelVertical[i][j] * sobelVertical[i][j] + sobelHorizontal[i][j] * sobelHorizontal[i][j]);
				if (val < 0) {
					val = 0;
				} else if (val > 255) {
					val = 255;
				}
				img[i][j].setValue(val);
			}
		}
		oimgInt.setImage(img);
		oimgInt.setNombreLignes(nombreLignes);
		oimgInt.setNombreColonnes(nombreColonnes);
		
		if (seuil != 0) {
			IImage oimg = ImageNiveauGris.seuillage(oimgInt, seuil);
			return oimg;
		} else {
			return oimgInt;
		}

	}
	
	public IImage filtreMedian(int taille) {
		Pixel[][] sortie = new Pixel[this.getNombreLignes()][this.getNombreColonnes()];
		
		Pixel[][] p =  (Pixel[][]) this.getImage();
		
		// Traitement des pixels externes
		for(int i = 0; i < this.getNombreLignes(); i++){
			for(int j = 0; j < this.getNombreColonnes(); j++){
				sortie[i][j] = new Pixel();
				sortie[i][j].setValue(p[i][j].getValue());
			}
		}
		
		ArrayList<Integer> filtre = new ArrayList<Integer>();
		
		// Traitement des pixels internes
		for(int i = taille/2; i < this.nombreLignes - taille/2; i++){
			for(int j = taille/2; j < this.nombreColonnes - taille/2; j++){
				
				for(int l = -taille/2; l <= taille/2; l++){
					for (int c = -taille/2; c <= taille/2; c++){
						filtre.add((int) p[i+l][j+c].getValue());
					}
				}
				
				Collections.sort(filtre);
				short median = filtre.get((taille*taille)/2).shortValue();
				sortie[i][j].setValue(median);
				filtre.clear();
			}
			System.out.println("i = "+i);
		}
		
		ImageNiveauGris img = new ImageNiveauGris(sortie);
		return img;
	}
	
	public IImage filtrePasseHaut(int rayon) {
				
		IPixel[][] mat = this.FFT();
		int lig = mat.length;
		int col = mat[0].length;
		
		this.setImage(mat);
		this.setNombreColonnes(col);
		this.setNombreLignes(lig);
		
		int lig1 = lig/2;
		int col1 = col/2;
		for(int i=0; i<lig1; i++){
			for(int j=0; j<col1; j++){
				if(Math.sqrt((lig1-i)*(lig1-i)+(col1-j)*(col1-j))<rayon){
					this.getImage()[i][j].setValue((short)0);
					this.getImage()[i][col-1-j].setValue((short)0);
					this.getImage()[lig-1-i][j].setValue((short)0);
					this.getImage()[lig-1-i][col-1-j].setValue((short)0);
				}
			}
		}
		
		
		Complex[][] matrix = fft.recentrage(fft.getComplexMatrix());
		lig = matrix.length;
		col = matrix[0].length;
		
		for(int i=0; i<lig; i++){
			for(int j=0; j<col; j++){
				if(this.getImage()[i][j].getValue()==0){
					matrix[i][j].setImg(0);
					matrix[i][j].setReel(0);
				}
			}
		}
		
		fft.setComplexMatrix(fft.recentrage(matrix));
		
		return this;
	}
	
	public IImage filtrePasseBas(int rayon) {
		
		IPixel[][] mat = this.FFT();
		int lig = mat.length;
		int col = mat[0].length;
		
		this.setImage(mat);
		this.setNombreColonnes(col);
		this.setNombreLignes(lig);
		
		int lig1 = lig/2;
		int col1 = col/2;
		for(int i=0; i<lig1; i++){
			for(int j=0; j<col1; j++){
				if(Math.sqrt((lig1-i)*(lig1-i)+(col1-j)*(col1-j))>rayon){
					this.getImage()[i][j].setValue((short)0);
					this.getImage()[i][col-1-j].setValue((short)0);
					this.getImage()[lig-1-i][j].setValue((short)0);
					this.getImage()[lig-1-i][col-1-j].setValue((short)0);
				}
			}
		}
		
		Complex[][] matrix = fft.recentrage(fft.getComplexMatrix());
		lig = matrix.length;
		col = matrix[0].length;
		
		for(int i=0; i<lig; i++){
			for(int j=0; j<col; j++){
				if(this.getImage()[i][j].getValue()==0){
					matrix[i][j].setImg(0);
					matrix[i][j].setReel(0);
				}
			}
		}
		
		fft.setComplexMatrix(fft.recentrage(matrix));
		return this;
	}
	
	public IImage transformeeHough() {
		
		IImage im = this.filtreSobel((short) 40);
		short[][] img = new short[im.getNombreLignes()][im.getNombreColonnes()];
		for (int i = 0; i < im.getNombreLignes(); i++) {
			for (int j = 0; j < im.getNombreColonnes(); j++) {
				img[i][j] = im.getImage()[i][j].getValue();
			}
		}
		
		int hauteurhough = (int) Math.ceil(Math.sqrt(im.getNombreColonnes() * im.getNombreColonnes() + im.getNombreLignes() * im.getNombreLignes()));
		int largeurhough = hauteurhough + 1;
		IPixel[][] filtreHough = new IPixel[hauteurhough][largeurhough];
		
		for (int i = 0; i < hauteurhough; i++) {
			for (int j = 0; j < largeurhough; j++) {
				filtreHough[i][j] = new Pixel();
				filtreHough[i][j].setValue((short) 0);
			}
		}
	
		double pas = (Math.PI)/hauteurhough;
		for (int j = 0; j < im.getNombreLignes(); j++) {
			for (int i = 0; i < im.getNombreColonnes(); i++) {
				if (img[j][i] == 255) {
					double rho;
					int p = 0;
					for (double theta = 0; theta < Math.PI; theta += pas) {
						rho = i*Math.cos(theta) + j*Math.sin(theta);
						if (rho >= 0) {
							short val = (short) (filtreHough[(int) rho][p].getValue() + 2);
							if (val < 256)  {
								filtreHough[(int) rho][p].setValue(val);
							}
						}
						p++;
					}
				}
			}
		}
		
		IImage imageHough = new ImageNiveauGris(filtreHough);

		return imageHough;
	}
	
	public IImage ameliorationContrasteAddition() {
		
		IImage oimgInt = this;
		IPixel[][] img = new IPixel[nombreLignes][nombreColonnes];
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				img[i][j] = new Pixel();
				img[i][j].setValue(image[i][j].getValue());
			}
		}
		oimgInt.setImage(img);
		oimgInt.setNombreLignes(nombreLignes);
		oimgInt.setNombreColonnes(nombreColonnes);
		
		IImage oimg = super.addition(oimgInt);
		
		return oimg;
	}
	
	public static IImage seuillage(IImage img, short seuil) {
		short val;
		IPixel imageFinale[][] = new IPixel[img.getNombreLignes()][img.getNombreColonnes()];
		for (int i = 0; i < img.getNombreLignes(); i++) {
			for (int j = 0; j < img.getNombreColonnes(); j++) {
				val = img.getImage()[i][j].getValue();
				imageFinale[i][j] = new Pixel();
				if (val < seuil) {
					imageFinale[i][j].setValue((short) 0);
				} else {
					imageFinale[i][j].setValue((short) 255);
				}
			}
		}
		IImage oimg = new Image();
		oimg.setImage(imageFinale);
		oimg.setNombreLignes(img.getNombreLignes());
		oimg.setNombreColonnes(img.getNombreColonnes());
		
		return oimg;
	}
	
	public int[] histogramme() {
		int h[] = new int[256];
		short n = 0;
		for (int i = 0; i < 256; i++) {
			h[i] = 0;
		}
		for (int i = 0; i < nombreLignes; i++) {
			for (int j = 0; j < nombreColonnes; j++) {
				n = image[i][j].getValue();
				h[n]++;
			}
		}
		return h;
	}
}
