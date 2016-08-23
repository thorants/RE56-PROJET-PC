
package application;

import java.util.ArrayList;

public class PowerCalculation {

	//-------------------------------------------------------
	//------------------------METHODS------------------------
    //-------------------------------------------------------
	
	public static double calculPuissanceRecu(Moveable Receiver, Moveable Source) {
		double power;

		/* Computation of the distance between the 2 devices */
		double x = (double) ((Receiver.getxPos() - Source.getxPos()) * 3) / 200;
		double y = (double) ((Receiver.getyPos() - Source.getyPos()) * 3) / 200;
		double dist = (double) Math.sqrt((x * x + y * y));

		double loss = (double) (32.44 + 20 * Math.log10(Source.getFrequency()) + 20 * Math.log10(dist));

		/* Friss Formula */
		power = (double) (Source.getpEmited() + Source.getGain() - loss + Receiver.getGain());
		
		return power;  /* dBm */
	}

	// Function that calculate the interferences
	// Antenna and list of all mobiles as parameters
	public static double powerInterf(Antenna nodeB, ArrayList<Mobile> mobiles) {

		double interferences = 0;
		int i=0;

		for (Mobile mob : mobiles) {
			if (mob.isConnected()) {
				i++;
				interferences = interferences + calculPuissanceRecu(nodeB, mob);
			}

			nodeB.setNbTel(i);
		}
		if (nodeB.getNbTel() != 0) {
			double finalPowerInt = interferences/nodeB.getNbTel();
			return finalPowerInt;
		} else
			return 0;

	}
	
	//Calculation of the power that will be emitted by the Phone.
	//Phone and Antenna as parameter
	public static double powerEmitted(Antenna nodeB, Mobile mobile) {
		double powerEmitted = 0;
		
		powerEmitted = Math.pow(10, ((nodeB.getpEmited()/10))-3) - Math.pow(10, ((calculPuissanceRecu(mobile, nodeB)/10))-3)
					+ Math.pow(10, ((mobile.getSirTarget()/10))-3) + Math.pow(10, ((nodeB.getPowerInter())/10));

		powerEmitted = 10 * Math.log10(powerEmitted);
		
		return powerEmitted;

	}

	 //Calculation of the SIR Estimated 
	public static double sirEstimated(ArrayList<Mobile> mobiles, Antenna nodeB,Mobile mobile) {

		double powerInt = 0;
		// gaussian Noise -> est à -60 dbm = 0.000001 mW
		double gaussianNoise = 0.00001;
		for (Mobile mob : mobiles) {
			if (mob.isConnected() && (mob != mobile)) {
				powerInt = powerInt + Math.pow(10, ((calculPuissanceRecu(nodeB, mobile)/10)));
			}

		}
		double puissanceEnWatt = Math.pow(10, ((calculPuissanceRecu(nodeB, mobile)/10)));
		double sirEstimatedEndB = 10 * Math.log10((puissanceEnWatt * (mobile.getsF())) / (powerInt + gaussianNoise));
		return sirEstimatedEndB;
	}

	 //Function to obtain a BLER Target value
	public static double blerEstimate(Mobile mobile) {
		double i = mobile.getBlerTarget();
		return i;
	}
}