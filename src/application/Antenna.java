package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Antenna extends Moveable{

	//-------------------------------------------------------
	//------------------------VARIABLES----------------------
    //-------------------------------------------------------
	
	private int nbTel = 0;
	private double powerInter = 0;
	private Circle circle;

	
	public Antenna(double power,double gain){
		super(power,((565/2)-17.5),((645/2)-17.5),gain, "res/cell-tower.png");
		this.circle = new Circle((565/2),(645/2),200);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
	}
	
	//getter and setter nbTel
	public int getNbTel() {
		return nbTel;
	}

	public void setNbTel(int nbTel) {
		this.nbTel = nbTel;
	}
	
	//getter and setter powerInter
	public double getPowerInter() {
		return powerInter;
	}

	public void setPowerInter(double powerInter) {
		this.powerInter = powerInter;
	}
	
	//getter and setter circle
	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	
	//-------------------------------------------------------
	//------------------------METHODS------------------------
    //-------------------------------------------------------
	
	public String doubleToStr(double value){
		String strValue = Double.toString(value);
		return strValue;
	}
}