package seg.jUCMNav.model.commands.changeImpactAnalysis;

import grl.Actor;
import grl.ActorRef;
import grl.Contribution;
import grl.Decomposition;
import grl.Dependency;
import grl.ElementLink;
import grl.Evaluation;
import grl.EvaluationStrategy;
import grl.IntentionalElement;
import grl.IntentionalElementRef;
import grl.LinkRef;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.internal.editorsupport.ComponentSupport;

import ucm.map.ComponentRef;
import ucm.map.RespRef;
import ucm.map.UCMmap;
import urn.URNlink;
import urn.URNspec;
import urncore.Component;
import urncore.IURNConnection;
import urncore.IURNContainer;
import urncore.IURNContainerRef;
import urncore.IURNDiagram;
import urncore.IURNNode;
import urncore.Responsibility;

public class CIADelalgo {
	private URNspec urnspec;  
	boolean onceExecutedDestLinks;
	boolean isImpacted;
	EObject criterion;
	ArrayList<NodeGMD> impactedList;
	ArrayList<NodeGMD> allGMDGraph;
	ArrayList<NodeGMD> parentLinkList; 
	String infoCommentStrategy;
	String infoCommentUrlLinks;
	List<EvaluationStrategy> strategy; //strategy
	List<Object> evals;
    List<IntentionalElement> nodesInStrategy;
    int countElement;
    boolean foundStrategy;
    boolean foundUrnLinks;
    ArrayList<String> tempFromElemList;
    ArrayList<EObject> impactedUrnLinksList;
    String infoComment;
    
	public CIADelalgo(URNspec urnspec, EObject criterion, ArrayList<NodeGMD> allGMDGraph){
		this.urnspec = urnspec;
		impactedList = new ArrayList<NodeGMD>();
		parentLinkList = new ArrayList<NodeGMD>(); 
		impactedUrnLinksList = new ArrayList<EObject>();
		tempFromElemList = new ArrayList<String>();
		//tempFromElemList = new 
		isImpacted = false;
		onceExecutedDestLinks = false;
		foundStrategy = false;
	    foundUrnLinks = false;
		this.allGMDGraph = new ArrayList<NodeGMD>(allGMDGraph);
		infoCommentStrategy = ""; // using for check the added comment 
		infoCommentUrlLinks = ""; // using for check the added comment 
		infoComment = "";
		countElement = 0;  // count element number in contribution Strategy 
		
		if(criterion instanceof LinkRef)
			System.out.println("The criterion is LinkRef");
			//JOptionPane.showMessageDialog(null,"LinkRef");
		else if(criterion instanceof IntentionalElementRef)
			System.out.println("The criterion is IER");
		
		for(NodeGMD node : allGMDGraph)	{
			if(node.Node instanceof LinkRef){
				if(node.Node.equals(criterion)){
					//JOptionPane.showMessageDialog(null,"The criterion is LinkRef : "+((LinkRef) node.Node).getLink().getName());
					createImpactedList(node);
					break;
				}
			}else if(node.Node instanceof IntentionalElementRef){
				if(node.Node.equals(criterion)){
				//JOptionPane.showMessageDialog(null,"The criterion is IER : "+((IntentionalElementRef)node.Node).getDef().getName());
					createImpactedList(node);
					break;}
			}
		}
		
		nodeInStrategy();
		checkUrnLink();
		print();
		
		if(foundStrategy == true && foundUrnLinks == true){		
			infoComment ="# Strategies : \n" + infoCommentStrategy +
					"---------- \n# URN Links : \n" + infoCommentUrlLinks;
			//infoComment.split("\n").length  * 35 H
			JOptionPane.showMessageDialog(null,infoComment);
		}else if(foundUrnLinks == true){
			
			JOptionPane.showMessageDialog(null,"# URN Links : \n" + infoCommentUrlLinks);
		}else if(foundStrategy == true){
			JOptionPane.showMessageDialog(null,"# Strategies : \n" + infoCommentStrategy);
		}
	}
	
	public void createImpactedList(NodeGMD criterion){
		//JOptionPane.showMessageDialog(null,"createImpactedList");
		if(criterion.Node instanceof LinkRef){
			// check link with its Source.
			LinkRef l = (LinkRef) criterion.Node;
			if(!isContained(criterion)){				
				impactedList.add(criterion);//add the LinkNodeGMD - LinkRef
				
				// in this case we don't use the sourceElements list to get all sources 
				// because each nodeGMD link has only one  IntElementSource. 
				// so we used one statement by using index 0.
				IntentionalElementRef IER = (IntentionalElementRef) l.getTarget();
				
				//get the IntentionalElement Target of the selected link
				// the size of SourceElement is always 1
				if(criterion.sourceElelemnts.size() != 0)
					for(IntentionalElementRef srcIER: criterion.sourceElelemnts)
						if(!isContained(getNodeGMD(srcIER))){
							//impactedList.add(getNodeGMD(srcIER));
							onceExecutedDestLinks= true;
							createImpactedList(getNodeGMD(srcIER));
						}
				/*	for(int z=0; z< criterion.sourceElelemnts.size();z++){
						
						if(criterion.getSourceElelemnts().get(z) instanceof IntentionalElementRef){
							//JOptionPane.showMessageDialog(null,"it wrork");
							//check whether node already in Impacted list
							if(!isContained(getNodeGMD(criterion.getSourceElelemnts().get(z))))
								impactedList.add(getNodeGMD(criterion.getSourceElelemnts().get(z)));
						
								//recursively method
								//createImpactedList(getNodeGMD(criterion.getSourceElelemnts().get(z)));
							createImpactedList(getNodeGMD(IER));
					}
				}*/

			}// if - adding nodeGMD Link and its sourceElement

		}else if(criterion.Node instanceof IntentionalElementRef){
			
			if(!isContained(criterion)){
				impactedList.add(criterion);	// add IER to Impacted list

			// below piece of code should be executed once  with IER
			//get all dest nodeGMD links 
			if(!onceExecutedDestLinks)
				if(!criterion.inComingLinksList.isEmpty() || !criterion.outComingLinksList.isEmpty()){
					if(!criterion.inComingLinksList.isEmpty())
						for(IURNConnection link: criterion.inComingLinksList){
							if(getNodeGMD(link).Node instanceof LinkRef)
							impactedList.add(getNodeGMD(link));
							
							
						}
					
					if(!criterion.outComingLinksList.isEmpty())
						for(IURNConnection link: criterion.outComingLinksList){
							//JOptionPane.showMessageDialog(null,"inside outComlinks");
							impactedList.add(getNodeGMD(link));
						}
					
					onceExecutedDestLinks = true;
				}
				
			/*	
			if(!criterion.getDestLinks().isEmpty()){	
				if(!onceExecutedDestLinks)
					for(NodeGMD nodeDestLi: criterion.destLinks)
						if(nodeDestLi.Node instanceof LinkRef)
							if(!isContained(nodeDestLi))
								impactedList.add(nodeDestLi);
					
				onceExecutedDestLinks = true;
			}
			*/
			
			//below piece of code check whether Element has source, if it's 
			//we get the sourceElement and its nodeGMD Link.
			if(!criterion.getSourceElelemnts().isEmpty()){
				//onceExecutedDestLinks = true;
				for(int i=0; i<criterion.getSourceElelemnts().size(); i++){
					
					IntentionalElementRef IER = (IntentionalElementRef) criterion.getSourceElelemnts().get(i);
					//JOptionPane.showMessageDialog(null,"The size of Source element : " + criterion.getSourceElelemnts().size()+
					//		"\nThe name of  Source element : "+ IER.getName());
					
					if(!isContained(criterion))
						impactedList.add(getNodeGMD(IER));
						
					if(!criterion.sibLinks.isEmpty())
					for(int y=0; y < criterion.sibLinks.size(); y++){
						EObject nod = (EObject)criterion.sibLinks.get(y).Node;
						if(nod instanceof LinkRef){
							LinkRef l = (LinkRef)nod;
							IntentionalElementRef intEleRefSib = (IntentionalElementRef) l.getSource();
							
							//JOptionPane.showMessageDialog(null,"Target  : " + l.getTarget().equals(IER)+
							//		"Source  : "+l.getSource().equals(intEleRefSib));
							
							//it needs enhancement in order to get specific target
							if(!onceExecutedDestLinks)
								if(l.getTarget().equals(IER) && l.getSource().equals(intEleRefSib)){
									//onceExecutedDestLinks=true;	
									if(!isContained(criterion.sibLinks.get(y)))
									{
										impactedList.add(criterion.sibLinks.get(y));
										onceExecutedDestLinks=true;	
										//break;
									}
									
								}
						}
					}
						
					createImpactedList(getNodeGMD(IER));

				} // end of for loop
			}// end of criterion.getSourceElelemnts().isEmpty()
		}				

	}//end if else IntEleRef
}// end createImpactedList function
	
	
	// check whether Impacted element already in the List
	public boolean isContained(NodeGMD node){	
		for(NodeGMD node_ : impactedList)
		{
				if(node_.equals(node))
					return true;			
		}
		return false;
	}
	
	public NodeGMD getNodeGMD(EObject nod){
		for(NodeGMD node_: allGMDGraph)
			if(node_.Node.equals(nod))
				return node_;
		return null;
	}
	
	
	// get NodeGMD by IER id
	public NodeGMD getNodeGMDurnLink(String id){
		for(NodeGMD node_: allGMDGraph)
			if(node_.Node instanceof IntentionalElementRef)
				if(((IntentionalElementRef)node_.Node).getId() == id)
					return node_;
		return null;
	}
	
	// get NodeGMD by LinkRef id
		public NodeGMD getLinkRefNodeGMDurnLink(String id){
			for(NodeGMD node_: allGMDGraph)
				if(node_.Node instanceof LinkRef)
					if(((LinkRef)node_.Node).getLink().getId() == id)
						return node_;
			return null;
		}
		
	public void print (){
		String allImpactedElements = "";
		allImpactedElements +="CIA working Fine .. \nImpacted Elements list: " + impactedList.size() + "\n";
		allImpactedElements += "Impacted Elements of urlLinks: "+ impactedUrnLinksList.size()+ "\n";
		for(NodeGMD t: impactedList)
		{
			if(t.Node instanceof LinkRef){
				LinkRef tL = (LinkRef) t.Node;
				allImpactedElements += "\n" + tL.getLink().getName();
			}
			else if(t.Node instanceof IntentionalElementRef)
			{
				allImpactedElements += "\n" + ((IntentionalElementRef)t.Node).getDef().getName();
			}
		}		
		JOptionPane.showMessageDialog(null,allImpactedElements);
	}
	
	//check weather impacted element belongs to any Strategy 
	public void nodeInStrategy(){
		countElement = 1;
		//get all Strategies from getGrlspec
		strategy = urnspec.getGrlspec().getStrategies();
		
		if(strategy != null){
			//infoCommentStrategy += "# Strategies : \n";
			for(EvaluationStrategy stra: strategy){
				//JOptionPane.showMessageDialog(null,"Strategy Name : " + stra.getName() + "\nStrategy ID : " + stra.getId()+
			    //"\nNumber of Evaluation : "+ stra.getEvaluations().size());
				evals = stra.getEvaluations(); // get Evaluation in Strategy
				
				for(NodeGMD node: impactedList){
					if(node.Node instanceof IntentionalElementRef)
						for(Object eval: evals){
							if(((Evaluation)eval).getIntElement().getRefs().contains(node.Node)){
								foundStrategy  = true;
								infoCommentStrategy += countElement++ + "- Element name : " + ((IntentionalElementRef)node.Node).getDef().getName() +
								"\n     => ID : "+ ((IntentionalElementRef)node.Node).getDef().getId() +
								"\n     => Strategy : " +stra.getName() + "\n";
							}
						}
				}
			} //end for EvaluationStrategy in strategy
		}
		//JOptionPane.showMessageDialog(null,infoComment);
	} // end nodeInStrategy
	
	//check weather impacted element has a URNLinks if available
	public void checkUrnLink(){
		
	//infoCommentUrlLinks += "\n# URN Links : \n";
	//JOptionPane.showMessageDialog(null,impactedList.size());
	if(!urnspec.getUrnLinks().isEmpty())
	for(NodeGMD node: impactedList )
		//check with same GRL Model
		if(node.Node instanceof IntentionalElementRef){
			IntentionalElementRef IER = (IntentionalElementRef) node.Node;
			
			//JOptionPane.showMessageDialog(null,((grl.GRLGraph)((IntentionalElementRef)node.Node).getDiagram()).getName());
			if(!urnspec.getUrnLinks().isEmpty())
			for(Object urnLink: urnspec.getUrnLinks()){
				
				String strGetFromElem = ((URNlink) urnLink).getFromElem().getId();
				String strGetToElem = ((URNlink) urnLink).getToElem().getId();
				// this Part check urnLinks [ FromElem to ToElem ]
				if(IER.getId() == strGetFromElem){
					//getting NodeGMD from allGMDGraph if exist with the same Page
					NodeGMD nodeTemp = getNodeGMDurnLink(strGetToElem);
					
					
					if(nodeTemp!= null && nodeTemp.Node instanceof IntentionalElementRef){
						//this will check UrnLinks with in Same Digram - Graph
						IntentionalElementRef toElemIER = (IntentionalElementRef)nodeTemp.Node;
						
						impactedUrnLinksList.add(nodeTemp.Node);
						tempFromElemList.add(strGetToElem);
						
						infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
								"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
								" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() + 
								"\n     => Target=" + strGetToElem + " - " +  toElemIER.getDef().getName() +
								" - Graph: " + ((grl.GRLGraph)toElemIER.getDiagram()).getName() + "\n";
						
						foundUrnLinks = true;
						
						
					}else if(nodeTemp == null){
						//int tempCount = 0;
						ArrayList<IURNDiagram>specDiagrams= new ArrayList<IURNDiagram>(urnspec.getUrndef().getSpecDiagrams());
						ArrayList<IntentionalElement>intElements = new ArrayList<IntentionalElement>(urnspec.getGrlspec().getIntElements());
						ArrayList<Actor> actors = new ArrayList<Actor>(urnspec.getGrlspec().getActors());
						ArrayList<Responsibility> responsibility = new ArrayList<Responsibility>(urnspec.getUrndef().getResponsibilities());
						ArrayList<Component> component= new ArrayList<Component>(urnspec.getUrndef().getComponents());
						if(!specDiagrams.isEmpty())
							// get all elements from specDiagrams
							//IntentionalElementRefs
							//RespRefs
							//LinkRefs - connections
							//ActorRefs - contRefs
							for(IURNDiagram specDiagram : specDiagrams){
								//JOptionPane.showMessageDialog(null,"inside Digrams : ");
								
								// getting all Nodes inside specDiagram								
								for(Object elem:specDiagram.getNodes())
									//getting all connection inside specDiagram	- IER to IER from different Graph
									if(elem instanceof IntentionalElementRef){
										
										IntentionalElementRef elemIER= (IntentionalElementRef)elem;
										if(elemIER.getId() == strGetToElem){
											//JOptionPane.showMessageDialog(null,"element is - IER"+ elemIER.getId()+ " = " + strGetToElem);
											impactedUrnLinksList.add((EObject) elem);
											//print String
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " +  elemIER.getDef().getName()+
													" - Graph: " + ((grl.GRLGraph)elemIER.getDiagram()).getName() +"\n";
											foundUrnLinks = true;
										}						
										//getting all connection inside specDiagram	- IER to RespRefs
									}else if(elem instanceof RespRef){
										RespRef respRef = (RespRef)elem;
										//JOptionPane.showMessageDialog(null,"element is - "+ IER.getId()+ " = " + strGetToElem);
										if(respRef.getId() == strGetToElem){
											//JOptionPane.showMessageDialog(null,"element is - "+ respRef.getId() + " = " + strGetToElem);
											impactedUrnLinksList.add((EObject) elem);
											
											
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + respRef.getName()+ 
													" - Graph: " + ((ucm.map.UCMmap)((IURNDiagram)respRef.getDiagram())).getName() +"\n";
											//print String
											foundUrnLinks = true;
										}
											
									}else {}
								
								//getting all connection inside specDiagram	- IER to Links
								for(Object elem:specDiagram.getConnections())
									if(elem instanceof LinkRef){
										LinkRef li = (LinkRef) elem;
										if(li.getLink() instanceof Contribution
											|| li.getLink() instanceof Decomposition
											|| li.getLink() instanceof Dependency)
											if(li.getLink().getId() == strGetToElem){
												impactedUrnLinksList.add((EObject) elem);
												infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
														"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
														" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
														"\n     => Target=" + strGetToElem + " - " + li.getLink().getName()+
														" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName()+"\n";
												foundUrnLinks = true;
											}
									}
								
								//getting all actorRefs GRL inside specDiagram	- IER to ActorRef
								for(Object elem:specDiagram.getContRefs())
									if(elem instanceof ActorRef){
										ActorRef actorRef = (ActorRef)elem;
										if(actorRef.getId() == strGetToElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + actorRef.getName()+
													" - Graph: " + ((grl.GRLGraph)actorRef.getDiagram()).getName() + "\n";
											foundUrnLinks = true;
										}
										
										//getting all component refs UCM specDiagram	- IER to CompRefs
									}else if(elem instanceof ComponentRef){
										ComponentRef compRef= (ComponentRef) elem;
										if(compRef.getId() == strGetToElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + compRef.getName()+
													" - Graph: " + ((UCMmap)compRef.getDiagram()).getName() + "\n";
											foundUrnLinks = true;
										}
									}
							}
						
						//get all Intentional Element from grlSpec - IER to IE
						if(!intElements.isEmpty())
							
							//get all intElements 
							for(Object elem : intElements)
								if(elem instanceof IntentionalElement){
									IntentionalElement intElement = (IntentionalElement)elem;
									//JOptionPane.showMessageDialog(null,intElement.getId() + " " + strGetToElem);
									if(intElement.getId() == strGetToElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
												" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
												"\n     => Target=" + strGetToElem + " - " + intElement.getName() ; 
												//" - Graph: " + intElement.getRefs() + "\n";
												String ref = "[ ";
													for(Object refs: intElement.getRefs()){
														ref += ((IntentionalElementRef)refs).getId() + ", ";
														//added all ref's based on IE - urnLink
													impactedUrnLinksList.add(((IntentionalElementRef)refs))	;
													}
													ref+= " ]";
													infoCommentUrlLinks += "     => intElementRefs: " +  ref + "\n";	
										foundUrnLinks = true;
									}
								}
						
							if(!actors.isEmpty())
							//get all Actors Def's
							for(Object elem : actors)
								if(elem instanceof Actor){
									Actor actor = (Actor) elem;
									//JOptionPane.showMessageDialog(null,actor.getId() + " " + strGetToElem);
									if(actor.getId() == strGetToElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
												" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
												"\n     => Target=" + strGetToElem + " - " + actor.getName();
												//" - Graph: " + ((grl.GRLspec)intElement.getRefs().e) + "\n";
										String ref = "[ ";
										for(Object refs: actor.getContRefs())
											ref += ((ActorRef)refs).getId() + ", ";
										ref+= " ]";
										infoCommentUrlLinks += "     => actorRefs: " +  ref + "\n";	
										foundUrnLinks = true;
									}
								}else{}
							
							//get all responsiblities from urnDef - IER to Resp
							if(!responsibility.isEmpty())
								for(Object elem : responsibility)
									if(elem instanceof Responsibility){
										Responsibility resElem= (Responsibility)elem;
										//JOptionPane.showMessageDialog(null,resElem.getId() + " " + strGetToElem);
										if(resElem.getId() == strGetToElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + resElem.getName();
												String ref = "[ ";
												for(Object refs: resElem.getRespRefs())
													ref += ((RespRef)refs).getId() + ", ";
												ref+= " ]";
												infoCommentUrlLinks += "     => respRefs: " +  ref + "\n";	
												foundUrnLinks = true;
										}
									}else{}
									
							//get all Components from urnDef - IER to Compoent
							if(!component.isEmpty())
								for(Object elem:component)
									if(elem instanceof Component){
										Component compElem = (Component) elem;
										//JOptionPane.showMessageDialog(null,compElem.getId() + " " + strGetToElem);
										if(compElem.getId() == strGetToElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + IER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + compElem.getName();
											String ref = "[ ";
											for(Object refs:compElem.getContRefs())
												ref += ((ComponentRef)refs).getId() + ", ";
											ref+= " ]";
											infoCommentUrlLinks += "     => compRefs: " +  ref + "\n";	
											foundUrnLinks = true;
										}
									}else{}
					}// end nodeTemp == null 
				// this Part check urnLinks [ ToElem to FromElem ]
				}else if(IER.getId() == strGetToElem){
					//getting NodeGMD from allGMDGraph if exist with the same Page
					NodeGMD nodeTemp = getNodeGMDurnLink(strGetFromElem);
					
					if(nodeTemp!= null && nodeTemp.Node instanceof IntentionalElementRef){
						//JOptionPane.showMessageDialog(null,"within same Graph");
						//this will check UrnLinks with in Same Digram - Graph
						IntentionalElementRef fromtoElemIER = (IntentionalElementRef)nodeTemp.Node;
						impactedUrnLinksList.add(nodeTemp.Node);
						tempFromElemList.add(strGetFromElem);
						infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
								"\n     => Source=" + strGetFromElem + " - Name: " + fromtoElemIER.getDef().getName() + 
								" - Graph: " + ((grl.GRLGraph)fromtoElemIER.getDiagram()).getName() + 
								"\n     => Target=" + strGetToElem + " - Name: " + IER.getDef().getName() +
								" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() + "\n";
						
						foundUrnLinks = true;
						
					}else if(nodeTemp == null){
						ArrayList<IURNDiagram>specDiagrams= new ArrayList<IURNDiagram>(urnspec.getUrndef().getSpecDiagrams());
						ArrayList<IntentionalElement>intElements = new ArrayList<IntentionalElement>(urnspec.getGrlspec().getIntElements());
						ArrayList<Actor> actors = new ArrayList<Actor>(urnspec.getGrlspec().getActors());
						ArrayList<Responsibility> responsibility = new ArrayList<Responsibility>(urnspec.getUrndef().getResponsibilities());
						ArrayList<Component> component= new ArrayList<Component>(urnspec.getUrndef().getComponents());
						
						if(!specDiagrams.isEmpty())
							// get all elements from specDiagrams
							//IntentionalElementRefs
							//RespRefs
							//LinkRefs - connections
							//ActorRefs - contRefs
							for(IURNDiagram specDiagram : specDiagrams){
								// getting all Nodes inside specDiagram								
								for(Object elem:specDiagram.getNodes())
									//getting all connection inside specDiagram	- IER to IER from different Graph
									if(elem instanceof IntentionalElementRef){
										IntentionalElementRef elemIER= (IntentionalElementRef)elem;
										if(elemIER.getId() == strGetFromElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + elemIER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)elemIER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem +  " - Name: "  +  IER.getDef().getName()+
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
											foundUrnLinks = true;
										}
										//getting all connection inside specDiagram	- IER to RespRefs
									}else if(elem instanceof RespRef){
										RespRef respRef = (RespRef)elem;
										
										if(respRef.getId() == strGetFromElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + respRef.getName() +
													" - Graph: " + ((ucm.map.UCMmap)((IURNDiagram)respRef.getDiagram())).getName() +
													"\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
											//print String
											foundUrnLinks = true;	
										}
											
									}else {}
									
								//getting all connection inside specDiagram	- IER to Links
								for(Object elem:specDiagram.getConnections())
									if(elem instanceof LinkRef){
										LinkRef li = (LinkRef) elem;
										//JOptionPane.showMessageDialog(null,"Check IER to Links");
										if(li.getLink() instanceof Contribution
											|| li.getLink() instanceof Decomposition
											|| li.getLink() instanceof Dependency)
											if(li.getLink().getId() == strGetFromElem){
												impactedUrnLinksList.add((EObject) elem);
												infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
														"\n     => Source=" + strGetFromElem + " - Name: " +li.getLink().getName() +
														" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName()+
														"\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
														" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
												
												foundUrnLinks = true;
											}
									}
								//getting all actorRefs GRL inside specDiagram	- IER to ActorRef
								for(Object elem:specDiagram.getContRefs())	
									if(elem instanceof ActorRef){
										ActorRef actorRef = (ActorRef)elem;
										if(actorRef.getId() == strGetFromElem){

											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + actorRef.getName() + 
													" - Graph: " + ((grl.GRLGraph)actorRef.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
											
											foundUrnLinks = true;
										}
										//getting all component refs UCM specDiagram	- IER to CompRefs
									}else if(elem instanceof ComponentRef){
										ComponentRef compRef= (ComponentRef) elem;
										if(compRef.getId() == strGetFromElem ){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " +compRef.getName() +
													" - Graph: " + ((UCMmap)compRef.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";

											foundUrnLinks = true;
										}
									}
							}// end for(IURNDiagram specDiagram : specDiagrams)
						
						//get all Intentional Element from grlSpec - IER to IE
						if(!intElements.isEmpty())
							//get all intElements 
							for(Object elem : intElements)
								if(elem instanceof IntentionalElement){
									IntentionalElement intElement = (IntentionalElement)elem;
									//JOptionPane.showMessageDialog(null,intElement.getId() + " " + strGetToElem);
									if(intElement.getId() == strGetFromElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + intElement.getName() ;
												String ref = "[ ";
												for(Object refs: intElement.getRefs()){
													ref += ((IntentionalElementRef)refs).getId() + ", ";
													impactedUrnLinksList.add(((IntentionalElementRef)refs));
													
												}
												ref+= " ]";
													infoCommentUrlLinks += "     => intElementRefs: " +  ref + "\n";
													infoCommentUrlLinks += "\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
													" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";

										foundUrnLinks = true;		
												
									}
								}
							
						if(!actors.isEmpty())
							//get all Actors Def's
							for(Object elem : actors)
								if(elem instanceof Actor){
									Actor actor = (Actor) elem;
									//JOptionPane.showMessageDialog(null,actor.getId() + " " + strGetToElem);
									if(actor.getId() == strGetFromElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
															"\n     => Source=" + strGetFromElem + " - Name: " +actor.getName() ; 
															String ref = "[ ";
															for(Object refs: actor.getContRefs())
																ref += ((ActorRef)refs).getId() + ", ";
															ref+= " ]";
															infoCommentUrlLinks += "     => actorRefs: " +  ref + "\n";
										infoCommentUrlLinks += "\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
										" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
										foundUrnLinks = true;		
									}
								}else{}	
						
						//get all responsiblities from urnDef - IER to Resp
						if(!responsibility.isEmpty())
							for(Object elem : responsibility)
								if(elem instanceof Responsibility){
									Responsibility resElem= (Responsibility)elem;
									//JOptionPane.showMessageDialog(null,resElem.getId() + " " + strGetToElem);
									if(resElem.getId() == strGetFromElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " +resElem.getName();
												String ref = "[ ";
												for(Object refs: resElem.getRespRefs())
													ref += ((RespRef)refs).getId() + ", ";
												ref+= " ]";
												infoCommentUrlLinks += "     => respRefs: " +  ref + "\n";	
												infoCommentUrlLinks += "\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
												" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
											foundUrnLinks = true;
									}
								}else{}
						
						//get all Components from urnDef - IER to Compoent
						if(!component.isEmpty())
							for(Object elem:component)
								if(elem instanceof Component){
									Component compElem = (Component) elem;
									//JOptionPane.showMessageDialog(null,compElem.getId() + " " + strGetToElem);
									if(compElem.getId() == strGetFromElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + compElem.getName();
										String ref = "[ ";
										for(Object refs:compElem.getContRefs())
											ref += ((ComponentRef)refs).getId() + ", ";
										ref+= " ]";
										infoCommentUrlLinks += "     => compRefs: " +  ref + "\n";	

										infoCommentUrlLinks += "\n     => Target=" + strGetToElem +  " - Name: "  + IER.getDef().getName()+ 
										" - Graph: " + ((grl.GRLGraph)IER.getDiagram()).getName() +"\n";
										foundUrnLinks = true;
									}
								}else{}
						
					}// end nodeTemp == null 
				} // end - if(IER.getId() == strGetToElem)

		} // end of for(Object urnLink: urnspec.getUrnLinks())
	}else if(node.Node instanceof LinkRef){
		 LinkRef li= (LinkRef)node.Node;
		 if(!urnspec.getUrnLinks().isEmpty())
			 for(Object urnLink: urnspec.getUrnLinks()){
				String strGetFromElem = ((URNlink) urnLink).getFromElem().getId();
				String strGetToElem = ((URNlink) urnLink).getToElem().getId();
				// this Part check urnLinks [ FromElem to ToElem ]
				if(li.getLink().getId() == strGetFromElem){
					// getting NodeGMD by sending IER ID - return NodeGMD 
					NodeGMD nodeTemp = getNodeGMDurnLink(strGetToElem);
					
					if(nodeTemp!= null && nodeTemp.Node instanceof IntentionalElementRef){
						IntentionalElementRef toElemIER = (IntentionalElementRef)nodeTemp.Node;
						impactedUrnLinksList.add(nodeTemp.Node);
						tempFromElemList.add(strGetToElem);
						
						infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
								"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
								" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
								"\n     => Target=" + strGetToElem + " - " + toElemIER.getDef().getName() +
								" - Graph: " + ((grl.GRLGraph)toElemIER.getDiagram()).getName() + "\n";
						foundUrnLinks = true;
						
					}else if(nodeTemp == null){
						ArrayList<IURNDiagram>specDiagrams= new ArrayList<IURNDiagram>(urnspec.getUrndef().getSpecDiagrams());
						ArrayList<IntentionalElement>intElements = new ArrayList<IntentionalElement>(urnspec.getGrlspec().getIntElements());
						ArrayList<Actor> actors = new ArrayList<Actor>(urnspec.getGrlspec().getActors());
						ArrayList<Responsibility> responsibility = new ArrayList<Responsibility>(urnspec.getUrndef().getResponsibilities());
						ArrayList<Component> component= new ArrayList<Component>(urnspec.getUrndef().getComponents());
						
						if(!specDiagrams.isEmpty())
							// get all elements from specDiagrams
							//IntentionalElementRefs
							//RespRefs
							//LinkRefs - connections
							//ActorRefs - contRefs
							for(IURNDiagram specDiagram : specDiagrams){
								// getting all Nodes inside specDiagram								
								for(Object elem:specDiagram.getNodes())
									//getting all connection inside specDiagram	- LinkRef to IER from different Graph
									if(elem instanceof IntentionalElementRef){
										IntentionalElementRef elemIER= (IntentionalElementRef)elem;
										if(elemIER.getId() == strGetToElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
													" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + elemIER.getDef().getName() +
													" - Graph: " + ((grl.GRLGraph)elemIER.getDiagram()).getName() +"\n";
											foundUrnLinks = true;
										}	
											//getting all connection inside specDiagram	- LinkRef to RespRefs
										}else if(elem instanceof RespRef){
											RespRef respRef = (RespRef)elem;
											//JOptionPane.showMessageDialog(null,"element is - "+ IER.getId()+ " = " + strGetToElem);
											if(respRef.getId() == strGetToElem){
												//JOptionPane.showMessageDialog(null,"element is - "+ respRef.getId() + " = " + strGetToElem);
												impactedUrnLinksList.add((EObject) elem);
												infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
														"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
														" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
														"\n     => Target=" + strGetToElem + " - " + respRef.getName()+ 
													" - Graph: " + ((ucm.map.UCMmap)((IURNDiagram)respRef.getDiagram())).getName() +"\n";
												foundUrnLinks = true;
											}
												
										}else {}
										
										//getting all connection inside specDiagram	- Link to Links
										for(Object elemLink:specDiagram.getConnections())
											if(elemLink instanceof LinkRef){
												LinkRef li_ = (LinkRef) elemLink;
												if(li_.getLink() instanceof Contribution
														|| li_.getLink() instanceof Decomposition
														|| li_.getLink() instanceof Dependency)
													if(li_.getLink().getId() == strGetToElem){
														impactedUrnLinksList.add((EObject) elemLink);
														infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
																"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
																" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
																"\n     => Target=" + strGetToElem + " - " + li_.getLink().getName()+
																" - Graph: " + ((grl.GRLGraph)li_.getDiagram()).getName()+"\n";
														foundUrnLinks = true;
													}
											}
										
										//getting all actorRefs GRL inside specDiagram	- Link to ActorRef
										for(Object elemCont:specDiagram.getContRefs())
											if(elemCont instanceof ActorRef){
												ActorRef actorRef = (ActorRef)elemCont;
												if(actorRef.getId() == strGetToElem){
													impactedUrnLinksList.add((EObject) elemCont);
													infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
															"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
															" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
															"\n     => Target=" + strGetToElem + " - " + actorRef.getName()+
															" - Graph: " + ((grl.GRLGraph)actorRef.getDiagram()).getName() + "\n";
													foundUrnLinks = true;
												}
												
											//getting all component refs UCM specDiagram	- Link to CompRefs
											}else if(elemCont instanceof ComponentRef){
												ComponentRef compRef= (ComponentRef) elemCont;
												if(compRef.getId() == strGetToElem){
													impactedUrnLinksList.add((EObject) elemCont);
													infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
															"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
															" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
															"\n     => Target=" + strGetToElem + " - " + compRef.getName()+
															" - Graph: " + ((UCMmap)compRef.getDiagram()).getName() + "\n";
													foundUrnLinks = true;
											}
									}
								
							}//end for(IURNDiagram specDiagram : specDiagrams)
					
						//get all Intentional Element from grlSpec - Link to IE
						if(!intElements.isEmpty())
							//get all intElements 
							for(Object elem : intElements)
								if(elem instanceof IntentionalElement){
									IntentionalElement intElement = (IntentionalElement)elem;
									//JOptionPane.showMessageDialog(null,intElement.getId() + " " + strGetToElem);
									if(intElement.getId() == strGetToElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
												" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
												"\n     => Target=" + strGetToElem + " - " + intElement.getName() ; 
										//" - Graph: " + intElement.getRefs() + "\n";
										String ref = "[ ";
											for(Object refs: intElement.getRefs()){
												ref += ((IntentionalElementRef)refs).getId() + ", ";
												//added all ref's based on IE - urnLink
											impactedUrnLinksList.add(((IntentionalElementRef)refs))	;
											}
											ref+= " ]";
											infoCommentUrlLinks += "     => intElementRefs: " +  ref + "\n";	
								foundUrnLinks = true;
									}
								}
						
						if(!actors.isEmpty())
							////get all Actor's Def's from grlSpec - Link to Actor's Def
							for(Object elem : actors)
								if(elem instanceof Actor){
									Actor actor = (Actor) elem;
									//JOptionPane.showMessageDialog(null,actor.getId() + " " + strGetToElem);
									if(actor.getId() == strGetToElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
												" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
												"\n     => Target=" + strGetToElem + " - " + actor.getName();
										//" - Graph: " + ((grl.GRLspec)intElement.getRefs().e) + "\n";
												String ref = "[ ";
												for(Object refs: actor.getContRefs())
													ref += ((ActorRef)refs).getId() + ", ";
												ref+= " ]";
												infoCommentUrlLinks += "     => actorRefs: " +  ref + "\n";	
											foundUrnLinks = true;
									}
								}else{}
						
						//get all responsiblities Def from urnDef - Link to Resp
						if(!responsibility.isEmpty())
							for(Object elem : responsibility)
								if(elem instanceof Responsibility){
									Responsibility resElem= (Responsibility)elem;
									//JOptionPane.showMessageDialog(null,resElem.getId() + " " + strGetToElem);
									if(resElem.getId() == strGetToElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
												" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
												"\n     => Target=" + strGetToElem + " - " + resElem.getName();
												String ref = "[ ";
												for(Object refs: resElem.getRespRefs())
													ref += ((RespRef)refs).getId() + ", ";
												ref+= " ]";
												infoCommentUrlLinks += "     => respRefs: " +  ref + "\n";	
												foundUrnLinks = true;
									}
								}else{}
						
						//get all Components from urnDef - Link to Compoent
						if(!component.isEmpty())
							for(Object elem:component)
								if(elem instanceof Component){
									Component compElem = (Component) elem;
									//JOptionPane.showMessageDialog(null,compElem.getId() + " " + strGetToElem);
									if(compElem.getId() == strGetToElem){
										impactedUrnLinksList.add((EObject) elem);
										infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
												"\n     => Source=" + strGetFromElem + " - Name: " + li.getLink().getName()+
												" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() +
												"\n     => Target=" + strGetToElem + " - " + compElem.getName();
										String ref = "[ ";
										for(Object refs:compElem.getContRefs())
											ref += ((ComponentRef)refs).getId() + ", ";
										ref+= " ]";
										infoCommentUrlLinks += "     => compRefs: " +  ref + "\n";	
										foundUrnLinks = true;
									}
								}else{}
					}// end nodeTemp == null 
					// this Part check urnLinks [ ToElem to FromElem ]
				}else if(li.getLink().getId() == strGetToElem){
					NodeGMD nodeTemp = getNodeGMDurnLink(strGetFromElem);
					
					if(nodeTemp!= null && nodeTemp.Node instanceof IntentionalElementRef){
						IntentionalElementRef toElemIER = (IntentionalElementRef)nodeTemp.Node;
						impactedUrnLinksList.add(nodeTemp.Node);
						tempFromElemList.add(strGetFromElem);
						
						infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
								"\n     => Source=" + strGetFromElem + " - Name: " + toElemIER.getDef().getName()+
								" - Graph: " + ((grl.GRLGraph)toElemIER.getDiagram()).getName() +
								"\n     => Target=" + strGetToElem + " - " + li.getLink().getName() +
								" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() + "\n";
						foundUrnLinks = true;
						
					}else if(nodeTemp == null){
						ArrayList<IURNDiagram>specDiagrams= new ArrayList<IURNDiagram>(urnspec.getUrndef().getSpecDiagrams());
						ArrayList<IntentionalElement>intElements = new ArrayList<IntentionalElement>(urnspec.getGrlspec().getIntElements());
						
						if(!specDiagrams.isEmpty())
							// get all elements from specDiagrams
							//IntentionalElementRefs
							//IntentionalElement
							for(IURNDiagram specDiagram : specDiagrams){
								// getting all Nodes inside specDiagram								
								for(Object elem:specDiagram.getNodes())
									//getting all connection inside specDiagram	- LinkRef to IER from different Graph
									if(elem instanceof IntentionalElementRef){
										IntentionalElementRef elemIER= (IntentionalElementRef)elem;
										if(elemIER.getId() == strGetFromElem){
											impactedUrnLinksList.add((EObject) elem);
											infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
													"\n     => Source=" + strGetFromElem + " - Name: " + elemIER.getDef().getName()+
													" - Graph: " + ((grl.GRLGraph)elemIER.getDiagram()).getName() +
													"\n     => Target=" + strGetToElem + " - " + li.getLink().getName() +
													" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() + "\n";		
											foundUrnLinks = true;
	
										}
											//getting all connection inside specDiagram	- LinkRef to RespRefs
										}else if(elem instanceof RespRef){
											RespRef respRef = (RespRef)elem;
											//JOptionPane.showMessageDialog(null,"element is - "+ IER.getId()+ " = " + strGetToElem);
											if(respRef.getId() == strGetFromElem){
												//JOptionPane.showMessageDialog(null,"element is - "+ respRef.getId() + " = " + strGetToElem);
												impactedUrnLinksList.add((EObject) elem);
												infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
														"\n     => Source=" + strGetFromElem + " - Name: " +respRef.getName()+ 
														" - Graph: " + ((ucm.map.UCMmap)((IURNDiagram)respRef.getDiagram())).getName() +
														"\n     => Target=" + strGetToElem + " - " + li.getLink().getName() +
														" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() + "\n";	
												foundUrnLinks = true;
											}
											
										}else {}
								//getting all connection inside specDiagram	- Link to Links
								for(Object elemLink:specDiagram.getConnections())
									if(elemLink instanceof LinkRef){
										LinkRef li_ = (LinkRef) elemLink;
										if(li_.getLink() instanceof Contribution
												|| li_.getLink() instanceof Decomposition
												|| li_.getLink() instanceof Dependency)
											if(li_.getLink().getId() == strGetFromElem){
												impactedUrnLinksList.add((EObject) elemLink);
												infoCommentUrlLinks += countElement++ + "- Type : " + ((URNlink) urnLink).getType() +
														"\n     => Source=" + strGetFromElem + " - Name: " + li_.getLink().getName()+
														" - Graph: " + ((grl.GRLGraph)li_.getDiagram()).getName() +
														"\n     => Target=" + strGetToElem + " - " + li.getLink().getName() +
														" - Graph: " + ((grl.GRLGraph)li.getDiagram()).getName() + "\n";	
												foundUrnLinks = true;
											}
									}
							}//end for(IURNDiagram specDiagram : specDiagrams)
							
					}
				}
			 } // for(Object urnLink: urnspec.getUrnLinks())
	}
	}//end public void checkUrnLink()
}//end class
