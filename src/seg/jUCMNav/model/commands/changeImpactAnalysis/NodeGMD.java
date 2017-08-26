package seg.jUCMNav.model.commands.changeImpactAnalysis;

import grl.Contribution;
import grl.Decomposition;
import grl.Dependency;
import grl.GRLGraph;
import grl.GRLLinkableElement;
import grl.GRLNode;
import grl.IntentionalElement;
import grl.IntentionalElementRef;
import grl.LinkRef;
import grl.Actor;
import grl.ActorRef;
import grl.ElementLink;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import jdk.nashorn.internal.runtime.arrays.IntElements;

import org.eclipse.emf.ecore.EObject;

import seg.jUCMNav.actions.URNSelectionAction;
import seg.jUCMNav.actions.hyperlinks.HyperlinkUtils;
import seg.jUCMNav.editparts.LinkRefEditPart;
import seg.jUCMNav.importexport.z151.generated.GRLmodelElement;
import urn.URNspec;
import urncore.IURNConnection;
import urncore.IURNNode;
import urncore.URNmodelElement;

public class NodeGMD{
	    
	EObject Node; // store the type of Element IER or LinkRef
	
	int type;
	int isInterActor; // 0: Intra-actor 1: Inter-actor -1: non-actor 
	boolean isImpacted;	// check whether it's impacted by change
	int countDecomp; // whether one leg removed
	
	public ArrayList<IntentionalElementRef> sourceElelemnts;
	public ArrayList<IntentionalElementRef> destElements;
	public ArrayList<IURNConnection> inComingLinksList;
	public ArrayList<IURNConnection> outComingLinksList;
	
	//in order add node of type Dependency 
	public ArrayList<NodeGMD> sourceLinks;
	public ArrayList<NodeGMD> destLinks;
	
	// lists for findSibling
	ArrayList<IntentionalElementRef> parent;
	ArrayList<IntentionalElementRef> sibEelements;
	ArrayList<NodeGMD> sibLinks;
	
	public ArrayList<IntentionalElementRef> getSourceElelemnts() {
		return sourceElelemnts;
	}
	
	public ArrayList<NodeGMD> getSourceLinks() {
		return sourceLinks;
	}

	public ArrayList<NodeGMD> getDestLinks() {
		return destLinks;
	}
	
	public ArrayList<IntentionalElementRef> getSibEelements() {
		return sibEelements;
	}

	public ArrayList<NodeGMD> getSibLinks() {
		return sibLinks;
	}
	
	public ArrayList<IntentionalElementRef> getParent() {
		return parent;
	}

	public NodeGMD(){
		Node = null;
		isImpacted = false;
		sourceElelemnts = new ArrayList<IntentionalElementRef>();
		destElements = new ArrayList<IntentionalElementRef>();
		sourceLinks = new ArrayList<NodeGMD>();
		destLinks = new ArrayList<NodeGMD>();
		sibEelements = new ArrayList<IntentionalElementRef>();
		sibLinks = new ArrayList<NodeGMD>();
		parent = new ArrayList<IntentionalElementRef>();
		inComingLinksList = new ArrayList<IURNConnection>();
		outComingLinksList = new ArrayList<IURNConnection>();
	}
	
	public void determineIsInterActor()
	{
		LinkRef link;
		if(Node instanceof LinkRef)
		{
			//JOptionPane.showMessageDialog(null, "Instance of LinkRef inside NodeGMD");
			link=(LinkRef) Node;
			
			if(link.getSource().getContRef() != null && link.getTarget().getContRef() != null )
				if(link.getSource().getContRef() == link.getTarget().getContRef()){
					//JOptionPane.showMessageDialog(null, "Source : "+ ((IntentionalElementRef)link.getSource()).getDef().getName()+
							//"\nTarget : " + ((IntentionalElementRef)link.getTarget()).getDef().getName() +
							//"\nFrom Same Actor");
					isInterActor = 0;
				}else{
					//JOptionPane.showMessageDialog(null, "Source : "+ ((IntentionalElementRef)link.getSource()).getDef().getName()+
						//	"\nTarget : " + ((IntentionalElementRef)link.getTarget()).getDef().getName() +
							//"\nFrom Different Actor");
					isInterActor = 1;
				}
			else
				{
				isInterActor = -1;
				//JOptionPane.showMessageDialog(null, "No Actor Or one is not belongs to Actor");
				}
		
		
			if(link.getLink() instanceof Contribution){
				//JOptionPane.showMessageDialog(null, "Instance of Contribution inside NodeGMD");
				this.type = 1;
			}else if(link.getLink() instanceof Dependency)
			{
				//JOptionPane.showMessageDialog(null, "Instance of Dependency inside NodeGMD");
				this.type = 2;
			}else if(link.getLink() instanceof Decomposition)
			{
				this.type= 3;
				//JOptionPane.showMessageDialog(null, "Instance of Decomposition inside NodeGMD");
			}
		}
	}
}
