package it.imt.qflan.simulation.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.eclipse.ui.console.MessageConsoleStream;

import vesta.mc.InfoMultiQuery;

public abstract class DataOutputHandlerAbstract implements IDataOutputHandler {

	protected double[] x;
	//A matrix speciesName * speciesPlot (e.g., plots[0] is the plot of the first species)
	protected double[][] plotsAll;
	protected String[] labelsAll;
	protected double[][] plotsViews;
	protected String[] labelsViews;
	protected String[] viewsExpressions;
	protected boolean hasViews;
	protected boolean covariances=false;
	protected boolean hasSMCVariances=false;
	protected double[][] smcVariances;

	public static final String allGifName="images/species.gif";
	public static final String viewsGifName="images/views.gif";
	public  ImageIcon iconAll;
	public ImageIcon iconViews;
	public String mainTabLabel;//="Concentrations of the species";
	
	protected String messageSuffix=" - All species/variables";
	protected String xLabel="Time";
	protected String yLabel="Species/variable concentrations";
	
	protected String minimalDescription;
	protected boolean showLabels=true;//private static final boolean showLabels=true;
	protected String command;
	
	private static final String csvSeparator=", ";
	
	@Override
	public void setShowLabels(boolean showLabels){
		this.showLabels=showLabels;
	}

	@Override
	public void writeCSV(String csvFile,MessageConsoleStream out) {

		try {
			FileOutputStream fos = new FileOutputStream(csvFile+".cdat");
			//CRNReducerCommandLine.print(out,"Writing the csvFile "+csvFile+".cdat ... ");

			PrintStream Output = new PrintStream(fos);
			StringBuilder caption = new StringBuilder();
			caption.append("# time"+csvSeparator);
			for(String speciesName : this.labelsAll){
				caption.append(speciesName + csvSeparator);
			}

			//covariances
			/*boolean printAllCovariances=false;
			for(int covariance=labelsAll.length;covariance<plotsAll.length;covariance++){
				int cov=(covariance-labelsAll.length);//this is the cov-th covariance. I.e., the C_{species1,species2}
				int species1 = cov / labelsAll.length;
				int species2 = cov % labelsAll.length;
				if(species1==species2){//with this if I only print the variances
					caption.append("V_"+ labelsAll[species1] + csvSeparator);
				}
				else{
					if(printAllCovariances){
						caption.append("C_"+ labelsAll[species1]+"-"+labelsAll[species2] + csvSeparator);
					}
				}
			}*/

			Output.println(caption.toString());

			for(int step=0;step<x.length;step++){
				StringBuilder concentrationsAtStep = new StringBuilder();
				concentrationsAtStep.append(x[step]+ csvSeparator);
				for(int species=0;species<labelsAll.length-1;species++){
					concentrationsAtStep.append(plotsAll[species][step]+ csvSeparator);
				}

				/*//covariances
				for(int covariance=labelsAll.length;covariance<plotsAll.length;covariance++){
					int cov=(covariance-labelsAll.length);//this is the cov-th covariance. I.e., the C_{species1,species2}
					int species1 = cov / labelsAll.length;
					int species2 = cov % labelsAll.length;
					if(species1==species2){//with this if I only print the variances
						concentrationsAtStep.append(plotsAll[covariance][step]+ csvSeparator);
					}
					else{
						if(printAllCovariances){
							concentrationsAtStep.append(plotsAll[covariance][step]+ csvSeparator);
						}
					}
				}*/


				concentrationsAtStep.append(plotsAll[labelsAll.length-1][step]);
				Output.println(concentrationsAtStep.toString());
			}
			Output.close();
			//CRNReducerCommandLine.println(out,"completed.");
		} catch (FileNotFoundException e) {
			//CRNReducerCommandLine.println(out,"Could not create the file "+csvFile+".cdat");
		}
		if(hasViews){
			try {
				FileOutputStream fos = new FileOutputStream(csvFile+".vdat");
				//CRNReducerCommandLine.print(out,"Writing the csvFile "+csvFile+".vdat ... ");

				PrintStream Output = new PrintStream(fos);
				StringBuilder caption = new StringBuilder();
				caption.append("# time"+csvSeparator);
				for(String viewsName : this.labelsViews){
					caption.append(viewsName + csvSeparator);
				}
				Output.println(caption.toString());

				for(int step=0;step<x.length;step++){
					StringBuilder concentrationsOfViewsAtStep = new StringBuilder();
					concentrationsOfViewsAtStep.append(x[step]+ csvSeparator);
					for(int view=0;view<labelsViews.length-1;view++){
						concentrationsOfViewsAtStep.append(plotsViews[view][step]+ csvSeparator);
					}
					concentrationsOfViewsAtStep.append(plotsViews[labelsViews.length-1][step]);
					Output.println(concentrationsOfViewsAtStep.toString());
				}
				Output.close();
				//CRNReducerCommandLine.println(out,"completed.");
			} catch (FileNotFoundException e) {
				//CRNReducerCommandLine.println(out,"Could not create the file "+csvFile+".vdat");
			}
		}
		

	}
	
	private void setData(String minimalDescription, String mainTabLabel, String command){
		this.mainTabLabel=mainTabLabel;
		this.command=command;
		//iconAll=createImageIcon(allGifName);
		//iconViews=createImageIcon(viewsGifName);
		//this.labelsAll= crn.createArrayOfAllSpeciesNames();
		//this.labelsViews=crn.getViewNames();
		//this.viewsExpressions=crn.getViewExpressionsSupportedByMathEval();
		
		this.minimalDescription=minimalDescription;
	}

	@Override
	/**
	 * Invoked after solving the statistical analysis with MultiVeStA 
	 */ 
	public void setData(String minimalDescription, InfoMultiQuery infoMultiQuery,double alpha, double delta, String command){
		setData(minimalDescription, infoMultiQuery, String.valueOf(alpha),String.valueOf(delta),command);
	}
	
	@Override
	/**
	 * Invoked after solving the statistical analysis with MultiVeStA 
	 */ 
	public void setData(String minimalDescription, InfoMultiQuery infoMultiQuery,String alpha, String delta, String command){
		setData(minimalDescription,"Means estimations",command);
		labelsAll= null;
		labelsViews=null;
		viewsExpressions=null;
		covariances=false;
		hasViews= false;
		hasSMCVariances=true;
		
		//messageSuffix= ". Means estimations. CI=("+alpha+","+delta+")";
		messageSuffix= ". CI=("+alpha+","+delta+")";
		xLabel=infoMultiQuery.getxVariableName();
		yLabel="Means estimations";
		
		//Init x
		ArrayList<Double> xList = infoMultiQuery.getX();
		this.x=new double[xList.size()];
		for(int i = 0; i < xList.size();i++){
			x[i]=xList.get(i);
		}
		
		//init data
		labelsAll = new String[infoMultiQuery.getNumberOfYsForEachX()];
		plotsAll= new double[labelsAll.length][];
		smcVariances = new double[labelsAll.length][];
		for(int query=0;query<labelsAll.length;query++){
			labelsAll[query]=infoMultiQuery.getLabel(query);
			plotsAll[query]=new double[x.length];
			smcVariances[query]=new double[x.length];
			for(int i = 0; i < x.length;i++){
				ArrayList<Double> yi = infoMultiQuery.getY(i);
				ArrayList<Double> yVari = infoMultiQuery.getYVar(i);
				plotsAll[query][i]=yi.get(query);
				smcVariances[query][i]=yVari.get(query);
			}
		}
		
	}

}
