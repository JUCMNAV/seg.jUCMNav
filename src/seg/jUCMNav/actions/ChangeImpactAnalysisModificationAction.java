package seg.jUCMNav.actions;

import javax.swing.JOptionPane;

import grl.IntentionalElementRef;
import grl.LinkRef;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import seg.jUCMNav.JUCMNavPlugin;
import seg.jUCMNav.model.commands.changeImpactAnalysis.ChangeImpactAnalysisCommand;
import urn.URNspec;
import urncore.URNmodelElement;

/**
 * action for GRL chnage impact analysis - Modification
 * 
 * @author hasanKaff
 */
 
public class ChangeImpactAnalysisModificationAction  extends URNSelectionAction  {
	public static final String ChangeImpactAnalysisModification = "seg.jUCMNav.ChangeImpactAnalysisModification";
	public static EditPartViewer Viewr;
	 
	private URNmodelElement element;
	private URNspec urnspec;
	private EObject criterion;
	
	/**
     * @param part
     */
	public ChangeImpactAnalysisModificationAction(IWorkbenchPart part) {
		super(part);
		setId(ChangeImpactAnalysisModification);
		//setImageDescriptor(JUCMNavPlugin.getImageDescriptor("icons/StrategyNumImp16.gif")); //$NON-NLS-1$
	
	}
	
	/**
	    * at this level, the value is True, if select one Intentional/Indicator element selected. 
	    */
		protected boolean calculateEnabled(){
			EditPart ePart;
			if(getSelectedObjects().size() != 1)
				return false;
	        SelectionHelper sel = new SelectionHelper(getSelectedObjects());
	        if(sel.getSelectionType() == SelectionHelper.INTENTIONALELEMENTREF || sel.getSelectionType() == SelectionHelper.LINKREF){
	        	ePart= ((EditPart)getSelectedObjects().get(0));
				EObject obj = (EObject) ePart.getModel();
				
				if(obj instanceof IntentionalElementRef){
					//intElement = (IntentionalElementRef) element;
					criterion = obj;				
					return true;
				}else if(obj instanceof LinkRef){
					criterion = obj;
					return true;
				}
				else return false;
				
	        }
	        
			return false;
	    }
		
		/**
	     * @return a {@link ChangeImpactAnalysisCommand}
	     */
		
		 protected Command getCommand() {
			 if(calculateEnabled()){
				//JOptionPane.showMessageDialog(null,ie.getName());					
				 
				 Command cmd= new ChangeImpactAnalysisCommand(criterion, 1);
				 return cmd;
			 }
			 return null;
		 }
}
