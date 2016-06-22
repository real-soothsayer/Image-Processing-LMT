package fft;

import image.IPixel;

public class FFT {

	public FFT() {
	}

	public Complex[][] fft(IPixel[][] matrix ){

		int height, width;
		height = matrix.length;
		width = matrix[0].length;

		Complex[][] result = new Complex[height][width];

		double coef = -2*Math.PI/(width*height);

		for(int u=0; u<height; u++){
			for(int v=0; v<width; v++){

				result[u][v] = new Complex(0, 0); 

				for(int x=0; x<width; x++){
					for(int y=0; y<height; y++){						
						coef*=(u*x+v*y);						
						result[u][v] = result[u][v].add(new Complex(matrix[x][y].getValue()*Math.cos(coef), matrix[x][y].getValue()*Math.sin(coef))); 
					}
				}
			}
		}
		return result;
	}
}
