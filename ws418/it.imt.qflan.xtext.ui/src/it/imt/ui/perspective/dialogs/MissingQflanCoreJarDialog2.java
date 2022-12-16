package it.imt.ui.perspective.dialogs;

import org.eclipse.swt.widgets.Label;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class MissingQflanCoreJarDialog2 extends Dialog {

	private String path;
	private String pathShort;
	//private ArrayList<String> paths;
	//private String OS;

	private String jarFileLocation;
	//private org.eclipse.swt.widgets.Text text; 
	
	public MissingQflanCoreJarDialog2(Shell parentShell, String link, String linkShort/*, ArrayList<String> paths, String OS*/) {
		super(parentShell);
		this.path=link;
		this.pathShort=linkShort;
		//this.paths=paths;
		//this.OS=OS;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		/*createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);*/
	}

	public String getJarFileLocation(){
		return jarFileLocation;
	}
	
	private void setJarFileLocation(String jarFileLocation){
		this.jarFileLocation=jarFileLocation;
		if(jarFileLocation!=null && !jarFileLocation.equals("")){
			//getButton(SWT.OK).setEnabled(true);
		}
		//text.setText(jarFileLocation);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		//getButton(SWT.OK).setEnabled(false);
		Label messageLabel= new Label(container, SWT.WRAP);
		//messageLabel.setFont(parent.getFont());
		//messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		messageLabel.setText("Please download the following archive:");
		//msg = "Please download the archive \n   "+link32 +" or \n   "+link64+"\n"+"and add its files to one of the following locations:";


		Link link = new Link(container, SWT.NONE);
		link.setText(createLink(path,pathShort));
		link.setToolTipText("QFlan core library");

		link.addSelectionListener(new MyWebSelectionListener());

		Label messageLabel2= new Label(container, SWT.WRAP);
		//messageLabel.setFont(parent.getFont());
		//messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		messageLabel2.setText("Locate the downloaded jar file:");
        
		/*text = new Text(container, SWT.WRAP|SWT.LEFT);
        text.setSize(text.getSize().x*4, text.getSize().y);*/
        
        //Copia come fa l'import wizard
        
        org.eclipse.swt.widgets.Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new MyLocalSelectionListener());

		/*for (String path : paths) {
			//Label pathLabel = new Label(container, SWT.WRAP);pathLabel.setText("\t"+path);
			Link pathLink = new Link(container, SWT.NONE);
			//pathLink.setText(createLink("file:///"+path));
			pathLink.setText(createLink(path));
			if(OS.contains("windows")||OS.contains("Windows")||OS.contains("Mac")){
				pathLink.addSelectionListener(new MyLocalSelectionListener());
				File f=new File(path);
				if(!f.isDirectory()){
					pathLink.setEnabled(false);
				}
			}
		}*/
		return container;
	}

	/*private String createLink(String path) {
		return createLink(path,path);
	}*/
	
	private String createLink(String path,String pathToShow) {
		return "\t<a href=\""+path+"\">"+pathToShow+"</a>";
	}


	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("QFLan core library missing.");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	private final class MyWebSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			//System.out.println("You have selected: "+e.text);
			try {
				//  Open default external browser 
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
			} 
			catch (PartInitException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} 
			catch (MalformedURLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}

	private final class MyLocalSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(getShell());//display.getActiveShell()
			//dialog.setFilterExtensions(new String[]{"jar"});
			dialog.setOverwrite(false);
			String absFileName = dialog.open();
	        if(absFileName!=null){
	        	//jarFileLocation=absFileName;
	        	setJarFileLocation(absFileName);
	        }
			/*
			//System.out.println("You have selected: "+e.text);
			File file = new File(e.text+"/");
			boolean fileisDir = file.isDirectory();
			boolean exists=file.exists();
			System.out.println(e.text + " is directory: "+fileisDir);
			System.out.println(e.text + " exists: "+exists);
			if(file.isDirectory()){
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				} catch(java.lang.IllegalArgumentException e2){
					//TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			*/
		}
	}

} 
