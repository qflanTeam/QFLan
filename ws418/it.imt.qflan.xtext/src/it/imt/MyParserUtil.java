package it.imt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import it.imt.qFLan.*;
//import it.imt.qflaner.*; //it.imt.erode.crn.chemicalReactionNetwork.*;
//import it.imt.qFLan.Constraint;

/**
 * 
 * @author Andrea Vandin
 *
 */
public class MyParserUtil {

	public static final HashSet<String> RESERVEDKEYWORDS = new HashSet<>(Arrays.asList(
			"abstract","continue","for","new","switch","assert","default","goto","package","synchronized","boolean","do","if","private","this",
			"break","double","implements","protected","throw","byte","else","import","public","throws","case","enum*","instanceof","return","transient",
			"catch","extends","int","short","try","char","final","interface","static","void","class","finally","long","strictfp","volatile","const","float",
			"native","super","while","true","false","null"));
	
	public static List<String> parseDeltas(MultiQuatexQuery q,String defaultDelta){
		//StringBuilder sb=new StringBuilder("[ ");
		List<String> l = new ArrayList<String>();
		if(q instanceof NonParametricQueries){
			parseDeltas((NonParametricQueries)q, defaultDelta,l);
		}
		else{
			parseDeltas((ParametricQuery)q, defaultDelta,l);
		}
		//sb.replace(sb.length()-1, sb.length(),"]");
		//return sb.toString();
		return l;
	}
	
	private static void parseDeltas(NonParametricQueries queries,String defaultDelta,List<String> l){
		for (NonParametricQuery q : queries.getQueries()) {
			parseOptionalDelta(defaultDelta, l, q.getMqObs());
		}
	}

	private static void parseDeltas(ParametricQuery query,String defaultDelta,List<String> l) {
		parseOptionalDelta(defaultDelta, l, query.getMqObs());
	}
	
	public static void parseOptionalDelta(String defaultDelta, List<String> l, SetOfObs observsations) {
		for(AdditionWithPredicatesAndFeaturesWithOptionalDelta o : observsations.getObs()){
			if(o.getDelta()==null){
				l.add(defaultDelta);
			}
			else{
				String delta=visitExpr(o.getDelta());
				l.add(delta);
			}
		}
	}
	
	public static String computeQuery(NonParametricQueries queries) {
		StringBuilder sb=new StringBuilder();
		int i=0;
		ArrayList<String> tOps = new ArrayList<String>();
		for (NonParametricQuery q : queries.getQueries()) {
			SetOfObs obs= q.getMqObs();
			for(AdditionWithPredicatesAndFeaturesWithOptionalDelta o:obs.getObs()){
				String parsedObs = parseMultiQuaTExObservation(o.getObs());
				//sb.append("eval E[ "+tOp+"{"+parsedObs+"}) ] ;\n");
				
				//String tOp="ProductObsAfterFirstDeploy"+i;
				String tOp="ProductObs"+i;
				tOps.add(tOp);
				sb.append(		
						tOp+"() =\n"+
								" if "+writeCond(q.getCond())+" \n"+
								"	then "+parsedObs+" \n"+
								"	else # "+tOp+"()\n"+
						" fi ;\n");
				i++;
			}	
			for (String tOp : tOps) {
				sb.append("eval E[ "+tOp+"() ] ;\n");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public static String computeQuery(UntilQuery query) {
		StringBuilder sb=new StringBuilder();
		int i=0;
		ArrayList<String> tOps = new ArrayList<String>();
		
		SetOfObs obs= query.getMqObs();
		for(AdditionWithPredicatesAndFeaturesWithOptionalDelta o:obs.getObs()){
			String parsedObs = parseMultiQuaTExObservation(o.getObs());
			//sb.append("eval E[ "+tOp+"{"+parsedObs+"}) ] ;\n");
			
			//String tOp="ProductObsAfterFirstDeploy"+i;
			String tOp="ProductObs"+i;
			tOps.add(tOp);
			sb.append(		
					tOp+"() =\n"+
							" if "+writeCond(query.getCond())+" \n"+
							"	then  "+parsedObs+" \n"+
							"	else # "+tOp+"()\n"+
					" fi ;\n");
			i++;
		}	
		for (String tOp : tOps) {
			sb.append("eval E[ "+tOp+"() ] ;\n");
		}
		
		sb.append("\n");
		return sb.toString();
	}
	
	
	private static String writeCond(BoolExpr cond) {
		// TODO Auto-generated method stub
		//AAA
		//return "{ s.rval(\"deploys\") == 1.0 }";
		return "( "+
				visitConstraint(cond,true)
				+" )";
	}

	public static String computeQuery(ParametricQuery query) {
		StringBuilder sb=new StringBuilder();
		SetOfObs observations = query.getMqObs();
		int i=1;
		Collection<String> tOps=new ArrayList<String>(observations.getObs().size());
		parseMQObs(sb, observations, i, tOps,false);
		sb.append("eval parametric(");
		for (String tOp : tOps) {
			sb.append("E[ "+tOp+"(x) ], ");
		}
		sb.append("x,"+query.getFrom()+","+query.getStep()+","+query.getTo()+") ;\n");
		return sb.toString();
	}

	private static void parseMQObs(StringBuilder sb, SetOfObs observations, int i, Collection<String> tOps,boolean steadystate) {
		for (AdditionWithPredicatesAndFeaturesWithOptionalDelta o : observations.getObs()) {
			String parsedObs = parseMultiQuaTExObservation(o.getObs());
			String tOp="obs"+i+"AtStep";
			
			Expression expr = o.getObs();
			if(expr instanceof ReftToQFLanVariableOrFeature){
				QFLanVariableOrFeature varOrFeat = ((ReftToQFLanVariableOrFeature) expr).getValue();
				String name = varOrFeat.getName();
				tOp=name+"_"+i;//+"AtStep";
			}
			else if(expr instanceof Predicate){
				Predicate pred = (Predicate)expr;
				String name = pred.getPredicate().getName();
				tOp=name+"_"+i;//+"AtStep";
			}
			tOps.add(tOp);
			
			if(steadystate) {
				sb.append(tOp+"() = ");
				sb.append(parsedObs+";\n");
			}
			else {
				sb.append(tOp+"(numberOfStep) = ");
				sb.append("\n if ( s.rval(\"steps\") >= numberOfStep )\n"+
						    "  then "+parsedObs+"\n"+
						    "  else # "+tOp+"(numberOfStep)\n"+
						    " fi ;\n");	
			}
					//"eval parametric(");
			i++;
		}
	}
	
	public static String computeQuery(SteadyStateQuery query) {
		StringBuilder sb=new StringBuilder();
		SetOfObs observations = query.getMqObs();
		int i=1;
		Collection<String> tOps=new ArrayList<String>(observations.getObs().size());
		parseMQObs(sb, observations, i, tOps,true);
		if(query.getAlgorithm().equals("BM")) {
			sb.append("eval autoBM(");
		}
		else {
			sb.append("eval autoRD(");
		}
		
		int o=0;
		for (String tOp : tOps) {
			sb.append("E[ "+tOp+"() ]");
			if(o<tOps.size()-1) {
				sb.append(", ");
			}
			o++;
		}
		sb.append(") ;\n");
		return sb.toString();
	}
	
	private static String parseMultiQuaTExObservation(Expression o) {
		//return "s.rval(\"af1\")";
		return visitExpr(o, true);
	}

	public static String visitExpr(Expression expr){
		return visitExpr(expr,false);
	}
	
	/**
	 * Parse an arithmetic expression enriched with predicates
	 * If the mq flag (multiquatex) is true, it parses a state observation to be analysed using MultiVeStA (including also qflanvariables and features) 
	 * @param expr
	 * @return
	 */
	public static String visitExpr(Expression expr,boolean mq){
		String leftVisited;  
		String rightVisited;
		if(expr instanceof NumberLiteral){
			NumberLiteral val = (NumberLiteral)expr;
			return String.valueOf(val.getValue());
		}
		else if(expr instanceof Step){
			if(mq){
				return "s.rval(\"steps\")";
			}
			else{
				return "steps";
			}
			
		}
		else if(expr instanceof Predicate){
			Predicate pred = (Predicate)expr;
			Feature f = pred.getFeature();
			String fName="";
			if(f instanceof AbstractFeature){
				fName=((AbstractFeature) f).getName();
			}
			else if(f instanceof ConcreteFeature){
				fName=((ConcreteFeature) f).getName();
			} 
			if(mq){
				return "s.rval(\""+pred.getPredicate().getName() +"(" + fName +")\")";
			}
			else{
				return pred.getPredicate().getName() +"(" + fName +")";
			}
			
		}
		else if(expr instanceof RefToQFLanVariable){
			//TODO check
			QFLanVariable var = ((RefToQFLanVariable) expr).getVarqflan();
			String vName = var.getName();
			if(mq){
				return "s.rval(\""+vName +"\")";
			}
			else{
				return vName;
			}
		}
		else if(expr instanceof ReftToQFLanVariableOrFeature){
			//TODO check
			QFLanVariableOrFeature v = ((ReftToQFLanVariableOrFeature) expr).getValue();
			String vName = v.getName();
			if(mq){
				return "s.rval(\""+vName +"\")";
			}
			else{
				return vName;
			}
		}
		/*else if(expr instanceof ParameterValue){
			ParameterValue val = (ParameterValue)expr;
			if(val.getValue()==null || val.getValue().getName()==null){
				return "undef";
			}
			else{
				//System.out.println(val.toString());
				return val.getValue().getName();
			}
		} */
		else if(expr instanceof Addition || expr instanceof AdditionWithPredicates || expr instanceof AdditionWithPredicatesAndFeaturesForQuery){
			if(expr instanceof Addition){
				leftVisited = visitExpr(((Addition)expr).getLeft(),mq);
				rightVisited = visitExpr(((Addition)expr).getRight(),mq);
			}
			else if(expr instanceof AdditionWithPredicates){
				leftVisited = visitExpr(((AdditionWithPredicates)expr).getLeft(),mq);
				rightVisited = visitExpr(((AdditionWithPredicates)expr).getRight(),mq);
			}
			else //if(expr instanceof AdditionWithPredicatesAndFeaturesForQuery)
				{
				leftVisited = visitExpr(((AdditionWithPredicatesAndFeaturesForQuery)expr).getLeft(),mq);
				rightVisited = visitExpr(((AdditionWithPredicatesAndFeaturesForQuery)expr).getRight(),mq);
			}
			leftVisited = addParIfNotTerminal(leftVisited);
			rightVisited = addParIfNotTerminal(rightVisited);
			String ret = leftVisited + " + " + rightVisited;
			return ret;
		}
		else if(expr instanceof Subtraction || expr instanceof SubtractionWithPredicates || expr instanceof SubtractionWithPredicatesAndFeaturesForQuery){
			if(expr instanceof Subtraction){
				leftVisited = visitExpr(((Subtraction)expr).getLeft(),mq);
				rightVisited = visitExpr(((Subtraction)expr).getRight(),mq);
			}
			else if(expr instanceof SubtractionWithPredicates){
				leftVisited = visitExpr(((SubtractionWithPredicates)expr).getLeft(),mq);
				rightVisited = visitExpr(((SubtractionWithPredicates)expr).getRight(),mq);
			}
			else //if(expr instanceof SubtractionWithPredicatesAndFeaturesForQuery)
				{
				leftVisited = visitExpr(((SubtractionWithPredicatesAndFeaturesForQuery)expr).getLeft(),mq);
				rightVisited = visitExpr(((SubtractionWithPredicatesAndFeaturesForQuery)expr).getRight(),mq);
			}
			leftVisited = addParIfNotTerminal(leftVisited);
			rightVisited = addParIfNotTerminal(rightVisited);
			String ret = leftVisited + " - " + rightVisited;
			return ret;
		}
		else if(expr instanceof Multiplication || expr instanceof MultiplicationWithPredicates || expr instanceof MultiplicationWithPredicatesAndFeaturesForQuery){
			if(expr instanceof Multiplication){
				leftVisited = visitExpr(((Multiplication)expr).getLeft(),mq);
				rightVisited = visitExpr(((Multiplication)expr).getRight(),mq);
			}
			else if(expr instanceof MultiplicationWithPredicates){
				leftVisited = visitExpr(((MultiplicationWithPredicates)expr).getLeft(),mq);
				rightVisited = visitExpr(((MultiplicationWithPredicates)expr).getRight(),mq);
			}
			else //if(expr instanceof MultiplicationWithPredicatesAndFeaturesForQuery)
				{
				leftVisited = visitExpr(((MultiplicationWithPredicatesAndFeaturesForQuery)expr).getLeft(),mq);
				rightVisited = visitExpr(((MultiplicationWithPredicatesAndFeaturesForQuery)expr).getRight(),mq);
			}
			leftVisited = addParIfNotTerminal(leftVisited);
			rightVisited = addParIfNotTerminal(rightVisited);
			String ret = leftVisited + " * " + rightVisited;
			return ret;
		}
		/*else if(expr instanceof Division || expr instanceof DivisionWithPredicates){
			if(expr instanceof Division){
				leftVisited = visitExpr(((Division)expr).getLeft());
				rightVisited = visitExpr(((Division)expr).getRight());
			}
			else {//if(expr instanceof DivisionWithPredicates){
				leftVisited = visitExpr(((DivisionWithPredicates)expr).getLeft());
				rightVisited = visitExpr(((DivisionWithPredicates)expr).getRight());
			}
			leftVisited = addParIfNotTerminal(leftVisited);
			rightVisited = addParIfNotTerminal(rightVisited);
			String ret = leftVisited + " / " + rightVisited;
			return ret;
		}
		else if(expr instanceof Power || expr instanceof PowerWithPredicates){
			if(expr instanceof Power){
				leftVisited = visitExpr(((Power)expr).getLeft());
				rightVisited = visitExpr(((Power)expr).getRight());
			}
			else{ //if(expr instanceof PowerWithPredicates) {
				
				leftVisited = visitExpr(((PowerWithPredicates)expr).getLeft());
				rightVisited = visitExpr(((PowerWithPredicates)expr).getRight());
			}
			leftVisited = addParIfNotTerminal(leftVisited);
			rightVisited = addParIfNotTerminal(rightVisited);
			String ret = leftVisited + " ^ " + rightVisited;
			return ret;
		}*/
		else if(expr instanceof MinusPrimary || expr instanceof MinusPrimaryWithPredicates || expr instanceof MinusPrimaryWithPredicatesAndFeaturesForQuery){
			if(expr instanceof MinusPrimary){
				leftVisited = visitExpr(((MinusPrimary)expr).getLeft(),mq);
			}
			else if(expr instanceof MinusPrimaryWithPredicates){
				leftVisited = visitExpr(((MinusPrimaryWithPredicates)expr).getLeft(),mq);
			}
			else //if(expr instanceof MinusPrimaryWithPredicatesAndFeaturesForQuery)
				{
				leftVisited = visitExpr(((MinusPrimaryWithPredicatesAndFeaturesForQuery)expr).getLeft(),mq);
			}
			leftVisited = addParIfNotTerminal(leftVisited);
			String ret = "-"+leftVisited;
			return ret;
		}
		else{
			throw new UnsupportedOperationException("Unsupported expression: " + expr.toString());
		}
	}

/*	private static String getName(Set<String> undefinedSpeciesNames, Species sp) {
		if(sp.getName()==null){
			ICompositeNode node = NodeModelUtils.findActualNodeFor(sp);
			int offset=node.getOffset();
			String text = node.getText();
			text = text.substring(offset-node.getTotalOffset());
			if(undefinedSpeciesNames!=null){
				undefinedSpeciesNames.add(text);
			}
			return text;
		}
		else{
			return sp.getName();
		}
	}*/
	
	private static String addParIfNotTerminal(String expr) {
		boolean add=false;
		String[] operators = new String[]{"+","*","-","/","^",};
		for (String operator : operators) {
			if(expr.contains(operator)){
				add=true;
				break;
			}
		}
		if(add){
			return "("+expr+")";
		}
		else{
			return expr;
		}
	}

	public static String getOnlyAlphaNumericAndDot(String s) {
	    Pattern pattern = Pattern.compile("[^0-9 a-z A-Z _ .]");
	    Matcher matcher = pattern.matcher(s.replace('-', '_'));
	    String number = matcher.replaceAll("");
	    return number;
	 }
	
	/*public static String computeFileName(String fileName, String projectPath) {
		if(!fileName.startsWith(File.separator)){
			if(fileName.startsWith("."+File.separator)){
				fileName=fileName.substring(2);
			}
			fileName = getOnlyAlphaNumericAndDot(fileName);
			fileName=projectPath+File.separator+fileName;
		}
		else{
			int lastSep = fileName.lastIndexOf(File.separator);
			String path = fileName.substring(0,lastSep+1);
			String relativeName = fileName.substring(lastSep);
			fileName=path+getOnlyAlphaNumericAndDot(relativeName);
		}
		return fileName;
	}*/
	
	
	public static String computeFileName(String fileName, String absoluteParentPath) {
		return computeFileName(fileName, absoluteParentPath,false);
	}
	
	public static String computeFileName(String fileName, String absoluteParentPath,boolean computeOnlyAlphaNumeric) {
		File f = new File(fileName);
		if(f.isAbsolute()){
			if(computeOnlyAlphaNumeric){
				int lastSep = fileName.lastIndexOf(File.separator);
				String path = fileName.substring(0,lastSep+1);
				String relativeName = fileName.substring(lastSep);
				fileName=path+getOnlyAlphaNumericAndDot(relativeName);
			}
		}
		else{
			if(fileName.startsWith("./")||fileName.startsWith(".\\")){
				fileName=fileName.substring(2);
			}
			/*while(fileName.startsWith(".."+File.separator)){
				fileName=fileName.substring(3);
				int lastSep = absoluteParentPath.lastIndexOf(File.separator);
				if(lastSep>0){
					absoluteParentPath=absoluteParentPath.substring(0, lastSep);
				}
			}*/
			while(fileName.startsWith("../")){
				fileName=fileName.substring(3);
				int lastSep = absoluteParentPath.lastIndexOf(File.separator);
				if(lastSep>0){
					absoluteParentPath=absoluteParentPath.substring(0, lastSep);
				}
			}
			while(fileName.startsWith("..\\")){
				fileName=fileName.substring(3);
				int lastSep = absoluteParentPath.lastIndexOf(File.separator);
				if(lastSep>0){
					absoluteParentPath=absoluteParentPath.substring(0, lastSep);
				}
			}
			if(computeOnlyAlphaNumeric){
				fileName = getOnlyAlphaNumericAndDot(fileName);
			}
			
			fileName=absoluteParentPath+File.separator+fileName;
		}
		
		return fileName;
		
		
		/*
		 		if(!fileName.startsWith(File.separator)){
			if(fileName.startsWith("."+File.separator)){
				fileName=fileName.substring(2);
			}
			while(fileName.startsWith(".."+File.separator)){
				fileName=fileName.substring(3);
				int lastSep = absoluteParentPath.lastIndexOf(File.separator);
				if(lastSep>0){
					absoluteParentPath=absoluteParentPath.substring(0, lastSep);
				}
			}
			if(computeOnlyAlphaNumeric){
				fileName = getOnlyAlphaNumericAndDot(fileName);
			}
			
			fileName=absoluteParentPath+File.separator+fileName;
		}
		else{
			int lastSep = fileName.lastIndexOf(File.separator);
			String path = fileName.substring(0,lastSep+1);
			String relativeName = fileName.substring(lastSep);
			fileName=path+getOnlyAlphaNumericAndDot(relativeName);
		}
		return fileName;
		 */
	}
	
	public static String visitActionRequires(ActionRequires constraint) {
		//'do' '(' action=SpecialActionOrReferenceToActionOrToFeature ')' '->' constraint=PrimaryBooleanConstraintExpr {ActionRequires.constraint=current}
			//new ActionRequiresConstraint(sell, new DisequationOfPredicateExpressions(priceOfBike, new Constant(250), PredicateExprComparator.GE))
			String visitedAction = visitActionIncludingStoreModifierOrFeature(constraint.getAction());
			String visitedConstraint = visitConstraint(constraint.getConstraint());
			return "Execution of "+visitedAction+" requires "+visitedConstraint;
	}	
	
	public static String  visitActionIncludingAskOrStoreModifierOrFeature(AskOrStoreModifierActionOrReferenceToActionOrToFeature action) {
		if(action instanceof StoreModifierActionOrReferenceToActionOrToFeature){
			return visitActionIncludingStoreModifierOrFeature((StoreModifierActionOrReferenceToActionOrToFeature)action);
		}
		else if(action instanceof AskAction){
				return "ask("+visitConstraint(((AskAction) action).getQuestion())+")";
		}
		return ""; 
	}
	
	public static String  visitActionIncludingStoreModifierOrFeature(StoreModifierActionOrReferenceToActionOrToFeature action) {
		if(action instanceof Action){
			return ((Action) action).getName();
		}
		else if(action instanceof ConcreteFeature){
			return ((Action) action).getName();
		}
		else if(action instanceof ReferenceToActionOrFeature){
			ActionOrFeature referencedActionOrFeautre = ((ReferenceToActionOrFeature) action).getValue();
			if(referencedActionOrFeautre instanceof Action){
				return ((Action) referencedActionOrFeautre).getName();
			}
			else if(referencedActionOrFeautre instanceof ConcreteFeature){
				return ((ConcreteFeature) referencedActionOrFeautre).getName();
			} 
			else if(referencedActionOrFeautre instanceof AbstractFeature){
				return ((AbstractFeature) referencedActionOrFeautre).getName();
			} 
		}
		else if(action instanceof StoreModifierActions){
			/*new InstallAction(gps, true) new InstallAction(gps, false) 
			new AskAction(constraint) 
			new ReplaceAction(ConcreteFeature toRemove, ConcreteFeature toAdd)*/
			if(action instanceof InstallAction){
				return "install "+((InstallAction)action).getFeature().getName();
			}
			else if(action instanceof UninstallAction){
				return "uninstall "+((UninstallAction)action).getFeature().getName();
			}
			else if(action instanceof ReplaceAction){
				return "replace "+((ReplaceAction)action).getToRemove().getName()+" with "+((ReplaceAction)action).getToAdd().getName();
			}
		}
		return "";
	}
	
	public static String visitConstraint(BoolExpr constraint) {
		return visitConstraint(constraint,false);
	}
	
	public static String visitConstraint(BoolExpr constraint,boolean mq) {
		if(constraint instanceof HasFeature){
			 Feature f = ((HasFeature) constraint).getFeature();
			 String fName=null;
			if(f instanceof AbstractFeature){
				fName=((AbstractFeature) f).getName();
			}
			else if(f instanceof ConcreteFeature){
				fName=((ConcreteFeature) f).getName();
			}
			
			if(mq){
				//return "s.rval(\""+((HasFeature)constraint).getFeature().getName()+"\") == 1";
				return "s.rval(\""+fName+"\") == 1";
			}
			else{
				//return "has("+((HasFeature)constraint).getFeature().getName()+")";
				return "has("+fName+")";
			}
		}
		else if(constraint instanceof FalseConstraint || constraint instanceof FalseConditionOfQuery){
			return "false";
		}
		else if(constraint instanceof TrueConstraint || constraint instanceof TrueConditionOfQuery){
			return "true";
		}
		else if(constraint instanceof NotConstraintExpr){
			return "!("+visitConstraint(((NotConstraintExpr) constraint).getLeft(),mq)+")";
		}
		else if(constraint instanceof NotConditionOfQueryExpr){
			return "!("+visitConstraint(((NotConditionOfQueryExpr) constraint).getLeft(),mq)+")";
		}
		else if(constraint instanceof AndBoolConstraintExpr){
			if(mq){
				return "(("+visitConstraint(((AndBoolConstraintExpr) constraint).getLeft(),mq)+") && ("+visitConstraint(((AndBoolConstraintExpr) constraint).getRight(),mq)+"))";
			}
			else{
				return "(("+visitConstraint(((AndBoolConstraintExpr) constraint).getLeft(),mq)+") and ("+visitConstraint(((AndBoolConstraintExpr) constraint).getRight(),mq)+"))";
			}
		}
		else if(constraint instanceof AndBoolConditionOfQueryExpr){
			if(mq){
				return "(("+visitConstraint(((AndBoolConditionOfQueryExpr) constraint).getLeft(),mq)+") && ("+visitConstraint(((AndBoolConditionOfQueryExpr) constraint).getRight(),mq)+"))";
			}
			else{
				return "(("+visitConstraint(((AndBoolConditionOfQueryExpr) constraint).getLeft(),mq)+") and ("+visitConstraint(((AndBoolConditionOfQueryExpr) constraint).getRight(),mq)+"))";
			}
		}
		else if(constraint instanceof OrBoolConstraintExpr){
			if(mq){
				return "(("+visitConstraint(((OrBoolConstraintExpr) constraint).getLeft(),mq)+") || ("+visitConstraint(((OrBoolConstraintExpr) constraint).getRight(),mq)+"))";
			}
			else{
				return "(("+visitConstraint(((OrBoolConstraintExpr) constraint).getLeft(),mq)+") or ("+visitConstraint(((OrBoolConstraintExpr) constraint).getRight(),mq)+"))";
			}
		}
		else if(constraint instanceof OrBoolConditionOfQueryExpr){
			if(mq){
				return "(("+visitConstraint(((OrBoolConditionOfQueryExpr) constraint).getLeft(),mq)+") || ("+visitConstraint(((OrBoolConditionOfQueryExpr) constraint).getRight(),mq)+"))";
			}
			else{
				return "(("+visitConstraint(((OrBoolConditionOfQueryExpr) constraint).getLeft(),mq)+") or ("+visitConstraint(((OrBoolConditionOfQueryExpr) constraint).getRight(),mq)+"))";
			}
		}
		else if(constraint instanceof ImpliesBoolConstraintExpr){
			if(mq){
				return "((!("+visitConstraint(((ImpliesBoolConstraintExpr) constraint).getLeft(),mq)+")) || ("+visitConstraint(((ImpliesBoolConstraintExpr) constraint).getRight(),mq)+"))";
			}
			else{
				return "(("+visitConstraint(((ImpliesBoolConstraintExpr) constraint).getLeft(),mq)+") implies ("+visitConstraint(((ImpliesBoolConstraintExpr) constraint).getRight(),mq)+"))";
			}
			
		}
		else if(constraint instanceof ImpliesBoolConditionOfQueryExpr){
			if(mq){
				return "((!("+visitConstraint(((ImpliesBoolConditionOfQueryExpr) constraint).getLeft(),mq)+")) || ("+visitConstraint(((ImpliesBoolConditionOfQueryExpr) constraint).getRight(),mq)+"))";
			}
			else{
				return "(("+visitConstraint(((ImpliesBoolConditionOfQueryExpr) constraint).getLeft(),mq)+") implies ("+visitConstraint(((ImpliesBoolConditionOfQueryExpr) constraint).getRight(),mq)+"))";			}
			
		}
		else if(constraint instanceof DisequationOfPredicateExpr){
			String visitedlhs = visitExpr(((DisequationOfPredicateExpr) constraint).getLhs(),mq);
			String visitedrhs = visitExpr(((DisequationOfPredicateExpr) constraint).getRhs(),mq);
			String comp = ((DisequationOfPredicateExpr) constraint).getComp();
			return "("+visitedlhs+" "+comp+" "+visitedrhs+")";
		}
		else if(constraint instanceof DisequationOfPredicateExprForConditionOfQuery){
			String visitedlhs = visitExpr(((DisequationOfPredicateExprForConditionOfQuery) constraint).getLhs(),mq);
			String visitedrhs = visitExpr(((DisequationOfPredicateExprForConditionOfQuery) constraint).getRhs(),mq);
			String comp = ((DisequationOfPredicateExprForConditionOfQuery) constraint).getComp();
			return "("+visitedlhs+" "+comp+" "+visitedrhs+")";
		}
		
		
		return "";
	}
	
	public static String  visitProcess(ProcessExpr process) {
		//def visitProcess(ProcessExpr process) {
			//choice, parallel, sequential, prefix, ZeroProcess, ReferenceToProcessDefinition
			if(process instanceof ZeroProcess){
				return "nil";
			}
			else if(process instanceof Prefix){
				Prefix prefix = (Prefix)process;
				return 
						"("+visitActionIncludingAskOrStoreModifierOrFeature(prefix.getAction())+","+
						visitExpr(prefix.getRate())+","+
						visitListOfSideEffects(prefix.getSideEffects())+")."+
						visitProcess(prefix.getContinuation());
			}
			else if(process instanceof ReferenceToProcessDefinition){
				return ((ReferenceToProcessDefinition) process).getValue().getName();
			}
			else if(process instanceof Choice){
				return "(("+visitProcess(process.getFirst())+") + ("+visitProcess(((Choice) process).getSecond())+"))";
			}
			else if(process instanceof Sequential){
				return "(("+visitProcess(process.getFirst())+") ; ("+visitProcess(((Choice) process).getSecond())+"))";
			}
			else if(process instanceof Parallel){
				return "(("+visitProcess(process.getFirst())+") | ("+visitProcess(((Choice) process).getSecond())+"))";
			}
			else if(process instanceof ProcessExpr){
				return visitProcess(process.getFirst());
			}
			return "";
		}
		
	public static String  visitListOfSideEffects(SideEffects effects) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			
			if(effects!=null){
				EList<it.imt.qFLan.SideEffect> listOfEffects = effects.getEffects();
				int size = listOfEffects.size();
				int i=0;
				if(listOfEffects!=null){
					for(it.imt.qFLan.SideEffect eff : listOfEffects){
						String name = eff.getRefToQFLanVar().getVarqflan().getName();
						Expression expr = eff.getValue();
						sb.append(" "+name+" = "+MyParserUtil.visitExpr(expr));
						if(i<size-1){
							sb.append(",");
						}
						i++;
					}
				}
			}
			sb.append("}");
			return sb.toString();
		}
	
	
	public static void generateFigure(InputStream dot, String fileName, Resource resource) throws IOException {
		URI rur = resource.getURI();
		URI figure = rur.trimFileExtension();
		figure = figure.trimSegments(figure.segmentCount()-2);
		figure =figure.appendSegment(fileName);
		//figure =figure.appendFileExtension("svg");
		figure =figure.appendFileExtension("png");
		
		URIConverter uc = resource.getResourceSet().getURIConverter();
		OutputStream os = uc.createOutputStream(figure);
		MutableGraph loadedDot = new Parser().read(dot);
		//Graphviz.fromGraph(loadedDot).width(700).render(Format.SVG).toOutputStream(os);
		//Graphviz.fromGraph(loadedDot).render(Format.SVG).toOutputStream(os);
		Graphviz.fromGraph(loadedDot).render(Format.PNG).toOutputStream(os);
		os.close();
		dot.close();
		
		//throw new UnsupportedOperationException("TODO: auto-generated method stub");
//		try {
//			TestGraphvizJava.main(null);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//System.out.println("Hello World!");
        /*
		
        MutableGraph g = mutGraph("example1").setDirected(true).add(
                mutNode("a").add(Color.RED).addLink(mutNode("b")));
        try {
			Graphviz.fromGraph(g).width(200).render(Format.PNG).toFile(new File("example/ex1m.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Completed g1");
        */

		//App.main(null);
		/*
		GraphParser parser = new GraphParser(dot);
		Map<String, GraphNode> nodes = parser.getNodes();
		Map<String, GraphEdge> edges = parser.getEdges();	

		System.out.println("--- nodes:");
		for (GraphNode node : nodes.values()) {
			System.out.println(node.getId() + " " + node.getAttributes());
		}

		System.out.println("--- edges:");
		for (GraphEdge edge : edges.values()) {
			System.out.println(edge.getNode1().getId() + "->" + edge.getNode2().getId() + " " + edge.getAttributes());
		}
		*/
		
		
//		String rurp = rur.path();
//		String rurpd = rur.devicePath();
//		String rurh = rur.host();
//		String rurfs =rur.toFileString();
//		String rurpst = rur.toPlatformString(true);
//		String rurpsf = rur.toPlatformString(true);
//		String s = URI.decode(fileName);
//		
		
		
		
			/*	var figure = rur.trimFileExtension.appendFileExtension('png')//tree
				//var fileFigure = new File
				fsa.generateFile(fileName, AttackTree)
				
				var a = resource.URI.trimFileExtension.toPlatformString(true) 
				var fi = new File(modelName+"Tree.png");
				var ur =fi.toURI
				var uc = resource.resourceSet.URIConverter;
				//var fs =uc.normalize(ur);
				//uc.normalize(resource.URI.trimFileExtension.appendFileExtension('gen')).toFileString
				//var ur2 = resource.resourceSet.UriConverter.normalize( resource.URI.trimFileExtension.appendFileExtension('gen') ).toFileString
*/
//		try {
//			//dot.reset();
//			
//			Graphviz.fromGraph(g3).width(700).render(Format.PNG).toFile(new File("/Users/andrea/Desktop/"+fileName+".png"));
//			Graphviz.fromGraph(g3).width(700).render(Format.PNG).toOutputStream(os);
///*
//			g3.graphAttrs()
//			.add(Color.WHITE.gradient(Color.rgb("888888")).background().angle(90))
//			.nodeAttrs().add(Color.WHITE.fill())
//			.nodes().forEach(node ->
//			node.add(
//					Color.named(node.name().toString()),
//					Style.lineWidth(4).and(Style.FILLED)));
//			Graphviz.fromGraph(g3).render(Format.PNG).toFile(new File("example/ex4-2.png"));
//			System.out.println("Completed g3");
//			Graphviz.fromGraph(g3).width(700).render(Format.PNG).toFile(new File("example/ex4-3.png"));
//			System.out.println("Completed g4");*/
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}