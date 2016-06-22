package image;

public class Pixel implements IPixel{
	private short value;
	
	public short getValue() {
		return value;
	}
	public void setValue(short val) {
		this.value = val;
	}
}
