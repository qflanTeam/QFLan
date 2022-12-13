package it.imt.ui.perspective;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class StartupHook  implements IStartup {

	@Override
	public void earlyStartup() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
						public void run() {
							try {

								IWorkbench workbench = PlatformUI.getWorkbench();
								workbench.showPerspective("QFLANPerspective.perspective1",
										workbench.getActiveWorkbenchWindow());
							} catch (WorkbenchException e) {
								e.printStackTrace();
							}
						}
					});
		/*try {
			SwingUtilities.invokeAndWait(
			//SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							try {

								IWorkbench workbench = PlatformUI.getWorkbench();
								workbench.showPerspective("ErodePerspective.perspective1",
										workbench.getActiveWorkbenchWindow());
							} catch (WorkbenchException e) {
								e.printStackTrace();
							}
						}
					});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
}
