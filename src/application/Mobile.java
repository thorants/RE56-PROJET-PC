package application;

import javafx.scene.chart.XYChart;

public class Mobile extends Moveable {

	//-------------------------------------------------------
	//------------------------VARIABLES----------------------
    //-------------------------------------------------------
	
	private int idPhone;
	private double sirTarget; // Also COverI - de base en dB
	private double blerTarget; // Also Eb / N0 - de Base en dB
	private double blerEstimated;
	private double sF; // De Base lineaire (watt)
	private boolean connected;
	private boolean displayGraph;
	@SuppressWarnings("rawtypes")
	private XYChart.Series series;
	private String mode;
	private int fast;

	//constructor
	@SuppressWarnings("rawtypes")
	public Mobile(double pEmited,double gain, boolean connected, double sirTarget, double blerTarget, int fast){
		
		super(pEmited,50,50,gain,"res/phone_off.jpg");
		
		this.connected = connected;
		this.sirTarget = sirTarget;
		this.blerTarget = blerTarget;
		this.displayGraph = false;
		this.series = new XYChart.Series();
		this.mode = "No Mode";
		this.setFast(fast);
	}
	
	//-------------------------------------------------------
	//------------------GETTER AND SETTER--------------------
    //-------------------------------------------------------
	
	//getter and setter idPhone
	public int getIdPhone() {
		return idPhone;
	}

	public void setIdPhone(int idPhone) {
		this.idPhone = idPhone;
	}

	//getter and setter connected
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	//getter and setter sirTarget
	public double getSirTarget() {
		return sirTarget;
	}

	public void setSirTarget(double sirTarget) {
		this.sirTarget = sirTarget;
	}

	//getter and setter blerTarget
	public double getBlerTarget() {
		return blerTarget;
	}

	public void setBlerTarget(double blerTarget) {
		this.blerTarget = blerTarget;
	}

	//getter and setter mode
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	//getters and setter sF
	public double getsF() {
		return sF;
	}

	public void setsF(double sF) {
		this.sF = sF;
	}
	
	// getter and setter BlerEstimated
	public double getBlerEstimated() {
		return blerEstimated;
	}

	public void setBlerEstimated(double blerEstimated) {
		this.blerEstimated = blerEstimated;
	}
	
	// getter and setter displayGraph (boolean)
	public boolean isDisplayGraph() {
		return displayGraph;
	}

	public void setDisplayGraph(boolean displayGraph) {
		this.displayGraph = displayGraph;
	}

	//getter and setter series
	@SuppressWarnings("rawtypes")
	public XYChart.Series getSeries() {
		return series;
	}

	public void setSeries(@SuppressWarnings("rawtypes") XYChart.Series series) {
		this.series = series;
	}
	
	public int getFast() {
		return fast;
	}

	public void setFast(int fast) {
		this.fast = fast;
	}
	
	//-------------------------------------------------------
	//------------------------METHODS------------------------
    //-------------------------------------------------------
	
	public String strConnected(boolean connected){
		String strConnected = "False";
		if(this.connected)
			strConnected = "True";
		
		return strConnected;
	}
	
	public String intToStr(int value){
		String strValue = Integer.toString(value);
		return strValue;
	}
	
	public String doubleToStr(double value){
		String strValue = Double.toString(value);
		return strValue;
	}
}