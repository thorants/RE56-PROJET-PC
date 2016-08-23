package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Moveable {
	
	//-------------------------------------------------------
	//------------------------VARIABLES----------------------
    //-------------------------------------------------------
	
	private String imgName;
	private ImageView imgView;
	private double xPos;
	private double yPos;
	private double gain; //In dB
	private double frequency;// In MHz
	private double pEmited;
	
	public Moveable(double pEmited,double xPos, double yPos,double gain, String imgName){
		this.pEmited = pEmited;
		this.xPos = xPos;
		this.yPos = yPos;
		this.imgView = new ImageView();
		this.imgName = imgName;
		Image img = new Image(imgName);
		imgView.setImage(img);
		imgView.setX(xPos);
		imgView.setY(yPos);
		this.frequency = 900;
		this.gain = gain;
	}
	
	//-------------------------------------------------------
	//------------------GETTER AND SETTER--------------------
    //-------------------------------------------------------
	
	//Getter and setter img
	public String getImg() {
		return imgName;
	}
	public void setImg(String img) {
		this.imgName = img;
	}
	
	//Getter and setter imgView
	public ImageView getImgView() {
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	
	//Getter and setter xPos
	public double getxPos() {
		return xPos;
	}
	public void setxPos(double xPos) {
		this.xPos = xPos;
	}
	
	//getter and setter yPos
	public double getyPos() {
		return yPos;
	}
	public void setyPos(double yPos) {
		this.yPos = yPos;
	}
	
	//getter and setter Gain
	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	//getters and setters for frequency
	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	// getters and setters power emission
	public double getpEmited() {
		return pEmited;
	}

	public void setpEmited(double pEmited) {
		this.pEmited = pEmited;
	}
}