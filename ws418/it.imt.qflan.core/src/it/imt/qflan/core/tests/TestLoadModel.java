package it.imt.qflan.core.tests;

import it.imt.qflan.core.multivesta.QFlanJavaState;
import vesta.mc.ParametersForState;

public class TestLoadModel {

	public static void main(String[] args) {
		//QFlanJavaState s = new QFlanJavaState(new ParametersForState("/Users/andrea/Dropbox/runtime-EclipseApplication/qflan2/bikes.qflan", ""));
		//QFlanJavaState s = new QFlanJavaState(new ParametersForState("/Users/andrea/Dropbox/runtime-EclipseApplication/qflan2/BikesIDE2.qflan", ""));
		//QFlanJavaState s = new QFlanJavaState(new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/pino/BikesIDE3.qflan", ""));
		QFlanJavaState s = new QFlanJavaState(new ParametersForState("/Users/andrea/Dropbox/runtime-QFLan.product/TSE/ParametricElevator44_8_4.qflan", ""));
		s.performOneStepOfSimulation();
	}

}
