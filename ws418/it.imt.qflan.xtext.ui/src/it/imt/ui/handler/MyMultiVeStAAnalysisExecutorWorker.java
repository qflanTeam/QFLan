package it.imt.ui.handler;

import java.io.IOException;

//import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.console.MessageConsoleStream;

import it.imt.ui.perspective.plot.GUIDataOutputHandler;
import it.imt.qflan.core.model.QFlanModel;
import it.imt.ui.MyConsoleUtil;
import vesta.NewVesta;
import vesta.mc.InfoMultiQuery;

public class MyMultiVeStAAnalysisExecutorWorker extends Thread {

	MessageConsoleStream out;
	private IProject project;
	private String modelFullPath;
	private String jarPath;
	private String projectPath;
	private GUIDataOutputHandler guidog;
	private String query;
	private String logFile;
	private String alpha;
	private String delta;
	int parallelism;
	int ir;
	int blockSize;
	private int seed;
	private MultiVeStAOrSimulation whattodo;
	private QFlanModel model;
	private String fileOut;
	private int steps;
	
	public MyMultiVeStAAnalysisExecutorWorker(QFlanModel model, String query,String alpha,String delta,int parallelism,int ir,GUIDataOutputHandler guidog, MessageConsoleStream out, IProject project, String modelFullPath, String jarPath,String projectPath, int blockSize, String logFile) {
		this.model=model;
		this.guidog=guidog;
		this.out=out;
		this.project=project;
		this.modelFullPath=modelFullPath;
		this.jarPath=jarPath;
		this.projectPath=projectPath;
		this.query=query;
		this.alpha=alpha;
		this.delta=delta;
		this.parallelism=parallelism;
		this.ir=ir;
		this.blockSize=blockSize;
		this.whattodo=MultiVeStAOrSimulation.MULTIVESTA;
		this.logFile=logFile;
	}
	
	public MyMultiVeStAAnalysisExecutorWorker(int steps,String fileOut,QFlanModel model, int seed,GUIDataOutputHandler guidog, MessageConsoleStream out, IProject project, String modelFullPath, String jarPath, String projectPath) {
		this.steps=steps;
		this.fileOut=fileOut;
		this.model=model;
		this.guidog=guidog;
		this.out=out;
		this.project=project;
		this.modelFullPath=modelFullPath;
		this.jarPath=jarPath;
		this.projectPath=projectPath;
		this.seed=seed;
		this.whattodo=MultiVeStAOrSimulation.SIMULATE;
	}

	@Override
	public void run() {
		/*String query = "/MultiQuaTEx/FeaturesInstalledAndPriceWeightLoadAtStep.quatex";
		String alpha = "0.1";
		String delta = "[20.0,1.0,5.0,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1]";
		InfoMultiQuery result = vesta.NewVesta.invokeClient(new String[]{
				"-sm", "false", "-sd", "it.imt.qflan.core.multivesta.QFlanJavaState", 
				"-m", modelFullPath, "-l", "1", "-ir", "20", 
				"-f", query,
				"-op" , projectPath,
				"-vp", "false",
				"-jn", jarPath, //ImportBNGWizard.getPossibleJarPaths(),
				"-bs", "20", "-a", String.valueOf(alpha), "-ds", delta, "-osws", "ONESTEP", "-sots", "12343"
		},out);*/
		
		
		/*int parallelism = 1;
		int ir = 20;
		String outputPath = projectPath;*/

		final int REPEAT = 1;
		for(int i=0;i<REPEAT;i++){
			if(whattodo.equals(MultiVeStAOrSimulation.MULTIVESTA)){
				InfoMultiQuery result = MyMultiVeStAAnalysisExecutor.invokeMultiVeStA(modelFullPath, query, alpha, delta, parallelism,ir,jarPath,projectPath,blockSize,out,model,logFile);
				if(result.isParametric()){
					MyMultiVeStAAnalysisExecutor.visualizePlot(guidog,result, modelFullPath, query, alpha, delta,out);
				}
			}
			else{
				try {
					MyMultiVeStAAnalysisExecutor.performASimulation(steps,fileOut,model,modelFullPath,seed,out,jarPath,projectPath);
				} catch (IOException e) {
					NewVesta.printStackTrace(out, e);
				}
			}
		}
		
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(MyConsoleUtil.computeGoodbye(out));
	}

	
}