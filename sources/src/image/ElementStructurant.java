package image;

public class ElementStructurant {
	private int nbColonne;
	private int nbLigne;
	private int xCentre;
	private int yCentre;
	private short[][] elt;
	
	public ElementStructurant(){
		nbColonne = 0;
		nbLigne = 0;
		xCentre = 0;
		yCentre = 0;
		elt = null;
	}
	
	public ElementStructurant(int nbLig, int nbCol){
		this.nbLigne = nbLig;
		this.nbColonne = nbCol;
		this.xCentre = nbLig/2;
		this.yCentre =  nbCol/2;
		
		for(int i = 0; i < nbLig; i++){
			for(int j = 0; j < nbCol; j++){
				elt[i][j] = 1;
			}
		}
	}
	
	public ElementStructurant(ElementStructurant elt){
		this.nbLigne = elt.getNbLigne();
		this.nbColonne = elt.getNbColonne();
		this.xCentre = elt.getxCentre();
		this.yCentre =  elt.getyCentre();
		this.elt = elt.getElt();
	}
	
	public ElementStructurant(int taille, String type){
		
		if(type.equals("Carré")){
			this.nbLigne = taille;
			this.nbColonne = taille;
			this.xCentre = taille/2;
			this.yCentre =  taille/2;
			
			elt = new short[nbLigne][nbColonne];
			for(int i = 0; i < taille; i++){
				for(int j = 0; j < taille; j++){
					elt[i][j] = 1;
				}
			}
		}else
		if(type.equals("Etoile")){
			this.nbLigne = taille;
			this.nbColonne = taille;
			this.xCentre = taille/2;
			this.yCentre =  taille/2;
			
			elt = new short[nbLigne][nbColonne];
			for(int i = 0; i < taille; i++){
				for(int j = 0; j < taille; j++){
					if (i == xCentre || j == yCentre){
						elt[i][j] = 1;
					}else{
						elt[i][j] = 0;
					}
				}
			}
		}else
		if (type.equals("Colonne")){
			this.nbLigne = taille;
			this.nbColonne = 1;
			this.xCentre = taille/2;
			this.yCentre =  0;
			
			elt = new short[nbLigne][nbColonne];
			for(int i = 0; i < taille; i++){
				elt[i][0] = 1;
			}
		}else
		if(type.equals("Ligne")){
			this.nbLigne = 1;
			this.nbColonne = taille;
			this.xCentre = 0;
			this.yCentre =  taille/2;
			
			elt = new short[nbLigne][nbColonne];
			for(int i = 0; i < taille; i++){
				elt[0][i] = 1;
			}
		}
		/*
		System.out.println("Element structurant");
		for(int i = 0; i < nbLigne; i++){
			for(int j = 0; j < nbColonne; j++){
				System.out.print(elt[i][j]+"  ");
			}
			System.out.println();
		}//*/	
	}

	public int getNbColonne() {
		return nbColonne;
	}

	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}

	public int getNbLigne() {
		return nbLigne;
	}

	public void setNbLigne(int nbLigne) {
		this.nbLigne = nbLigne;
	}

	public int getxCentre() {
		return xCentre;
	}

	public void setxCentre(int xCentre) {
		this.xCentre = xCentre;
	}

	public int getyCentre() {
		return yCentre;
	}

	public void setyCentre(int yCentre) {
		this.yCentre = yCentre;
	}

	public short[][] getElt() {
		return elt;
	}

	public void setElt(short[][] elt) {
		this.elt = elt;
	}
}
