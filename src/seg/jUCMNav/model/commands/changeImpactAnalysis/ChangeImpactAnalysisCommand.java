package seg.jUCMNav.model.commands.changeImpactAnalysis;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import grl.EvaluationStrategy;
import grl.GRLGraph;
import grl.IntentionalElement;
import grl.IntentionalElementRef;
import grl.LinkRef;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import seg.jUCMNav.editors.UCMNavMultiPageEditor;
import seg.jUCMNav.editors.UrnEditor;
import seg.jUCMNav.model.commands.JUCMNavCommand;
import seg.jUCMNav.model.commands.create.AddCommentCommand;
import seg.jUCMNav.model.commands.transformations.ChangeColorCommand;
import urn.URNspec;
import urncore.Comment;
import urncore.IURNDiagram;
import urncore.URNdefinition;
import urncore.UrncoreFactory;

public class ChangeImpactAnalysisCommand extends Command implements JUCMNavCommand{
	private URNspec urnspec;   
    private IntentionalElementRef intRefCrietrion;
    private LinkRef linkRefCrietrion;
    private GRLGraph grlGraph;
    private EObject crietrion;
    private EObject criCIA;
    private GMDGraph gmdGraph;
    private CIADelalgo ciaAlgo;
    private CIAModificationAlgo ciaModifiAlgo;
    ArrayList<NodeGMD> finalGMDGraph;
    private int changeType; // 1: modification  2: Addition  3: Deletion
   
    static UCMNavMultiPageEditor editor; // color method
    
    List<EvaluationStrategy> strategy; //strategy
    List<Object> evals;
    List<IntentionalElement> nodesInStrategy;
    
    // remove comment - undo()
    Comment com;
    IURNDiagram graph;
    Command cmd;
    
    //impacted list of urnLinl IER
    ArrayList<IntentionalElementRef> impactedColouredList;
    
    public ChangeImpactAnalysisCommand(EObject crietrion, int changeType){
    	//this.urnspec = (URNspec) ((IntentionalElementRef)crietrion).getDef().getGrlspec().getUrnspec();
    	this.changeType = changeType;
    	
    	impactedColouredList = new ArrayList<IntentionalElementRef>();
    	//System.out.println("Constructor of ChangeImpactAnalysisCommand class");	
    	
    	if(crietrion instanceof IntentionalElementRef){
    		//System.out.println("inside ChangeImpactAnalysisCommand");
    		this.crietrion = crietrion;
    		
			this.criCIA = crietrion; // using as pramter in CIA Algorithm
			this.urnspec = (URNspec) ((IntentionalElementRef)crietrion).getDef().getGrlspec().getUrnspec();
	    	
    	}else if (crietrion instanceof LinkRef){
    		//System.out.println("it's LinkRef criterion");
    		LinkRef l= (LinkRef) crietrion;
			this.crietrion = (IntentionalElementRef) l.getTarget();
			this.urnspec = (URNspec) ((IntentionalElementRef)this.crietrion).getDef().getGrlspec().getUrnspec();
	    	
			this.criCIA = crietrion; // using as pramter in CIA Algorithm
    	}
    }
    
    /**
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    public boolean canExecute() {
    	//later try to enable this condition - Hasan
    	/*if(urnspec != null )
    		return true;
    	else
    		return false;*/
    	
    	return true;
    }
    
    /**
	* @see org.eclipse.gef.commands.Command#execute()
	*/
	public void execute() {
		//System.out.println("Save all nodes in List");
		
		//testPreConditions();
		redo();
	}
	
	/**
	 * @see seg.jUCMNav.model.commands.JUCMNavCommand#testPreConditions()
	 */
	public void testPreConditions() {

	}
	
	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		//uncolour all the Elements in Graph
		if(!gmdGraph.getAllGMDGNodes().isEmpty())
		for(NodeGMD node: gmdGraph.getAllGMDGNodes()){
			if(node.Node instanceof IntentionalElementRef){
				IntentionalElementRef IERCol = (IntentionalElementRef)node.Node;
				Command comm = new ChangeColorCommand(IERCol, null);
	            comm.execute();
			}
		}
		if(!impactedColouredList.isEmpty())
		for(IntentionalElementRef IER: impactedColouredList){
			Command comm = new ChangeColorCommand(IER, null);
            comm.execute();
		}
			
		//Remove the added infoComment
		ArrayList<URNdefinition> specDiagrams = new ArrayList<URNdefinition>(urnspec.getUrndef().getSpecDiagrams());
		if(!specDiagrams.isEmpty())
		for (EObject specDiagram : specDiagrams){
			if(specDiagram instanceof IURNDiagram)
				for(Object comment :((IURNDiagram)specDiagram).getComments())
					if(((Comment)comment).getFillColor() == "228,228,228"){
						IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
						//cmd = new AddCommentCommand(graph, com);
						//cmd.undo();
						if(graph.getComments().contains(comment)) {
							graph.getComments().remove(((Comment)comment));
							break;
						}
					}
		}
	}
	
	/**
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		switch (changeType) {
		case 1:
			//JOptionPane.showMessageDialog(null,"1. Modification");
			// construct GMDGraph Tree
			gmdGraph = new GMDGraph(crietrion);
			
			//Get all nodes that belongs to Diagram with respect to slected Criterion
			for(Object node : ((IntentionalElementRef) crietrion).getDiagram().getNodes()){			
				//	if(!gmdGraph.isContained((IntentionalElementRef) node)){
				gmdGraph.create((EObject) node);
				
				//gmdGraph.findParentSib(); // I added it in GMDGraph 168
			}

			gmdGraph.print(); // print all GMDGraph Tree nodes
			
			if(!gmdGraph.getAllGMDGNodes().isEmpty()){
				
				ciaModifiAlgo = new CIAModificationAlgo(urnspec, criCIA, gmdGraph.getAllGMDGNodes());
				undo();
				createInfoComment();
				coloring();
			}
			break;
		case 2: 
			//CIA begin
			//JOptionPane.showMessageDialog(null,"2. Addition/Deletion");
			// construct GMDGraph Tree
			gmdGraph = new GMDGraph(crietrion);
			
			//Get all nodes that belongs to Diagram with respect to slected Criterion
			for(Object node : ((IntentionalElementRef) crietrion).getDiagram().getNodes()){			
				//	if(!gmdGraph.isContained((IntentionalElementRef) node)){
				gmdGraph.create((EObject) node);
				
				//gmdGraph.findParentSib(); // I added it in GMDGraph 168
			}

			gmdGraph.print(); // print all GMDGraph Tree nodes
			
			if(!gmdGraph.getAllGMDGNodes().isEmpty()){
				
				ciaAlgo = new CIADelalgo(urnspec, criCIA, gmdGraph.getAllGMDGNodes());
				undo();
				createInfoComment();
				coloring();
			}
				
			break;
		}//end switch
	} // end redo()
	
	/**
	 * @see seg.jUCMNav.model.commands.JUCMNavCommand#testPostConditions()
	 */
	public void testPostConditions() {

	}
	
	/**
	 * getInstance of Comment to be added for infoComment
	 */
	public void createInfoComment(){
		
		if(changeType == 2){
			//Execute the cia Algorithm addition/deletion
			//determine the number of lines to be added in Comment
			if(ciaAlgo.foundStrategy == true && ciaAlgo.foundUrnLinks == true){
				//undo();
		    	com =UrncoreFactory.eINSTANCE.createComment();
		    	com.setDescription(" \n" + ciaAlgo.infoComment);
		    	com.setHeight(ciaAlgo.infoComment.split("\n").length * 17);
		    	com.setWidth(300);
		    	com.setX(600);
		    	com.setY(15);
		    	com.setFillColor("228,228,228");
		    	IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
		    	cmd = new AddCommentCommand(graph, com);
		    	cmd.execute();
		    	
				//infoComment.split("\n").length
			}else if(ciaAlgo.foundStrategy == true){
				//undo();
				com =UrncoreFactory.eINSTANCE.createComment();
		    	com.setDescription(" \n" + "# Strategies : \n" + ciaAlgo.infoCommentStrategy);
		    	com.setHeight(ciaAlgo.infoCommentStrategy.split("\n").length * 17);
		    	com.setWidth(300);
		    	com.setX(600);
		    	com.setY(15);
		    	com.setFillColor("228,228,228");
		    	IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
		    	cmd = new AddCommentCommand(graph, com);
		    	cmd.execute();
			}else if(ciaAlgo.foundUrnLinks == true){
				//undo();
				com =UrncoreFactory.eINSTANCE.createComment();
		    	com.setDescription(" \n" + "# URN Links : \n" + ciaAlgo.infoCommentUrlLinks);
		    	com.setHeight(ciaAlgo.infoCommentUrlLinks.split("\n").length * 17);
		    	com.setWidth(300);
		    	com.setX(600);
		    	com.setY(15);
		    	com.setFillColor("228,228,228");
		    	IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
		    	cmd = new AddCommentCommand(graph, com);
		    	cmd.execute();
			}
		}else
		{	//Execute the cia Algorithm modification
			//determine the number of lines to be added in Comment
			if(ciaModifiAlgo.foundStrategy == true && ciaModifiAlgo.foundUrnLinks == true){
				//undo();
		    	com =UrncoreFactory.eINSTANCE.createComment();
		    	com.setDescription(" \n" + ciaModifiAlgo.infoComment);
		    	com.setHeight(ciaModifiAlgo.infoComment.split("\n").length * 17);
		    	com.setWidth(300);
		    	com.setX(600);
		    	com.setY(15);
		    	com.setFillColor("228,228,228");
		    	IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
		    	cmd = new AddCommentCommand(graph, com);
		    	cmd.execute();
		    	
				//infoComment.split("\n").length
			}else if(ciaModifiAlgo.foundStrategy == true){
				//undo();
				com =UrncoreFactory.eINSTANCE.createComment();
		    	com.setDescription(" \n" + "# Strategies : \n" + ciaModifiAlgo.infoCommentStrategy);
		    	com.setHeight(ciaModifiAlgo.infoCommentStrategy.split("\n").length * 17);
		    	com.setWidth(300);
		    	com.setX(600);
		    	com.setY(15);
		    	com.setFillColor("228,228,228");
		    	IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
		    	cmd = new AddCommentCommand(graph, com);
		    	cmd.execute();
			}else if(ciaModifiAlgo.foundUrnLinks == true){
				//undo();
				com =UrncoreFactory.eINSTANCE.createComment();
		    	com.setDescription(" \n" + "# URN Links : \n" + ciaModifiAlgo.infoCommentUrlLinks);
		    	com.setHeight(ciaModifiAlgo.infoCommentUrlLinks.split("\n").length * 17);
		    	com.setWidth(300);
		    	com.setX(600);
		    	com.setY(15);
		    	com.setFillColor("228,228,228");
		    	IURNDiagram graph = (IURNDiagram)((IntentionalElementRef) crietrion).getDiagram();
		    	cmd = new AddCommentCommand(graph, com);
		    	cmd.execute();
			}
		}
		
	}
	/**
	 * color all the impacted links in GRL Graph
	 */
	public void coloring(){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		editor=(UCMNavMultiPageEditor) page.getActiveEditor();
		
		if(changeType == 2){
			
			
			// colour all Elements in impactedList
			for(NodeGMD node: ciaAlgo.impactedList){
				
				//colour Intentional Element in impactedList
				if(node.Node instanceof IntentionalElementRef){
					impactedColouredList.add((IntentionalElementRef) node.Node);
					IntentionalElementRef IERCol = (IntentionalElementRef)node.Node;
					Command comm = new ChangeColorCommand(IERCol, "160,160,255");
		            comm.execute();
				}
				
				
				//colour Intentional Element in impactedList with respect to UrnLinks
				for(EObject elem: ciaAlgo.impactedUrnLinksList)
					if(elem instanceof IntentionalElementRef){
						//colour Intentional Element in impactedList
						IntentionalElementRef IERCol = (IntentionalElementRef)elem;
						impactedColouredList.add(IERCol);
						if(elem instanceof IntentionalElementRef){
							Command comm = new ChangeColorCommand(IERCol, "160,160,255");
				            comm.execute();
						}
					}

				
			}
			
			//colour LinkRefs in impactedList
			for(NodeGMD nod: ciaAlgo.impactedList){
				
				if(nod.Node instanceof LinkRef){
					LinkRef lEl = (LinkRef) nod.Node;
					
					for(int i=0; i<editor.getPageCount();i++){
						UrnEditor pageEditor=(UrnEditor) editor.getEditor(i);
						
						EditPart  modelEditPart = (EditPart) pageEditor.getGraphicalViewer().getEditPartRegistry().get(lEl);
						
						if(modelEditPart != null) {
							   ((GraphicalEditPart) modelEditPart).getFigure().setForegroundColor(ColorConstants.lightBlue);
							   //((GraphicalEditPart) modelEditPart).getFigure().setBackgroundColor(ColorConstants.lightBlue);
							   //JOptionPane.showMessageDialog(null, lEl.getLink().getName());
							   break;
						   }
					}
				}
			}
		}else{
			
			
			// colour all Elements in impactedList
			for(NodeGMD node: ciaModifiAlgo.impactedList){
				
				//colour Intentional Element in impactedList
				if(node.Node instanceof IntentionalElementRef){
					impactedColouredList.add((IntentionalElementRef) node.Node);
					IntentionalElementRef IERCol = (IntentionalElementRef)node.Node;
					Command comm = new ChangeColorCommand(IERCol, "160,160,255");
		            comm.execute();
				}
				
				
				//colour Intentional Element in impactedList with respect to UrnLinks
				for(EObject elem: ciaModifiAlgo.impactedUrnLinksList)
					if(elem instanceof IntentionalElementRef){
						//colour Intentional Element in impactedList
						IntentionalElementRef IERCol = (IntentionalElementRef)elem;
						impactedColouredList.add(IERCol);
						if(elem instanceof IntentionalElementRef){
							Command comm = new ChangeColorCommand(IERCol, "160,160,255");
				            comm.execute();
						}
					}

				
			}
			
			//colour LinkRefs in impactedList
			for(NodeGMD nod: ciaModifiAlgo.impactedList){
				
				if(nod.Node instanceof LinkRef){
					LinkRef lEl = (LinkRef) nod.Node;
					
					for(int i=0; i<editor.getPageCount();i++){
						UrnEditor pageEditor=(UrnEditor) editor.getEditor(i);
						
						EditPart  modelEditPart = (EditPart) pageEditor.getGraphicalViewer().getEditPartRegistry().get(lEl);
						
						if(modelEditPart != null) {
							   ((GraphicalEditPart) modelEditPart).getFigure().setForegroundColor(ColorConstants.lightBlue);
							   //((GraphicalEditPart) modelEditPart).getFigure().setBackgroundColor(ColorConstants.lightBlue);
							   //JOptionPane.showMessageDialog(null, lEl.getLink().getName());
							   break;
						   }
					}
				}
			}
		}

	}
}
