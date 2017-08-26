package seg.jUCMNav.model.commands.changeImpactAnalysis;

import java.util.ArrayList;

import urncore.IURNContainerRef;
import grl.Contribution;
import grl.Dependency;
import grl.IntentionalElementRef;
import grl.LinkRef;

public class DepNode {

	LinkRef dependencyNode;
	int type;
	int isInterActor;
	IntentionalElementRef target;
	
	public DepNode(LinkRef dependencyNode){
		this.dependencyNode = dependencyNode;
		this.target = (IntentionalElementRef) dependencyNode.getTarget();
		if(dependencyNode instanceof Contribution){
			this.type = 1;
		}else if(dependencyNode instanceof Dependency)
		{
			this.type = 2;
		}else 
		{this.type= 3;}
		
		if(dependencyNode.getSource().getContRef() != null && dependencyNode.getTarget().getContRef() != null )
			if(dependencyNode.getSource().getContRef() == dependencyNode.getTarget().getContRef()){
				isInterActor = 0;
			}else
				isInterActor = 1;
		else
			isInterActor = -1;
		
	}
}
