package image;

public interface IImage {
	public void setImage(IPixel[][] valeurs);
	public IPixel[][] getImage();
	public void setNombreColonnes(int col);
	public int getNombreColonnes();
	public void setNombreLignes(int lign);
	public int getNombreLignes();
	
	public IImage inverser();
	public IImage rotation(double theta);
	public IImage flip(int sens);
}
