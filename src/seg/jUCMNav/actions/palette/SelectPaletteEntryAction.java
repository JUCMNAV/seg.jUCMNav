package seg.jUCMNav.actions.palette;

import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

import seg.jUCMNav.editors.UCMNavMultiPageEditor;
import seg.jUCMNav.editors.palette.UcmPaletteRoot;

public class SelectPaletteEntryAction extends Action
{
	public static final String SELECT_PALETTE = "seg.jUCMNav.select.palette";
	private IWorkbenchPart part;

	public static String getId(char letter)
	{
		return SELECT_PALETTE + "." + letter;
	}
	
	public SelectPaletteEntryAction(IWorkbenchPart part, char letter)
	{
		this.part = part;
		String txt = "Select Palette Action";
		setText(txt);
		setToolTipText(txt);
		setId(getId(letter));
	}

	/**
	 * Selects all edit parts in the active workbench part.
	 */
	public void run()
	{
		if (part instanceof UCMNavMultiPageEditor)
		{
			UCMNavMultiPageEditor nav = (UCMNavMultiPageEditor) part;

			if (nav.getCurrentPage()!=null)
			{
				PaletteViewer viewer = nav.getCurrentPage().getEditDomain().getPaletteViewer();
				if (viewer!=null) {
					PaletteRoot root =  viewer.getPaletteRoot();
					if (root!=null && root instanceof UcmPaletteRoot )
					{
						viewer.setActiveTool(((UcmPaletteRoot)root).getAssociatedTool(getId().substring(getId().length()-1)));
					}
				}
			}
		}
	}
}
