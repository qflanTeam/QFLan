package it.imt.ui.perspective.plot;

import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsole;

import it.imt.qflan.simulation.output.DataOutputHandlerAbstract;

public class GUIDataOutputHandler extends DataOutputHandlerAbstract {

	//private PlotView plotView;
	private MessageConsole console;
	private int counter=0;
	
	public GUIDataOutputHandler(MessageConsole console) {
		this.console=console;
	}

	@Override
	public void showPlots(boolean drawImages, String imagesName) {
		//devo solo fargli disegnare i plot che ho memorizzato nei field di DataOutputHandlerAbstract
		/*double[] xArray = new double[] { 10, 23, 34, 45, 56, 78, 88, 99 };
		double[] yArray = new double[] { 11, 44, 55, 45, 88, 98, 52, 23 };
		
		double[] xArray2 = new double[] { 10, 23, 34, 45, 56, 78, 88, 99 };
		double[] yArray2 = new double[] { 11+10, 44+10, 55+10, 45+10, 88+10, 98+10, 52+10, 23+10 };
		
		double[] xArray3 = new double[] { 10, 23, 34, 45, 56, 78, 88, 99 };
		double[] yArray3 = new double[] { 11+20, 44+20, 55+20, 45+20, 88+20, 98+20, 52+20, 23+20 };*/
		
		
		counter++;
		
		
				
		//Display.getDefault().asyncexec(
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
								
				/*PlotView plotView=null;
				try {
					plotView = (PlotView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.imt.erode.crn.ui.perspective.PlotView",console.getName(),org.eclipse.ui.IWorkbenchPage.VIEW_ACTIVATE);
				} catch (PartInitException e2) {
					e2.printStackTrace();
				}
								
				plotView.setPartName(console.getName());
				plotView.addTrace("My first trace", xArray, yArray);
				plotView.addTrace("My second trace", xArray2, yArray2);
				plotView.addTrace("My third trace", xArray3, yArray3);
				
				XYGraph xyg = plotView.getXYGraph();
				xyg.setShowLegend(showLabels);
				xyg.setTitle(minimalDescription+messageSuffix);
				xyg.getXAxisList().get(0).setTitle(xLabel);
				xyg.getYAxisList().get(0).setTitle(yLabel);*/
				
				
				//create plot for means and SMC variances
				PlotView plotViewMeans=null;
				PlotView plotViewSMCVariances=null;
				try {
					String consoleName = "console"+System.currentTimeMillis();//console.getName();
					if(console!=null){
						consoleName=console.getName();
					}
					if(hasSMCVariances){
						plotViewSMCVariances = (PlotView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.imt.qflan.ui.perspective.plot.PlotView",consoleName+"SMCVar"+counter,org.eclipse.ui.IWorkbenchPage.VIEW_ACTIVATE);
						plotViewSMCVariances.setPartName(consoleName);//plotViewSMCVariances.setPartName(console.getName()+"SMCVar"+counter);
						IXYGraph xyg = plotViewSMCVariances.getXYGraph();
						//xyg.setTitle(command+"\n"+minimalDescription+". Variances estimations.");
						setTitleAndLabelsAndOtherProperties(xyg, minimalDescription+" - Variances estimations.","Variances estimations");
					}
					plotViewMeans = (PlotView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.imt.qflan.ui.perspective.plot.PlotView",consoleName+counter,org.eclipse.ui.IWorkbenchPage.VIEW_ACTIVATE);
					//plotViewMeans.setPartProperty(key, value);
					plotViewMeans.setPartName(consoleName);//setPartName(plotViewMeans.setPartName(console.getName()+counter));
					IXYGraph xyg = plotViewMeans.getXYGraph();
					//xyg.setTitle(command+"\n"+minimalDescription+messageSuffix);
					setTitleAndLabelsAndOtherProperties(xyg, minimalDescription+messageSuffix,yLabel);
				} catch (PartInitException e2) {
					e2.printStackTrace();
				}
				
				
				for(int species=0;species<labelsAll.length;species++){
					// add a line 
					plotViewMeans.addTrace(labelsAll[species],x,plotsAll[species]);
					if(hasSMCVariances){
						//Trace trace = new Trace(name, xAxis, yAxis, dataProvider)
						plotViewSMCVariances.addTrace(labelsAll[species],x,smcVariances[species]);
					}
				}
				
				plotViewMeans.getXYGraph().performAutoScale();
				if(hasSMCVariances){
					plotViewSMCVariances.getXYGraph().performAutoScale();
				}
			}

			private void setTitleAndLabelsAndOtherProperties(IXYGraph xyg, String title, String yLabelToUse) {
				//command=command.replace("=>", "=").replace("({", "(").replace("})",")");
				int spaces = command.length() - title.length();
				String s = "";
				if(spaces>=0){
					for(int i=0;i<spaces;i++){//spaces/2
						s+=" ";
					}
				}
				title = command +"\n"+s+title;
				//title = title + "\n["+ command +"]";
				xyg.setTitle(title);
				xyg.getPrimaryXAxis().setShowMajorGrid(true);
				xyg.getPrimaryYAxis().setShowMajorGrid(true);
				
				xyg.setShowLegend(showLabels);
				xyg.getXAxisList().get(0).setTitle(xLabel);
				xyg.getYAxisList().get(0).setTitle(yLabelToUse);
				
				xyg.setFont(XYGraphMediaFactory.getInstance().getFont(XYGraphMediaFactory.FONT_TAHOMA));
				/*xyg.primaryXAxis.setDateEnabled(true);
				xyg.primaryYAxis.setAutoScale(true);
				xyg.primaryXAxis.setAutoScale(true);
				xyg.primaryXAxis.setShowMajorGrid(true);
				xyg.primaryYAxis.setShowMajorGrid(true);
				xyg.primaryXAxis.setAutoScaleThreshold(0);
				
				xyg.setFocusTraversable(true);
				xyg.setRequestFocusEnabled(true);*/
			}
		});
	}

}
