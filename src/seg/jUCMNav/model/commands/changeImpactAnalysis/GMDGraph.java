package seg.jUCMNav.model.commands.changeImpactAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.emf.ecore.EObject;

import com.sun.corba.se.impl.orbutil.graph.Node;

import urn.URNspec;
import urncore.URNmodelElement;
import grl.Actor;
import grl.Contribution;
import grl.Decomposition;
import grl.Dependency;
import grl.ElementLink;
import grl.GRLGraph;
import grl.GRLNode;
import grl.IntentionalElement;
import grl.IntentionalElementRef;
import grl.LinkRef;
import grl.kpimodel.Indicator;

public class GMDGraph {
	
	private URNspec urnspec;
	private GRLGraph grlGraph;
	private IntentionalElement grlelement;
    private EObject crietrion;
	
	
	ArrayList<NodeGMD> allGMDGNodes;

	public ArrayList<NodeGMD> getAllGMDGNodes() {
		return allGMDGNodes;
	}

	public GMDGraph(EObject crietrion)
	{		
			 allGMDGNodes = new ArrayList<NodeGMD>();
			 //System.out.println(" Number of All Nodes : "+ allLinkNodes.size());	 
	}// end CreateGMDG construct 
	
	/**
	 * It adds Intentional Element Refs as a node within a graph 
	 * @param IER
	 */
	public void create(EObject IER)
	{
		if(IER instanceof IntentionalElementRef){
		//boolean decompAdded= false;
		NodeGMD node = new NodeGMD();
		//1. add the actual node
		
		node.Node = (IntentionalElementRef)IER;
		
		//2. get the Pred first 

		for(Object l : ((IntentionalElementRef)IER).getPred()){
			if(l instanceof LinkRef){
			LinkRef link = (LinkRef) l;
			node.inComingLinksList.add(link);
			//JOptionPane.showMessageDialog(null,"Pred Links [ "+ node.inComingLinksList.size() +" ] : " + link.getLink().getName()+
					//"\n The name of IER" + ((IntentionalElementRef)IER).getDef().getName()); 
			NodeGMD depnode = new NodeGMD();		
			depnode.Node = link;
			
			//determine weather Element is within same actor of different.
			depnode.determineIsInterActor();
			
						
			if(link.getLink() instanceof Contribution){
				node.destLinks.add(depnode);
				//node.sourceLinks.add(node);
				depnode.sourceLinks.add(node);// 13/12
				//depnode.destLinks.add(node);
				// add properties of the depNode
				depnode.sourceElelemnts.add((IntentionalElementRef) link.getTarget());
				
				// add Dependency as a node in GMDGraph 
				allGMDGNodes.add(depnode);
				//JOptionPane.showMessageDialog(null, "Contribution "+ allGMDGNodes.size());
			}
			else if(link.getLink() instanceof Dependency)
			{
				IntentionalElementRef depSource= 	(IntentionalElementRef) ((LinkRef)depnode.Node).getSource();
				node.sourceElelemnts.add(depSource);
				depnode.sourceLinks.add(node);
				//node.sourceLinks.add(node);	// 13/12 
				// add properties of the depNode
				depnode.sourceElelemnts.add((IntentionalElementRef) link.getSource());
				allGMDGNodes.add(depnode);
				//JOptionPane.showMessageDialog(null, "Dependency "+ allGMDGNodes.size());
				// add the source elementT Ref of the dependncy in the sourceElementslist 
				// of the current node 
						
			}
			else if(link.getLink()  instanceof Decomposition){
				// code for check Decomposition type as one Node
				 			
				//if(!decompAdded){
					//decompAdded = true;
					node.destLinks.add(depnode);
                    depnode.sourceLinks.add(node); // 13/12
					// add properties of the depNode
					depnode.sourceElelemnts.add((IntentionalElementRef) link.getTarget());
					
					//It add a Dependency Link as a node in GMDGraph 
					
					allGMDGNodes.add(depnode);
					//JOptionPane.showMessageDialog(null, "Decomposition "+ allGMDGNodes.size());
				//}
			}	
			
			// in Pred the source elements are added to destElements List
			// ad the opositie happened in Succ but u need to add condition Dependency 
			if(!(link.getLink() instanceof Dependency))
				{
					// get the source of DependencyLink - Normal flow
				  IntentionalElementRef source = (IntentionalElementRef) link.getSource();
				  node.destElements.add(source);				
				}
			}
		} //end of the Pred
		
		//3 .get the Succ 
		for(Object l : ((IntentionalElementRef)IER).getSucc()){
			//node.sourceLinks.a
			if(l instanceof LinkRef){
			LinkRef link = (LinkRef) l;
			node.outComingLinksList.add(link);
			//JOptionPane.showMessageDialog(null,"Succ Links [ "+ node.outComingLinksList.size() +" ] : " + link.getLink().getName()+
					//"\n The name of IER" + ((IntentionalElementRef)IER).getDef().getName());
			NodeGMD depnode = new NodeGMD();		
			depnode.Node = link;
			//determine weather Element is within same actor of different.
			depnode.determineIsInterActor();
			
			
			IntentionalElementRef target = (IntentionalElementRef) link.getTarget();
			//IntentionalElementRef source = (IntentionalElementRef) link.getSource();
			if(!(link.getLink() instanceof Dependency)){
					node.sourceElelemnts.add(target);
					//node.sourceLinks.add(depnode);
					depnode.sourceElelemnts.add(target);
				}
			else
			{
				node.destElements.add(target);
				node.destLinks.add(depnode);
				depnode.sourceLinks.add(node); // 13/12

			}
		}
		} // end of Succ 
		// end of graph creation  
		
		
		//4. Add Source Elements 
		
		//Add the mainNode to AllGMD nodes
		allGMDGNodes.add(node);
		}
		
		findParentSib();
	}
	
	
	
	/**
	 * check whether IntELRef or Link  is already CREATED as a node in GMgraph 
	 * @param IERNode
	 * @return
	 */
	public boolean isContained(EObject IERNode){
		for(NodeGMD node : allGMDGNodes)
		{
				if(node.Node.equals(IERNode))
					return true;			
		}
		return false;
	}
	
	/**
	 * find all siblings nodes for Parent in GMDGraph  
	 * @param sibling
	 */
	public void findParentSib(){
		for (NodeGMD node : allGMDGNodes){
			// Save the node Parent source
			node.parent = node.sourceElelemnts;
			
			/*if(node.Node instanceof IntentionalElementRef){
				JOptionPane.showMessageDialog(null,"Node is " + ((IntentionalElementRef) node.Node).getDef().getName());
				
				for(IntentionalElementRef ier: node.sourceElelemnts){
					JOptionPane.showMessageDialog(null,"Parent of node is : "+
				ier.getDef().getName());
				}
			}*/
			findSib(node);
		}
	}
	
	/**
	 * find all siblings nodes belongs to Parent in GMDGraph  
	 * @param sibling
	 */
	public void findSib(NodeGMD node){
		for(NodeGMD temp: allGMDGNodes)
			if(temp.destElements.contains(node.Node)){
				node.sibEelements = temp.destElements;
				//JOptionPane.showMessageDialog(null,"number of siblings" + node.sibEelements.size());
				node.sibLinks = temp.destLinks;				
				break;
			}
	}

	/**
	 * Print all node in GMDGraph  
	 * @param print
	 */
	
	public void print() {
		int intentionalElCount=0;
		int DepNodeCount=0;
		System.out.println("\n\n\n\nNewTest \nNodes in Graph= "+ allGMDGNodes.size());
		
		for(NodeGMD node : allGMDGNodes){
	// display the destinations
			
			if(node.Node instanceof IntentionalElementRef)
			{
				intentionalElCount++;
				IntentionalElementRef ier=(IntentionalElementRef) node.Node;
				System.out.println("= = = = = = = = = = = = ");	
				System.out.println("IntentionalElem node: "+ ier.getDef().getName());
			
				if(!node.destElements.isEmpty()){
					System.out.println("  No. of IntentionalElem Dest= "+ node.destElements.size());
					for(IntentionalElementRef dest: node.destElements)
					{
						System.out.println("   - "+ dest.getDef().getName());
					}
				}
			
				if(node.sibEelements.size() != 0){
					System.out.println("\n === === ==== \nNumber of Siblings : "+
							(int)(node.sibEelements.size()+ node.sibLinks.size()));
				for(IntentionalElementRef sib: node.sibEelements)
				{
					System.out.println("   - "+ sib.getDef().getName());
				}
				
				}
				if(node.sibLinks.size() !=0){
					for(NodeGMD link: node.sibLinks){
						LinkRef l = (LinkRef) link.Node;
						System.out.println("   - "+ l.getLink().getName());
					}
				}
			
				if(!node.destLinks.isEmpty()){
					System.out.println("\n  No. of Dependency dest= "+node.destLinks.size());
					for(NodeGMD dest: node.destLinks)
					{  LinkRef dep=(LinkRef) dest.Node;
						System.out.println("   - "+ "Dependency name: "+dep.getLink().getName());
					}
				}
			// display the sources
			
				if(!node.sourceElelemnts.isEmpty()){
					System.out.println("- - - - - ");	
					System.out.println("  No. of IntentionalElem Sources= "+ node.sourceElelemnts.size());
					for(IntentionalElementRef source: node.sourceElelemnts)
					{
						System.out.println("   - "+ source.getDef().getName());
					}
				}

		}//when node is a depNode
			
			else if(node.Node instanceof LinkRef)
			{
				System.out.println("= = = = = = = = = = = = ");
				DepNodeCount++;
				
				LinkRef ier=(LinkRef) node.Node;
				System.out.println("Dependency node: "+ ier.getLink().getName() );
				
				if(!node.destElements.isEmpty()){
					System.out.println("  No. of IntentionalElem Dest= "+ node.destElements.size());
					for(IntentionalElementRef dest: node.destElements)
					{
						System.out.println("   - "+ dest.getDef().getName());
					}
				}
				// display the sources
				System.out.println("- - - - - ");	
				if(!node.sourceElelemnts.isEmpty()){
					System.out.println("  No. of IntentionalElem Sources= "+ node.sourceElelemnts.size());
					
					for(IntentionalElementRef source: node.sourceElelemnts)
					{
						System.out.println("   - "+ source.getDef().getName());
					}
				}
			}
			System.out.println(" = = = = = = = = = = = = = ");
			System.out.println("No. of Dependency Nodes in Graph= "+ DepNodeCount);
			System.out.println("No. of IntElement Nodes in Graph= "+ intentionalElCount);	
		} // end for allGMDGNodes
	}//end Print 
}
 