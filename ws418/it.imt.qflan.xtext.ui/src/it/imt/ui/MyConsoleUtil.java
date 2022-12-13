package it.imt.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * 
 * @author Andrea Vandin
 *
 */
public class MyConsoleUtil {
	
	private static final String CONSOLE_NAME = "QFLaner ";
	//public static final MessageConsoleStream out=setOut();
	//private static PrintStream out;	
	//private static MessageConsole console;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH-mm-ss-SSS");

	private static String greeting(String message, String asterisks){
		//System.out.println("ciao");
		return		 	"**" + asterisks + "**\n" +
				  		" *" + asterisks + "* \n" +
				  		"  " +  message  + "  \n" +
				  		" *" + asterisks + "* \n" +
				  		"**" + asterisks + "**";
		
	}
	
	public static String computeWelcome(MessageConsoleStream out){
		/*Date date = new Date();
		String dateString=DATE_FORMAT.format(date);
		String welcome = "********************************** Welcome to QFLaner [" + dateString + "] **********************************";*/
		//String welcome = "************************************ Welcome to "+out.getConsole().getName()+" **********************************";
		String welcome = "************************************ "+out.getConsole().getName()+" **********************************";
		int length = welcome.length();
		StringBuilder asterisks=new StringBuilder();
		for(int i=0;i<length;i++){
			asterisks.append('*');
		}
		welcome = greeting(welcome, asterisks.toString());
		return "\n\n"+welcome;
	}
	
	public static String computeGoodbye(MessageConsoleStream out){
		Date date = new Date();
		String dateString=DATE_FORMAT.format(date);
		/*String goodbye = "********************************** Goodbye from "+out.getConsole().getName()+" **********************************\n";
		int length = goodbye.length();
		goodbye = goodbye + "  ********************************** Completed at time  ["+dateString+"] **********************************";*/
		String goodbye = "******************************** Completed at ["+dateString+"] *******************************";
		int length = goodbye.length();
		
		StringBuilder asterisks=new StringBuilder();
		for(int i=0;i<length;i++){
			asterisks.append('*');
		}
		goodbye = greeting(goodbye, asterisks.toString());
		return "\n"+goodbye+"\n";
	}

	/*private static MessageConsoleStream setOut(){
		//Find console
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		boolean found=false;
		for (int i = 0; i < existing.length; i++){
			if (CONSOLE_NAME.equals(existing[i].getName())){
				console = (MessageConsole) existing[i];
				found=true;
			}
		}
		if(!found){
			//no console found, so create a new one
			console = new MessageConsole(CONSOLE_NAME, null);
			conMan.addConsoles(new IConsole[]{console});
		}

		//Set console stream of core tool
		MessageConsoleStream o = console.newMessageStream();
		CRNReducerCommandLine.setOutStream(o);
		return o;
	}*/

	public static final String CONSOLE_CHARSET = "utf8"; 
	
	/*public static IOConsole generateConsole() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		Date date = new Date();
		String dateString=DATE_FORMAT.format(date);
		IOConsole newConsole = new IOConsole(CONSOLE_NAME+"["+dateString+"]", null);
		conMan.addConsoles(new IConsole[]{newConsole});
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.console.ConsoleView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try
		{
			PrintWriter consoleWriter = new PrintWriter(new OutputStreamWriter(newConsole.newOutputStream(), CONSOLE_CHARSET), true);
			BufferedReader consoleReader = new BufferedReader(new InputStreamReader(newConsole.getInputStream(), CONSOLE_CHARSET));
			
			try {
				String piero = consoleReader.readLine();
				System.out.println(piero);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
		
		//To display a specific console 
		//((IConsoleView) consoleView).display(newConsole);
		return newConsole;
	}*/
	
	public static MessageConsole generateConsole() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		Date date = new Date();
		String dateString=DATE_FORMAT.format(date);
		MessageConsole newConsole = new MessageConsole(CONSOLE_NAME+"["+dateString+"]", null);
		conMan.addConsoles(new IConsole[]{newConsole});
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.console.ConsoleView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//To display a specific console 
		//((IConsoleView) consoleView).display(newConsole);
		return newConsole;
	}
	
}
