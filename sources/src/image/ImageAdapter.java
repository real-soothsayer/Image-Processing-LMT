package image;

import fft.Complex;
import fft.FFT2D;

public abstract class ImageAdapter  implements IImage{
	protected IPixel[][] image;
	protected int nombreLignes;
	protected int nombreColonnes;
	protected String type;
	protected static FFT2D fft;
	
	public void setImage(IPixel[][] valeurs) {
		this.image = valeurs;
	}
	public IPixel[][] getImage() {
		return this.image;
	}
	
	public void setNombreColonnes(int col) {
		this.nombreColonnes = col;
	}
	public int getNombreColonnes() {
		return this.nombreColonnes;
	}
	
	public void setNombreLignes(int lign) {
		this.nombreLignes = lign;
	}
	public int getNombreLignes() {
		return this.nombreLignes;
	}

	public IImage inverser() {
		short[] modele = new short[256];
		for (short i = 0; i < 255; i++) {
			modele[i] = (short) (255-i);
		}

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
	public IImage rotation(double theta) {
		//transformation des coordonnees
		int[][] xPrime = new int[nombreLignes][nombreColonnes];
		int[][] yPrime = new int[nombreLignes][nombreColonnes];
		int xMin = 0;
		int xMax = 0;
		int yMin = 0;
		int yMax = 0;
		for (int y = 0; y < nombreLignes; y++) {
			for (int x = 0; x < nombreColonnes; x++) {
				xPrime[y][x] = (int) Math.round(x*Math.cos(theta) - y*Math.sin(theta));
				yPrime[y][x] = (int) Math.round(x*Math.sin(theta) + y*Math.cos(theta));
				if (y == 0 && x == 0) {
					xMin = xPrime[0][0];
					xMax = xPrime[0][0];
					yMin = yPrime[0][0];
					yMax = yPrime[0][0];
				} else {
					if (xPrime[y][x] < xMin) {
						xMin = xPrime[y][x];
					}
					if (xPrime[y][x] > xMax) {
						xMax = xPrime[y][x];
					}
					if (yPrime[y][x] < yMin) {
						yMin = yPrime[y][x];
					}
					if (yPrime[y][x] > yMax) {
						yMax = yPrime[y][x];
					}
				}
			}
		}
		
		int nbLignFin = yMax - yMin + 1;
		int nbColFin = xMax - xMin + 1;
		
		IImage oimg = this;
		IPixel[][] img = new IPixel[nbLignFin][nbColFin];
		for (int i = 0; i < nbLignFin; i++) {
			for (int j = 0; j < nbColFin; j++) {
				img[i][j] = new Pixel();
//				img[i][j].setValue((short) 255);
			}
		}
		for (int i = 0; i < nbLignFin; i++) {
			for (int j = 0; j < nbColFin; j++) {

				int xPrimeInv = (int) Math.round((j + xMin)*Math.cos(-theta) - (i + yMin)*Math.sin(-theta));
				int yPrimeInv = (int) Math.round((j + xMin)*Math.sin(-theta) + (i + yMin)*Math.cos(-theta));
				
				if (xPrimeInv < nombreColonnes && xPrimeInv >= 0 && yPrimeInv < nombreLignes && yPrimeInv >= 0) {
					img[i][j].setValue(this.image[yPrimeInv][xPrimeInv].getValue());
				}
//				System.out.print("(" + (yPrime[i][j] - yMin) + ", " + (xPrime[i][j] - xMin) + ")\t");
			}
//			System.out.println();
		}
		
		oimg.setImage(img);
		oimg.setNombreLignes(nbLignFin);
		oimg.setNombreColonnes(nbColFin);
		
		return oimg;
		/*
		//rotation des sommets de l'image
		int ghx = (int) Math.round(0*Math.cos(theta) - 0*Math.sin(theta));//coordonnees (0, 0)
		int ghy = (int) Math.round(0*Math.sin(theta) + 0*Math.cos(theta));
		
		int dhx = (int) Math.round((nombreColonnes- 1)*Math.cos(theta) - 0*Math.sin(theta));//coordonnees (nbCol, 0)
		int dhy = (int) Math.round((nombreColonnes- 1)*Math.sin(theta) + 0*Math.cos(theta));
		
		int gbx = (int) Math.round(0*Math.cos(theta) - (nombreLignes - 1)*Math.sin(theta));//coordonnees (0, nbLign)
		int gby = (int) Math.round(0*Math.sin(theta) + (nombreLignes - 1)*Math.cos(theta));
		
		int dbx = (int) Math.round((nombreColonnes- 1)*Math.cos(theta) - (nombreLignes - 1)*Math.sin(theta));//coordonnees (nbCol, nbLign)
		int dby = (int) Math.round((nombreColonnes- 1)*Math.sin(theta) + (nombreLignes - 1)*Math.cos(theta));*/
	
	}
	public IImage flip(int sens) {
		IPixel[][] result = new IPixel[nombreLignes][nombreColonnes];
		
		if(sens==0){  //flip horizontal
			for(int i=0; i<nombreLignes; i++){
				for(int j=0; j<nombreColonnes; j++){
					result[i][j] = new Pixel(); 
					result[i][j].setValue(this.image[i][nombreColonnes-1-j].getValue());
				}
			}
		}
		else{ 	// flip vertical
			for(int i=0; i<nombreLignes; i++){
				for(int j=0; j<nombreColonnes; j++){
					result[i][j] = new Pixel();
					result[i][j].setValue(this.image[nombreLignes-1-i][j].getValue());
				}
			}
		}
		
		IImage ret = this;
		ret.setImage(result);
		ret.setNombreLignes(nombreLignes);
		ret.setNombreColonnes(nombreColonnes);
		
		return ret;
	}
	
	public IImage addition(IImage img) {
		
		int nbLig = this.getNombreLignes();
		if(nbLig < img.getNombreLignes()){
			nbLig = img.getNombreLignes();
		}
		
		int nbCol = this.getNombreColonnes();
		if(nbCol < img.getNombreColonnes()){
			nbCol = img.getNombreColonnes();
		}
		
		Pixel image[][] = new Pixel[nbLig][nbCol];
		short value = 0;
		int i, j;
		for(i = 0; i < this.getNombreLignes() && i < img.getNombreLignes(); i++){
			
			for(j = 0; j < this.getNombreColonnes() && j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (this.getImage()[i][j].getValue() + img.getImage()[i][j].getValue());
				if(value > 255){
					value = 255;
				}
				image[i][j].setValue(value);
			}
			
			// Si le nombre de colonne de la première image est plus grande que le nombre de colonne de la seconde
			for(; j < this.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (this.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
			
			// Si le nombre de colonne de la deuxième image est plus grande que le nombre de colonne de la première
			for(; j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (img.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
		}
		
		// Si le nombre de ligne de la première image est plus grande que le nombre de ligne de la seconde
		for(; i < this.getNombreLignes(); i++){
			
			for(j = 0; j < this.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (this.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
			
			for(; j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				value = 0;
				image[i][j].setValue(value);
			}
		}
		
		// Si le nombre de ligne de la deuxième image est plus grande que le nombre de ligne de la première
		for(; i < img.getNombreLignes(); i++){
			
			for(j = 0; j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (img.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
			
			for(; j < this.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				value = 0;
				image[i][j].setValue(value);
			}
		}
		
		this.setNombreLignes(image.length);
		this.setNombreColonnes(image[0].length);
		this.setImage(image);
		return this;
	}
	
	public IImage soustraction(IImage img) {
		int nbLig = this.getNombreLignes();
		if(nbLig < img.getNombreLignes()){
			nbLig = img.getNombreLignes();
		}
		
		int nbCol = this.getNombreColonnes();
		if(nbCol < img.getNombreColonnes()){
			nbCol = img.getNombreColonnes();
		}
		
		Pixel image[][] = new Pixel[nbLig][nbCol];
		short value = 0;
		int i, j;
		for(i = 0; i < this.getNombreLignes() && i < img.getNombreLignes(); i++){
			
			for(j = 0; j < this.getNombreColonnes() && j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (this.getImage()[i][j].getValue() - img.getImage()[i][j].getValue());
				if(value < 0){
					value = 0;
				}
				image[i][j].setValue(value);
			}
			
			// Si le nombre de colonne de la première image est plus grande que le nombre de colonne de la seconde
			for(; j < this.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (this.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
			
			// Si le nombre de colonne de la deuxième image est plus grande que le nombre de colonne de la première
			for(; j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (255 - img.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
		}
		
		// Si le nombre de ligne de la première image est plus grande que le nombre de ligne de la seconde
		for(; i < this.getNombreLignes(); i++){
			
			for(j = 0; j < this.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (this.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
			
			for(; j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				value = 0;
				image[i][j].setValue(value);
			}
		}
		
		// Si le nombre de ligne de la deuxième image est plus grande que le nombre de ligne de la première
		for(; i < img.getNombreLignes(); i++){
			
			for(j = 0; j < img.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				
				value = (short) (255 - img.getImage()[i][j].getValue());
				image[i][j].setValue(value);
			}
			
			for(; j < this.getNombreColonnes(); j++){
				image[i][j] = new Pixel();
				value = 0;
				image[i][j].setValue(value);
			}
		}

		this.setNombreLignes(image.length);
		this.setNombreColonnes(image[0].length);
		this.setImage(image);
		return this;
	}

	
	public Complex[][] complexion(IPixel[][] image){
		
		int nbLigne = image.length;
		int nbColonne = image[0].length;
		int taille = 2;
		int max = Math.max(nbLigne, nbColonne);
		while(taille<max){
			taille *= 2;
		}
		Complex[][] result = new Complex[taille][taille];
		int i = 0;
		int j = 0;
		for(i=0; i<nbLigne; i++){
			for(j=0; j<nbColonne; j++){
				result[i][j]=new Complex(image[i][j].getValue(), 0);
			}
			for(; j<taille; j++){
				result[i][j]=new Complex(0, 0);
			}
		}
		for(; i<taille; i++){
			for(j=0; j<taille; j++){
				result[i][j]=new Complex(0, 0);
			}
		}
		return result;
	}

	public IPixel[][] FFT() {
		Complex[][] matrix = complexion(this.image);
		fft = new FFT2D();		
		fft.setNbLigne(this.image.length);
		fft.setNbColonne(this.image[0].length);	
		Complex[][] matrix1 = fft.fft2D(matrix, -1);
		matrix = fft.recentrage(matrix1);
		double[][] result = fft.logarithme(matrix);		
		return fft.centrage(result);
	}
	
	public IPixel[][] inverseFFT() {
		
		/*
		 Complex[][] matrix = complexion(this.image);
		
		FFT2D fft = new FFT2D();
		
		Complex[][] matrix1 = fft.recentrage(matrix);
		
		matrix = fft.fft2D(matrix1, -1);
		int lig = matrix.length;
		int col = matrix[0].length;
		
		
		
		double[][] result = new double[lig][col];
		
		
		
		for(int i=0; i<lig; i++){
			for(int j=0; j<col; j++){
				//System.out.println(matrix[i][j].getReel()+" "+matrix[i][j].getImg());
				result[i][j] = Math.log( 1 + Math.sqrt(matrix[i][j].getReel()*matrix[i][j].getReel()+matrix[i][j].getImg()*matrix[i][j].getImg()));
			}
			//System.out.println();
		}
		this.setImage(fft.centrage(result));
		return this; 
		 */
		return fft.inverseFFT();
	}
}
