
package fft;

import image.IPixel;
import image.Pixel;

public class FFT2D {
	
	int nbLigne;
	int nbColonne;
	Complex[][] complexMatrix;
	
	
	public Complex[][] getComplexMatrix() {
		return complexMatrix;
	}

	public void setComplexMatrix(Complex[][] complexMatrix) {
		this.complexMatrix = complexMatrix;
	}

	public int getNbLigne() {
		return nbLigne;
	}

	public void setNbLigne(int nbLigne) {
		this.nbLigne = nbLigne;
	}

	public int getNbColonne() {
		return nbColonne;
	}

	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}
	
	public Complex[][] fft2D(Complex[][] matrix, int sens) {
		
		//Complex[][] matrix = complexion(image);
		
		int height, width = height = matrix.length;
		
		Complex[][] result = new Complex[width][width];
		
		Complex[] imgWork = new Complex[width];
		
		for(int i=0; i<height; i++){
			imgWork = getImageLineVector(i, matrix);
			imgWork = fft1D(imgWork, sens);
			for(int j=0; j<width; j++){
				result[i][j] = imgWork[j]; 
			}
		}
		
		
		for(int j=0; j<width; j++){
			imgWork = getImageColumnVector(j, result);
			
//			System.out.println();
//			for(int k=0; k<imgWork.length;k++) System.out.println(imgWork[k].getReel()+" "+imgWork[k].getImg());
			
			imgWork =fft1D(imgWork, sens);
			for(int i=0; i<height; i++){
				result[i][j] = imgWork[i]; 
			}
		}
		
		complexMatrix = result;
		return result;
	}
	
	
	private Complex[] getImageColumnVector(int col, Complex[][] matrix){
		Complex[] result = new Complex[matrix.length];
		
		for(int i = 0; i < matrix.length; i++){
			result[i] = matrix[i][col];
		}
		
		return result;
	}
	
	private Complex[] getImageLineVector(int lig, Complex[][] matrix){
		Complex[] result = new Complex[matrix[0].length];
		
		for(int j = 0; j < matrix[0].length; j++){
			result[j] = matrix[lig][j];
		}
		
		return result;
	}
	
	public double[][] getRealImage(Complex[][] matrix){
		double[][] real = new double[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				//System.out.print(matrix[i][j].getReel()+"   ");
				real[i][j] = matrix[i][j].getReel();
			}
			//System.out.println();
		}
		return real;
	}
	
	public double[][] getImgImage(Complex[][] matrix){
		double[][] img = new double[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				img[i][j] = matrix[i][j].getImg();
			}
		}
		return img;
	}
	
	public IPixel[][] centrage(double[][] matrix){
		double min, max = min = matrix[0][0];
		int lig = matrix.length;
		int col = matrix[0].length;
		double val;
		
		
		
		IPixel[][] result = new IPixel[lig][col]; 
		
		for(int i=0; i<lig; i++){
			for(int j=0; j<col; j++){
				if(matrix[i][j] < min){
					min = matrix[i][j];
				}
				if(matrix[i][j] > max){
					max = matrix[i][j];
				}
			}
		}
		
		val = max - min;
		
		for(int i=0; i<lig; i++){
			for(int j=0; j<col; j++){
				result[i][j] = new Pixel();
				result[i][j].setValue((short) (255*matrix[i][j]/val - 255*min/val));
			}
		}
		return result;
	}
	
	
	
	private Complex[] _fft1D(Complex[] vecteur, int sens){
		int width = vecteur.length;
		Complex[] result = vecteur;
		
			if(width != 1){
				Complex[] MPair = new Complex[width/2];
				Complex[] MImpair = new Complex[width/2];
				
				
				for(int i = 0; i<width; i++){
					if(i%2==0){
						MImpair[(i)/2] = vecteur[i];
					}
					else{
						MPair[i/2] = vecteur[i];
					}
				}
				
				Complex[] ImgPair = _fft1D(MPair, sens);
				Complex[] ImgImpair = _fft1D(MImpair, sens);
				
				Complex coef;
				double theta;
				
				for(int u = 0; u<width/2; u++){
					theta = (sens*2*u*Math.PI)/width;
					coef = new Complex(Math.cos(theta), Math.sin(theta));
					result[u] = ImgPair[u].add(coef.multiply(ImgImpair[u]));
					result[u + (width/2)] = ImgPair[u].sub(coef.multiply(ImgImpair[u]));					
				}
			}		
		
		return result;
	}

	
	private Complex[] fft1D(Complex[] vecteur, int sens){
		
		int width = vecteur.length;
		Complex[] result = new Complex[width];
		Complex[] imgTemp1 = new Complex[width];
		Complex[] imgTemp2;
		

		for(int i=0; i<width ; i++){
			imgTemp1[i] = vecteur[width-1-i];
		}

		imgTemp2 = _fft1D(imgTemp1, sens);

		for(int i=0; i<width ; i++){
			result[i] = imgTemp2[width-1-i];
		}

		if(sens == 1){
			for(int i=0; i<width ; i++){
				result[i] = result[i].RealDiv(width);
			}
		}
		return result;
	}
	
	public Complex[][] recentrage(Complex[][] matrix){
		int taille = matrix.length;
		
		Complex[][] result = new Complex[taille][taille]; 
		
		for(int i=0; i<taille; i++){
			for(int j=0; j < taille/2; j++){
				int valeur = i+taille/2;
				if (valeur >= taille) {
					valeur = valeur - taille;
				}
				result[i][j] = matrix[valeur][j+taille/2]; 
				result[valeur][j+taille/2] = matrix[i][j];
				//System.out.println(i+" "+j+" "+valeur+" "+(j+taille/2));
			}
		}		
		return result;
	}
	
	public double[][] logarithme(Complex[][] matrix){
		
		int lig = matrix.length;
		int col = matrix[0].length;
		double[][] result = new double[lig][col];
		
		for(int i=0; i<lig; i++){
			for(int j=0; j<col; j++){
				result[i][j] = Math.log( 1 + Math.sqrt(matrix[i][j].getReel()*matrix[i][j].getReel()+matrix[i][j].getImg()*matrix[i][j].getImg()));
			}
		}
		
		return result;
	}
	
	public IPixel[][] inverseFFT(){
		Complex[][] matrix2 = fft2D(complexMatrix, 1);

		double[][] result2 = new double[nbLigne][nbColonne];

		for(int i=0; i<nbLigne; i++){
			for(int j=0; j<nbColonne; j++){
				result2[i][j] = Math.abs(matrix2[i][j].getReel())+Math.abs(matrix2[i][j].getImg());
			}
		}
		return centrage(result2);
	}
}
