package fft;

public class Complex {
	
	private double reel;
	private double img;
	
	public double getReel() {
		return reel;
	}

	public void setReel(double reel) {
		this.reel = reel;
	}

	public double getImg() {
		return img;
	}

	public void setImg(double img) {
		this.img = img;
	}

	public Complex(double reel, double img) {
		this.reel = reel;
		this.img = img;
	}
	
	public Complex add(Complex c){
		return new Complex(this.reel+c.reel, this.img + c.img);
	}
	
	public Complex multiply(Complex c){
		return new Complex(this.reel*c.reel - this.img*c.img, this.reel*c.img + this.img*c.reel);
	}
	
	public Complex sub(Complex c) {
		return new Complex(this.reel-c.reel, this.img - c.img);
	}
	
	public Complex RealDiv(double val) {
		return new Complex(reel/val, img/val);
	}
	/*
	public static void main(String[] arg0){
		Complex c = new Complex(1,1);
		c = c.add(new Complex(-1, 2));
		System.out.println(c.reel+"+"+c.img+"i");
	}*/
}
