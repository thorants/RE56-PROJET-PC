package application;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class UpdatePower extends Thread{

	//-------------------------------------------------------
	//------------------------VARIABLES----------------------
    //-------------------------------------------------------
	
	private ArrayList <Mobile> mobileList;
	private Antenna nodeB;
	
	public UpdatePower (Antenna nodeB, ArrayList <Mobile> mobList){
		this.setNodeB(nodeB);
		this.setMobileList(mobList);
	}
	
	//-------------------------------------------------------
	//------------------GETTER AND SETTER--------------------
    //-------------------------------------------------------

	// Getter and Setter mobileList
	public ArrayList <Mobile> getMobileList() {
		return mobileList;
	}

	public void setMobileList(ArrayList <Mobile> mobileList) {
		this.mobileList = mobileList;
	}

	//Getter and Setter Antenna
	public Antenna getNodeB() {
		return nodeB;
	}

	public void setNodeB(Antenna nodeB) {
		this.nodeB = nodeB;
	}
	
	//-------------------------------------------------------
	//------------------------METHODS------------------------
    //-------------------------------------------------------
	
	public void run(){
		while(true){
			updatePower(nodeB, mobileList);
		}
	}
	
	public void updatePower(Antenna nodeB,ArrayList <Mobile> mobileList){
		for(Mobile mob : mobileList){
			nodeB.setPowerInter(PowerCalculation.powerInterf(nodeB,mobileList));
			System.out.println("INTERF: " + nodeB.getPowerInter());
			boolean lastTargetBiggerEstimated = false;
			boolean firstTime = true;
			boolean loop  = true;
			// If power is to big, we power off the communication
			if ((mob.isConnected()) && ((mob.getpEmited() > 16 && mob.getMode() == "Data Mode") 
					|| (mob.getpEmited() > 19 && mob.getMode() == "Data2 Mode") 
					|| (mob.getpEmited() > -0.6 && mob.getMode() == "Voice Mode")) 
					&& (mob.getpEmited() != -100)) {
				mob.setConnected(false);
				mob.setpEmited(-100);
				mob.setImg("res/phone_off.jpg");
				Image img = new Image(mob.getImg());
				mob.getImgView().setImage(img);
			}
			if (mob.isConnected()) {	
				while( loop ){
					if (mob.getpEmited() == -100) {
						mob.setpEmited(PowerCalculation.powerEmitted(nodeB, mob));
						System.out.println("------- POWER EMITED INITIALISATION -------- ");
						System.out.println(mob.getpEmited());
					}
					//Other loop, we compare the SIRTarget and the SIREstimated, Then change the power value.
					System.out.println("------- SECOND LOOP: COMPARAISON OF THE SIRs -------- ");
					System.out.println("SIR Target: " + mob.getSirTarget());
					System.out.println("SIR Estimated: " + PowerCalculation.sirEstimated(mobileList, nodeB, mob));
					if ((mob.getSirTarget() > PowerCalculation.sirEstimated(mobileList, nodeB, mob))){
						System.out.println("------- THIRD LOOP: Ajustement of the Phone Power Emited -------- ");
						System.out.println("Case SIR Target > SIR Estimated : increase of power ");
						mob.setpEmited(mob.getpEmited() + 0.1);
						if (!lastTargetBiggerEstimated && !firstTime)
							loop = false;
						lastTargetBiggerEstimated = true;
						firstTime = false;
					}
					if ((mob.getSirTarget() < PowerCalculation.sirEstimated(mobileList, nodeB, mob))){
						System.out.println("------- THIRD LOOP: Ajustement of the Phone Power Emited -------- ");
						System.out.println("Case SIR Target < SIR Estimated : decrease of power ");
						mob.setpEmited(mob.getpEmited() - 0.1);
						if (lastTargetBiggerEstimated && !firstTime)
							loop = false;
						lastTargetBiggerEstimated = false;
						firstTime = false;
					}	
				}
			}
		}
	}
	
}