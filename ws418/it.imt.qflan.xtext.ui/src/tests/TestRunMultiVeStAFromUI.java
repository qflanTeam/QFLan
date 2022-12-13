package tests;

import it.imt.qflan.core.multivesta.EntryPointMultiVeStAAndQFLanJava;

//import it.imt.qflan.core.multivesta.EntryPointMultiVeStAAndQFLanJava;

public class TestRunMultiVeStAFromUI {

	public static void main(String[] args) {
		EntryPointMultiVeStAAndQFLanJava.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "1", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});
		/*EntryPointMultiVeStAAndQFLanGUI.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "oneLocal", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});*/
		/*EntryPointMultiVeStAAndQFLanJava.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "localhost:54340", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});*/
	}
}
