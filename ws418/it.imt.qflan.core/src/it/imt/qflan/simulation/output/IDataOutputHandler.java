package it.imt.qflan.simulation.output;

import org.eclipse.ui.console.MessageConsoleStream;

import vesta.mc.InfoMultiQuery;

public interface IDataOutputHandler {

	void setShowLabels(boolean showLabels);

	void showPlots(boolean drawImages, String imagesName);

	void writeCSV(String csvFile, MessageConsoleStream out);

	void setData(String minimalDescription, InfoMultiQuery infoMultiQuery,double alpha, double delta, String command);

	void setData(String minimalDescription, InfoMultiQuery infoMultiQuery, String alpha, String delta, String command);

}
