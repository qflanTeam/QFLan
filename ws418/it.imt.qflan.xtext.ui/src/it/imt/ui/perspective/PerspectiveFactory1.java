package it.imt.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory1 implements IPerspectiveFactory {
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.TOP, 0.5f, layout.getEditorArea());
		//IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.BOTTOM, 0.5f, layout.getEditorArea());
		//IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.LEFT, 0.5f, layout.getEditorArea());
		//IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.BOTTOM, 0.5f, "org.eclipse.ui.console.ConsoleView");
		//IFolderLayout folder1 = layout.createFolder("folder1", IPageLayout.RIGHT, 0.5f, "org.eclipse.ui.console.ConsoleView");
		folder1.addPlaceholder("it.imt.ui.perspective.plot.PlotView:viewSecondaryID*");
		
		//plotView = (PlotView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.imt.erode.crn.ui.perspective.PlotView",console.getName(),org.eclipse.ui.IWorkbenchPage.VIEW_ACTIVATE);
		/*IViewReference[] openViews = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (IViewReference viewRef : openViews) {
			IViewPart view = viewRef.getView(false);
			if(view instanceof PlotView){
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(view);
			}
		}*/

	}

}
