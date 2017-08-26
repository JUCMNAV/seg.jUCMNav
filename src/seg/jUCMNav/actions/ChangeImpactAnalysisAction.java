package seg.jUCMNav.actions;


import grl.IntentionalElementRef;
import grl.LinkRef;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import seg.jUCMNav.JUCMNavPlugin;
import seg.jUCMNav.model.commands.changeImpactAnalysis.ChangeImpactAnalysisCommand;
import ucm.map.EndPoint;
import ucm.map.NodeConnection;
import ucm.map.OrFork;
import ucm.map.PathNode;
import ucm.map.RespRef;
import ucm.map.Timer;
import ucm.map.WaitingPlace;
import urn.URNspec;
import urncore.URNmodelElement;

/**
 * action for GRL chnage impact analysis - Addition/Deletion
 * 
 * @author hasanKaff
 */
 
public class ChangeImpactAnalysisAction  extends URNSelectionAction {

	public static final String ChangeImpactAnalysis = "seg.jUCMNav.ChangeImpactAnalysis";
	public static EditPartViewer Viewr;
	 
	private URNmodelElement element;
	private URNspec urnspec;
	private EObject criterion;
	
	/**
     * @param part
     */
	public ChangeImpactAnalysisAction(IWorkbenchPart part) {
		super(part);
		setId(ChangeImpactAnalysis);
		//setImageDescriptor(JUCMNavPlugin.getImageDescriptor("icons/delete16.gif")); //$NON-NLS-1$
	//icons/StrategyNumImp16.gif
	}

	/**
    * at this level, the value is True, if select one Intentional/Indicator element selected. 
    */
	protected boolean calculateEnabled() {

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
			// create GMDG graph  
				// NodeGMD nodeObj = new NodeGMD(urnspec, element, elementRef);
				 
				 
				 
				 /*links= new ArrayList<grl.ElementLink>(((IntentionalElement)element).getLinksDest());
				 IntentionalElementList = new ArrayList<IntentionalElement>();
				 
				 for(grl.ElementLink l : links){
					 JOptionPane.showMessageDialog(null,"inside for");
					 //JOptionPane.showMessageDialog(null,((IntentionalElement) l.getSrc()).get);
					 IntentionalElementList.add((IntentionalElement) l.getSrc());
					 
					 //JOptionPane.showMessageDialog(null,(IntentionalElement) l.getSrc());
				 }
				 
				 for(IntentionalElement ie : IntentionalElementList){
					 JOptionPane.showMessageDialog(null,ie.getName());
				 }
				 if(IntentionalElementList.size() != 0){
					 elementRef = sel.getIntentionalElementRef();
					 System.out.println("After added all Eelments in list");
				 }
				
				if(elementRef.getPred().size() > 0)
				for(Object li : elementRef.getPred())
				{
					LinkRef noCon = (LinkRef) li;
					if(noCon.getLink() instanceof Contribution)
						JOptionPane.showMessageDialog(null,"its Contribution .. Link Id = " +noCon.getLink().getId() );
					else if(noCon.getLink() instanceof Dependency)
						JOptionPane.showMessageDialog(null,"its Dependency .. Link Id = " +noCon.getLink().getId());

						else if(noCon.getLink() instanceof Decomposition)
						JOptionPane.showMessageDialog(null,"it's Decomposition .. Link Id = " +noCon.getLink().getId());
					
				}
				else
				{
					for(Object li : elementRef.getSucc())
					{
						LinkRef noCon = (LinkRef) li;
						if(noCon.getLink() instanceof Contribution)
							JOptionPane.showMessageDialog(null,"its Contribution .. Link Id = " +noCon.getLink().getId() );
						else if(noCon.getLink() instanceof Dependency)
							JOptionPane.showMessageDialog(null,"its Dependency .. Link Id = " +noCon.getLink().getId());

							else if(noCon.getLink() instanceof Decomposition)
							JOptionPane.showMessageDialog(null,"it's Decomposition .. Link Id = " +noCon.getLink().getId());				
					}
				}
				 //JOptionPane.showMessageDialog(null,"Invoked before executed command");
				
				
				if(elementRef.getSucc().size()>0){
					for (Object li_: elementRef.getSucc()) {
						LinkRef noCon = (LinkRef) li_;
						
						if(noCon.getLink() instanceof Decomposition){
							JOptionPane.showMessageDialog(null,"Decomposition\n"+elementRef.getDef().getName()
									+"\n" + ((IntentionalElementRef)noCon.getTarget()).getDef().getName()
									+"\nNo. of Pred = " +noCon.getTarget().getPred().size() +
									"\nNo. of Succ = " +noCon.getTarget().getSucc().size());
							
							
						}
						else if(noCon.getLink() instanceof Contribution){
							JOptionPane.showMessageDialog(null,"Contribution\n"+elementRef.getDef().getName()
									+"\n" + ((IntentionalElementRef)noCon.getTarget()).getDef().getName()
									+"\nNo. of Pred = " +noCon.getTarget().getPred().size() +
									"\nNo. of Succ = " +noCon.getTarget().getSucc().size());
						}
						else if(noCon.getLink() instanceof Dependency){
							JOptionPane.showMessageDialog(null,"Dependency\n"+elementRef.getDef().getName()
									+"\n" + ((IntentionalElementRef)noCon.getTarget()).getDef().getName()
									+"\nNo. of Pred = " +noCon.getTarget().getPred().size() +
									"\nNo. of Succ = " +noCon.getTarget().getSucc().size());
						}
						else{
							JOptionPane.showMessageDialog(null,"No Relation");
						}
							
					}
				}
				if(elementRef.getPred().size()>0){
					for (Object li_: elementRef.getPred()){
						LinkRef noCon = (LinkRef) li_;
						if(noCon.getLink() instanceof Decomposition){
							JOptionPane.showMessageDialog(null,"Decomposition\n"+elementRef.getDef().getName()
									+"\n" + ((IntentionalElementRef)noCon.getTarget()).getDef().getName()
									+"\nNo. of Pred = " +noCon.getTarget().getPred().size() +
									"\nNo. of Succ = " +noCon.getTarget().getSucc().size());
						}
						else if(noCon.getLink() instanceof Contribution)
							JOptionPane.showMessageDialog(null,"Contribution\n"+elementRef.getDef().getName()
									+"\n" + ((IntentionalElementRef)noCon.getTarget()).getDef().getName()
									+"\nNo. of Pred = " +noCon.getTarget().getPred().size() +
									"\nNo. of Succ = " +noCon.getTarget().getSucc().size());
						else if(noCon.getLink() instanceof Dependency){
							JOptionPane.showMessageDialog(null,"Dependency\n"+elementRef.getDef().getName()
									+"\n" + ((IntentionalElementRef)noCon.getTarget()).getDef().getName()
									+"\nNo. of Pred = " +noCon.getTarget().getPred().size() +
									"\nNo. of Succ = " +noCon.getTarget().getSucc().size());
						}
						else{
							JOptionPane.showMessageDialog(null,"No Relation");
						}
					}
				}*/
			 
			 Command cmd= new ChangeImpactAnalysisCommand(criterion, 2);
			 return cmd;
		 }
		 return null;
	 }
}
