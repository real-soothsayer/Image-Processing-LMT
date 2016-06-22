package imageBinaire;

import niveauGris.ImageNiveauGris;
import image.ElementStructurant;
import image.IImage;
import image.IPixel;
import image.ImageAdapter;
import image.Pixel;

public class ImageBinaire  extends ImageAdapter{
	private short valObjet;
	
	public ImageBinaire() {
		
	}
	
	public ImageBinaire(IPixel[][] image) {
		this.image = image;
		this.nombreColonnes = image[0].length;
		this.nombreLignes = image.length;
		this.valObjet = this.valeurObjet();
	}
	
	private short valeurObjet() {
		int nombrePixNoir = 0;
		int nombrePixBlanc = 0;
		
		for (int i = 0; i < this.nombreColonnes; i++) {
			if (this.image[0][i].getValue() == 0) {
				nombrePixNoir++;
			} else {
				nombrePixBlanc++;
			}
		}
		for (int i = 1; i < this.nombreLignes - 1; i++) {
			if (this.image[i][0].getValue() == 0) {
				nombrePixNoir++;
			} else {
				nombrePixBlanc++;
			}
			if (this.image[i][this.nombreColonnes-1].getValue() == 0) {
				nombrePixNoir++;
			} else {
				nombrePixBlanc++;
			}
		}

		for (int i = 0; i < this.nombreColonnes; i++) {
			if (this.image[this.nombreLignes-1][i].getValue() == 0) {
				nombrePixNoir++;
			} else {
				nombrePixBlanc++;
			}
		}
		
		if (nombrePixBlanc > nombrePixNoir) {
			return 0;
		} else {
			return 255;
		}
	}
	
	public IImage Erosion(ElementStructurant elt) {
		int x = elt.getxCentre();
		int y = elt.getyCentre();
				
		short valFond;
		if(valObjet == 0){
			valFond = 255;
		}else{
			valFond = 0;
		}
		
		Pixel[][] p = (Pixel[][]) this.getImage(); 
		Pixel[][] sortie = new Pixel[this.getNombreLignes()][this.getNombreColonnes()];
		short[][] e = elt.getElt();
		
		for(int i = 0; i < this.getNombreLignes(); i++){
			for(int j = 0; j < this.getNombreColonnes(); j++){
				sortie[i][j] = new Pixel();
				sortie[i][j].setValue(p[i][j].getValue());
			}
		}
		
		for(int i = x; i < this.getNombreLignes() - x; i++){
			for(int j = y; j < this.getNombreColonnes() - y; j++){
				sortie[i][j] = new Pixel();
				sortie[i][j].setValue(p[i][j].getValue());
				if(p[i][j].getValue() == valObjet){
					boolean b = false;
					
					for(int k = -x; k <= x && !b; k++){
						for(int t = -y; t <= y && !b; t++){
							if(e[x+k][t+y] == 1 && p[i+x][j+y].getValue() == valFond){
								b =  true;
							}
						}
					}
					if(b){
						sortie[i][j].setValue((short) valFond);
					}
				}
			}
		}
		IImage img = new ImageBinaire(sortie);
		return img;	
	}
	
	public IImage Dilatation(ElementStructurant elt) {
		int x = elt.getxCentre();
		int y = elt.getyCentre();
	
		short valFond;
		if(valObjet == 0){
			valFond = 255;
		}else{
			valFond = 0;
		}
		
		Pixel[][] p = (Pixel[][]) this.getImage(); 
		Pixel[][] sortie = new Pixel[this.getNombreLignes()][this.getNombreColonnes()];
		short[][] e = elt.getElt();
		
		for(int i = 0; i < this.getNombreLignes(); i++){
			for(int j = 0; j < this.getNombreColonnes(); j++){
				sortie[i][j] = new Pixel();
				sortie[i][j].setValue(p[i][j].getValue());
			}
		}
		
		for(int i = x; i < this.getNombreLignes() - x; i++){
			for(int j = y; j < this.getNombreColonnes() - y; j++){
				sortie[i][j] = new Pixel();
				sortie[i][j].setValue(p[i][j].getValue());
				if(p[i][j].getValue() == valFond){
					boolean b = false;
					
					for(int k = -x; k <= x && !b; k++){
						for(int t = -y; t <= y && !b; t++){
							if(e[x+k][y+t] == 1 && p[i+k][j+t].getValue() == valObjet){
								b =  true;
							}
						}
					}
					if(b){
						sortie[i][j].setValue((short) valObjet);
					}
				}
			}
		}
		IImage img = new ImageBinaire(sortie);
		return img;	
	}
	
	public IImage Ouverture(ElementStructurant eltS) {
		ImageBinaire img = (ImageBinaire) this.Erosion(eltS);
		img =  (ImageBinaire) img.Dilatation(eltS);
		return img;
	}
	
	public IImage Fermeture(ElementStructurant eltS) {
		ImageBinaire img =  (ImageBinaire) this.Dilatation(eltS);
		img = (ImageBinaire) img.Erosion(eltS);
		return img;
	}
	
	public IImage GradiantMorphologique(ElementStructurant eltS) {
		ImageBinaire imgDil =  (ImageBinaire) this.Dilatation(eltS);
		ImageBinaire imgEro =  (ImageBinaire) this.Erosion(eltS);
		if (this.valObjet == 255) {
			return (ImageBinaire) imgDil.soustraction(imgEro);
		} else if (this.valObjet == 0) {
			return (ImageBinaire) imgEro.soustraction(imgDil);
		}
		return null;
	}
}
