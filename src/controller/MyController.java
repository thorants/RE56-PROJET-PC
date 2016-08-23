package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Antenna;
import application.Mobile;
import application.PowerCalculation;
import application.UpdateGraph;
import application.UpdatePower;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MyController implements Initializable {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nodeB = new Antenna(330,5);//power emitted and gain
		nodeB.setxPos(nodeB.getxPos() + 17.5);
		nodeB.setyPos(nodeB.getyPos() + 17.5);
		this.simPane.getChildren().add(nodeB.getCircle());
		this.simPane.getChildren().add(nodeB.getImgView());
	    idPhoneSelected = 0;
	    nbDisplayPhone = 0;
		mobileList = new ArrayList();
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel("Time");
		yAxis.setLabel("Phone Power");
		graph = new LineChart<Number,Number>(xAxis, yAxis);
		this.buttonPane.getChildren().add(graph);
		nodeB.getImgView().setCursor(Cursor.HAND);
		updatePower = new UpdatePower(nodeB, mobileList);
		updatePower.start();
	}
	
	//-------------------------------------------------------
	//------------------------VARIABLES----------------------
    //-------------------------------------------------------
	Antenna nodeB;
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    int idPhoneSelected;
    int nbDisplayPhone;
    NumberAxis xAxis;
    NumberAxis yAxis;
    ArrayList <Mobile> mobileList;
    UpdateGraph updateGraph;
    UpdatePower updatePower;
    
    
	@FXML
	private MenuItem newPhone, powerPhone, voiceMode, dataMode, 
		data2Mode, deletePhone, displayPhone, aboutMenu, hiwMenu, menuVoicePhone, menuDataPhone, menuData2Phone, menuPowerAll;

	@FXML
	private TextField txtId, txtConnected, txtMode, 
		txtBlerTarget, txtSirTarget, txtPowerEmited, txtGain, txtSirEstimated;

	@FXML
	private Pane simPane, buttonPane;
	
	@SuppressWarnings("rawtypes")
	@FXML
	private LineChart graph;

	//-------------------------------------------------------
	//------------------------METHODS------------------------
    //-------------------------------------------------------

	//Function that launch a new simulation
	@SuppressWarnings("unused")
	public void newSimulation(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Launch A New Simulation");
		alert.setHeaderText(null);
		alert.setContentText("Are You sure You want to launch a new simulation ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			for(Mobile m : mobileList){
				this.simPane.getChildren().remove(m.getImgView());
				m = null;
			}
			try{
			graph.getData().clear();
			}catch(NullPointerException npe){
				System.out.println("lineChart =" + graph);
				System.out.println("graph.getData() = " + graph.getData());
			}
			mobileList.clear();
			updateGraph = null;
			updatePower = null;
			this.simPane.getChildren().remove(nodeB.getImgView());
			this.simPane.getChildren().remove(nodeB.getCircle());
			nodeB = null;
			Antenna nodeB;
			UpdateGraph updateGraph;
		    UpdatePower updatePower;
		    ArrayList <Mobile> mobileList;
			this.buttonPane.getChildren().remove(graph);
			this.initialize(null, null);
		}
	}
	
	//Function that add a phone
	public void addPhone(ActionEvent event){
		Mobile mobile = new Mobile(-100,2,false,0,0,0);//power emitted, gain, connected, sirTarget and blerTarget
		this.simPane.getChildren().add(mobile.getImgView());
		if(mobileList.isEmpty())
			mobile.setIdPhone(1);
		else
			mobile.setIdPhone(mobileList.size()+1);
		mobileList.add(mobile);
		mobile.getImgView().setCursor(Cursor.HAND);
		mobile.getImgView().setOnMousePressed(imageViewOnMousePressedEventHandler);
		mobile.getImgView().setOnMouseDragged(imageViewOnMouseDraggedEventHandler);
		updatePower = null;
		updatePower = new UpdatePower(nodeB, mobileList);
		updatePower.start();

	}
	
	// Allow to display the phone on the graph
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public void displayPhone(ActionEvent e){
		if(idPhoneSelected != 0){
			for(Mobile m : mobileList ){
				if(m.getIdPhone() == idPhoneSelected){
					// Cas ou je veux afficher et qu'il n'y a pas d'affichage
					if(!m.isDisplayGraph() && nbDisplayPhone == 0){
						m.setDisplayGraph(!m.isDisplayGraph());
						nbDisplayPhone++;
						m.getSeries().setName("Phone n°" + m.getIdPhone());
						updatePower = null;
						updatePower = new UpdatePower(nodeB, mobileList);
						updatePower.start();
						updateGraph = new UpdateGraph(0, mobileList);
						updateGraph.setStopThread(false);
						updateGraph.start();
						if(m.isDisplayGraph())
							graph.getData().add(m.getSeries());
					}
					else if(!m.isDisplayGraph() && nbDisplayPhone < 3){
						m.setDisplayGraph(!m.isDisplayGraph());
						nbDisplayPhone++;
						updatePower = null;
						updatePower = new UpdatePower(nodeB, mobileList);
						updatePower.start();
						m.getSeries().setName("Phone n°" + m.getIdPhone());
						if(m.isDisplayGraph())
							graph.getData().add(m.getSeries());
					}
					// Cas ou je veux afficher, mais qu'il y a deja trois d'affiché
					else if(!m.isDisplayGraph() && nbDisplayPhone == 3){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Display on Graph");
						alert.setHeaderText(null);
						alert.setContentText("You just can display 3 (three) phones !");
						alert.showAndWait();	
					}
					// Cas ou je veux plus afficher le graph
					else{
						m.setDisplayGraph(!m.isDisplayGraph());
						graph.getData().removeAll(m.getSeries());
						m.setSeries(new XYChart.Series());
						nbDisplayPhone--;
						if(nbDisplayPhone == 0){
							graph.getData().clear();
							updateGraph.setStopThread(true);
							updateGraph.stop();
							updateGraph.setTimer(0);
							updateGraph = null;
							this.buttonPane.getChildren().remove(graph);
							graph = new LineChart<Number,Number>(xAxis, yAxis);
							this.buttonPane.getChildren().add(graph);
						}
					}	
				}
			}
		}
	}
	
	// Create a Phone with all the different values completed (DATA Mode)
	public void createDataPhone(ActionEvent e){
		Mobile mobile = new Mobile(-100,2,false,0,0,1);//power emitted, gain, connected, sirTarget and blerTarget
		this.simPane.getChildren().add(mobile.getImgView());
		if(mobileList.isEmpty())
			mobile.setIdPhone(1);
		else
			mobile.setIdPhone(mobileList.size()+1);
		mobileList.add(mobile);
		mobile.setMode("Data Mode");
		mobile.setBlerTarget(2);
		mobile.setSirTarget(-13.05);
		mobile.setsF(32);
		mobile.getImgView().setCursor(Cursor.HAND);
		mobile.getImgView().setOnMousePressed(imageViewOnMousePressedEventHandler);
		mobile.getImgView().setOnMouseDragged(imageViewOnMouseDraggedEventHandler);
		updatePower = null;
		updatePower = new UpdatePower(nodeB, mobileList);
		updatePower.start();
	}
	
	// Create a Phone with all the different values completed (DATA2 Mode)
	public void createData2Phone(ActionEvent e){
		Mobile mobile = new Mobile(-100,2,false,0,0,1);//power emitted, gain, connected, sirTarget and blerTarget
		this.simPane.getChildren().add(mobile.getImgView());
		if(mobileList.isEmpty())
			mobile.setIdPhone(1);
		else
			mobile.setIdPhone(mobileList.size()+1);
		mobileList.add(mobile);
		mobile.setMode("Data2 Mode");
		mobile.setBlerTarget(1.5);
		mobile.setSirTarget(-10.54);
		mobile.setsF(16);
		mobile.getImgView().setCursor(Cursor.HAND);
		mobile.getImgView().setOnMousePressed(imageViewOnMousePressedEventHandler);
		mobile.getImgView().setOnMouseDragged(imageViewOnMouseDraggedEventHandler);
		updatePower = null;
		updatePower = new UpdatePower(nodeB, mobileList);
		updatePower.start();
	}
	
	// Create a Phone with all the different values completed (VOICE Mode)
	public void createVoicePhone(ActionEvent e){
		Mobile mobile = new Mobile(-100,2,false,0,0,1);//power emitted, gain, connected, sirTarget and blerTarget
		this.simPane.getChildren().add(mobile.getImgView());
		if(mobileList.isEmpty())
			mobile.setIdPhone(1);
		else
			mobile.setIdPhone(mobileList.size()+1);
		mobileList.add(mobile);
		mobile.setMode("Voice Mode");
		mobile.setBlerTarget(4);
		mobile.setSirTarget(-20.08);
		mobile.setsF(256);
		mobile.getImgView().setCursor(Cursor.HAND);
		mobile.getImgView().setOnMousePressed(imageViewOnMousePressedEventHandler);
		mobile.getImgView().setOnMouseDragged(imageViewOnMouseDraggedEventHandler);
		updatePower = null;
		updatePower = new UpdatePower(nodeB, mobileList);
		updatePower.start();
	}
	
	// Power on all the specials phones
	public void powerAll(ActionEvent e){
		for(Mobile m : mobileList){
			if(m.getFast() == 1 && m.isConnected() == false){
				m.setConnected(!m.isConnected());
				m.setImg("res/phone_on.jpg");
				Image img = new Image(m.getImg());
				m.getImgView().setImage(img);
			}
		}
		updatePower.setMobileList(mobileList);
	}

	//Function that gets the different values for a phone after moving it
	public void moveAndSelectDevice(MouseEvent e){
		moveAndSelectPhone(e);
		moveAndSelectAntenna(e);
	}
	
	//Set the new coordinate of the selected phone when we move it
	public void moveAndSelectPhone(MouseEvent e){
		for(Mobile m : mobileList ){
			if(m.getImgView().isHover()){
				System.out.println("CORRESPONDANCE AVEC UN TEL !");
				System.out.println(m.getIdPhone());
				idPhoneSelected = m.getIdPhone();
				m.setxPos(e.getX());
				m.setyPos(e.getY());
				showPhoneInformation(m);
			}
		}
	}
	
	//Set the new coordinate of the antenna when me move it
	// Now just display informations of antenna
	public void moveAndSelectAntenna(MouseEvent e){
		if(nodeB.getImgView().isHover()){
			showAntennaInformation();
		}
	}

	// Switch on/off the selected phone
	public void connectedPhone(ActionEvent event){
		if(idPhoneSelected != 0){
			for(Mobile m : mobileList ){
				if(m.getIdPhone() == idPhoneSelected){
					m.setConnected(!m.isConnected());
					if(m.isConnected()==false)
						m.setImg("res/phone_off.jpg");
					else{
						if(m.getMode() == "No Mode"){
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Mobile mode");
							alert.setHeaderText(null);
							alert.setContentText("You need to select a Mode First ! ");
							alert.showAndWait();
							m.setConnected(false);
						}else{
							m.setImg("res/phone_on.jpg");
							//update(m);
							//updateAll();
						}
					}
					Image img = new Image(m.getImg());
					m.getImgView().setImage(img);
					idPhoneSelected = 0;
				}
			}
		}
	}
	
	// Set informations when VoiceMode selected
	public void setVoiceMode(ActionEvent e){
		if(idPhoneSelected != 0){
			for(Mobile m : mobileList){
				if(m.getIdPhone() == idPhoneSelected){
					if(!m.isConnected()){
						m.setMode("Voice Mode");
						m.setBlerTarget(4);
						m.setSirTarget(-20.08);
						m.setsF(256);
					}
				}
			}
		}
	}
	
	//Set informations when Data Mode Selected
	public void setDataMode(ActionEvent e){
		if(idPhoneSelected != 0){
			for(Mobile m : mobileList){
				if(m.getIdPhone() == idPhoneSelected){
					if(!m.isConnected()){
						m.setMode("Data Mode");
						m.setBlerTarget(2);
						m.setSirTarget(-13.05);
						m.setsF(32);
					}
				}
			}
		}
	}
	
	//Set informations when Data2 Mode selected
	public void setData2Mode(ActionEvent e){
		if(idPhoneSelected != 0){
			for(Mobile m : mobileList){
				if(m.getIdPhone() == idPhoneSelected){
					if(!m.isConnected()){
						m.setMode("Data2 Mode");
						m.setBlerTarget(1.5);
						m.setSirTarget(-10.54);
						m.setsF(16);
					}
				}
			}
		}
	}
	
	//Delete a selected mobile from the simulation
	public void deleteMobilePhone(ActionEvent e){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete a Cell Phone");
		alert.setHeaderText(null);
		alert.setContentText("Are You sure You want to delete the selcted phone ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			if(idPhoneSelected != 0){
				for(Mobile m : mobileList ){
					if(m.getIdPhone() == idPhoneSelected){
						this.simPane.getChildren().remove(m.getImgView());
						m = null;
						mobileList.remove(m);
						idPhoneSelected = 0;
						//updateAll();
					}
				}
			}
		}
	}
	
	//close the application
	public void closeApplication(ActionEvent event){
		System.out.println("Exit the application");
		updateGraph = null;
		updatePower = null;
		Platform.exit();
	}

	// Allow to display selected phone informations in the bottom right Pane
	public void showPhoneInformation(Mobile m){
		txtId.setText(m.intToStr(m.getIdPhone()));
		txtConnected.setText(m.strConnected(m.isConnected()));
		txtMode.setText(m.getMode());
		txtBlerTarget.setText(m.doubleToStr(m.getBlerTarget()));
		txtSirTarget.setText(m.doubleToStr(m.getSirTarget()));
		txtPowerEmited.setText(m.doubleToStr(m.getpEmited()));
		txtGain.setText(m.doubleToStr(m.getGain()));
		txtSirEstimated.setText(m.doubleToStr(PowerCalculation.sirEstimated(mobileList, nodeB, m)));
	}
	
	//Allow to display informations about the antenna in the bottom right Pane
	public void showAntennaInformation(){
		txtId.setText("Antenna");
		txtConnected.setText("");
		txtMode.setText("");
		txtBlerTarget.setText("");
		txtSirTarget.setText("");
		txtSirEstimated.setText("");
		txtPowerEmited.setText(nodeB.doubleToStr(nodeB.getpEmited()));
		txtGain.setText(nodeB.doubleToStr(nodeB.getGain()));
	}
	
	//Display informations about the application
	public void displayAbout(ActionEvent e){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Informations");
		alert.setHeaderText("RE56 - POWER CONTROL SIMULATION - P2016");
		alert.setContentText("Application develop by: \n\n Nicolas AGUDO-PEREZ GI04 \n ");
		alert.showAndWait();
	}
	
	//Event to move Antenna and Phone
	 EventHandler<MouseEvent> imageViewOnMousePressedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
		        	orgSceneX = t.getSceneX();
		            orgSceneY = t.getSceneY();
		            orgTranslateX = ((ImageView)(t.getSource())).getTranslateX();
		            orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();	
		        }
		    };

		    EventHandler<MouseEvent> imageViewOnMouseDraggedEventHandler =
		        new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent t) {
				        	double offsetX = t.getSceneX() - orgSceneX;
				            double offsetY = t.getSceneY() - orgSceneY;
				            double newTranslateX = orgTranslateX + offsetX;
				            double newTranslateY = orgTranslateY + offsetY;

				            ((ImageView)(t.getSource())).setTranslateX(newTranslateX);
				            ((ImageView)(t.getSource())).setTranslateY(newTranslateY);
		        }
		    };
}