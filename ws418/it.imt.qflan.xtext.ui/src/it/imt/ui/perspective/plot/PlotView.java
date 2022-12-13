package it.imt.ui.perspective.plot;

import org.eclipse.ui.part.ViewPart;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class PlotView extends ViewPart {

	private IXYGraph xyGraph;
	
	
	public PlotView() {
		super();
		//System.out.println("constructor of ploit view");
	}
	
	protected IXYGraph getXYGraph(){
		return xyGraph;
	}
	
	public void addTrace(String traceName,double[] xArray, double[] yArray){
		// create a trace data provider, which will provide the data to the
		// trace.
		CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(false);
		traceDataProvider.setBufferSize(xArray.length);
		//traceDataProvider.setBufferSize(100);
		//traceDataProvider.setUpdateDelay(100);
		
		//traceDataProvider.setCurrentXDataArray(new double[] { 10, 23, 34, 45, 56, 78, 88, 99 });
		traceDataProvider.setCurrentXDataArray(xArray);
		//traceDataProvider.setCurrentYDataArray(new double[] { 11, 44, 55, 45, 88, 98, 52, 23 });
		traceDataProvider.setCurrentYDataArray(yArray);

		// create the trace
		Trace trace = new Trace(traceName, xyGraph.getPrimaryXAxis(), xyGraph.getPrimaryYAxis(), traceDataProvider);
		
		// set trace property
		trace.setPointStyle(PointStyle.POINT);//trace.setPointStyle(PointStyle.XCROSS);
		trace.setPointSize(1);
		trace.setTraceType(TraceType.SOLID_LINE);
		trace.setLineWidth(1);
		//trace.setErrorBarEnabled(false);

		// add the trace to xyGraph
		xyGraph.addTrace(trace);
		
		//In case we want to hide species with no dynamics associated...
		//trace.setVisible(visible);
	}
	
	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);

	}
	
	
	@Override
	public void createPartControl(Composite parent) {
		
		 //use LightweightSystem to create the bridge between SWT and draw2D
		final LightweightSystem lws = new LightweightSystem(new Canvas(parent, SWT.NONE));

		// create a new XY Graph.
		xyGraph = new XYGraph();
		
		// set it as the content of LightwightSystem
		//lws.setContents(xyGraph);
		
		//Configure xy graph
		//xyGraph.setTitle("Simple Example");
		
		// create a new tool bar, and add the XY graph to it.
		ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(xyGraph);
		
		// set the toolbar as the content of LightwightSystem
		lws.setContents(toolbarArmedXYGraph);

		/*// create a trace data provider, which will provide the data to the
		// trace.
		CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(false);
		traceDataProvider.setBufferSize(100);
		traceDataProvider.setCurrentXDataArray(new double[] { 10, 23, 34, 45, 56, 78, 88, 99 });
		traceDataProvider.setCurrentYDataArray(new double[] { 11, 44, 55, 45, 88, 98, 52, 23 });

		// create the trace
		Trace trace = new Trace("Trace1-XY Plot", xyGraph.primaryXAxis, xyGraph.primaryYAxis, traceDataProvider);

		// set trace property
		trace.setPointStyle(PointStyle.XCROSS);

		// add the trace to xyGraph
		xyGraph.addTrace(trace);*/
	}

	@Override
	public void setFocus() {
		//label.setFocus();
		//Display display = Display.getDefault();
	}

}
