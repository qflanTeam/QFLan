package tests;

import it.imt.qflan.core.multivesta.EntryPointMultiVeStAAndQFLanJava;

public class TestRunMultiVeStAServerAndClientFromUI {

	public static void main(String[] args) {
		/*EntryPointMultiVeStAAndQFLanJava.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "1", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});*/
		/*EntryPointMultiVeStAAndQFLanJava.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "oneLocal", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});*/
		Thread t = new Thread(
		new Runnable() {
			
			@Override
			public void run() {
				EntryPointMultiVeStAAndQFLanJava.main(new String[]{"54341"});
			}
		});
		t.start();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EntryPointMultiVeStAAndQFLanJava.main(new String[]{
    			"-c", "-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
    			"-m", "BikesIDE.java", "-l", "localhost:54341", "-ir", "20", 
    			"-f", "MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex", 
    			"-bs", "20", "-a", "0.1", "-ds", "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]", "-osws", "ONESTEP", "-sots", "12343"
    	});
	}
}
