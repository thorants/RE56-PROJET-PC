package application;

import java.util.ArrayList;

import javafx.scene.chart.XYChart;

public class UpdateGraph extends Thread{

	//-------------------------------------------------------
	//------------------------VARIABLES----------------------
    //-------------------------------------------------------
	
	private int timer;
	private boolean stopThread;
	private ArrayList <Mobile> mobileList;
	
	public UpdateGraph (int timer, ArrayList <Mobile> mobList/*Mobile m, XYChart.Series series*/){
		this.timer = timer;
		this.mobileList = mobList;
		this.stopThread = true;
	}
	
	//-------------------------------------------------------
	//------------------GETTER AND SETTER--------------------
    //-------------------------------------------------------
	
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public boolean isStopThread() {
		return stopThread;
	}

	public void setStopThread(boolean stopThread) {
		this.stopThread = stopThread;
	}
	
	//-------------------------------------------------------
	//------------------------METHODS------------------------
    //-------------------------------------------------------
	
	public void run(){
		while(!this.stopThread){
			
			actualizeGraph(mobileList);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timer++;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void actualizeGraph(ArrayList<Mobile> mobileList){
		for(Mobile m : mobileList){
			if(m.isDisplayGraph()){	
				m.getSeries().getData().add(new XYChart.Data(timer,m.getpEmited()));
			}
		}
	}
}