/*
 * generated by Xtext 2.13.0
 */
package it.imt.ui.outline

import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.emf.ecore.EObject
import it.imt.qFLan.FatherAndSonsRelations
import it.imt.qFLan.FeaturePredicateValue
import it.imt.qFLan.QFLanVariable
import it.imt.qFLan.ProcessDefinition
import it.imt.qFLan.InitWithProcesses
import it.imt.qFLan.SideEffects
import it.imt.qFLan.InitWithProcessDiagram
import it.imt.qFLan.AdditionWithPredicatesAndFeaturesWithOptionalDelta

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#outline
 */
class QFLanOutlineTreeProvider extends DefaultOutlineTreeProvider {

	override IOutlineNode createRoot(IXtextDocument document) {
		//Thanks to this method the root in the outline will be the model definition, rather than the name of the file
		var documentNode = super.createRoot(document)
		if(documentNode.children.size>0){
			var modelDef = documentNode.children.get(0);
			return modelDef;	
		}
		else{
			return documentNode;	
		}
	}
	
	override protected _isLeaf(EObject modelElement) {
		var ret = super._isLeaf(modelElement); 
		if(modelElement instanceof ProcessDefinition || modelElement instanceof FatherAndSonsRelations || modelElement instanceof FeaturePredicateValue || modelElement instanceof QFLanVariable || modelElement instanceof InitWithProcesses || modelElement instanceof InitWithProcessDiagram 
			|| modelElement instanceof SideEffects || modelElement instanceof AdditionWithPredicatesAndFeaturesWithOptionalDelta
		){
			ret = true;
		}
		return ret;
	}
	
	
	/*override protected _createChildren(IOutlineNode parentNode, EObject modelElement) {
		if(modelElement instanceof ProcessExprImpl){
			var process = modelElement as ProcessExpr;
		 	while(process instanceof ProcessExprImpl && 
		 		 !( (process instanceof ZeroProcess)||
		 			 (process instanceof Prefix)||
		 			 (process instanceof ReferenceToProcessDefinition)||
		 			 (process instanceof Choice)||
		 			 (process instanceof Sequential)||
		 			 (process instanceof Parallel))){
		 		process= process.first
		 	}
		 	for (EObject childElement : process.eContents())
				createNode(parentNode, childElement);
	 	}
	 	//else if(modelElement instanceof InitialState){
	 	//	for (EObject childElement : modelElement.eContents())
		//		createNode(parentNode, childElement);
	 	//}
	 	else{
	 		for (EObject childElement : modelElement.eContents())
				createNode(parentNode, childElement);
	 	}

	}*/

}