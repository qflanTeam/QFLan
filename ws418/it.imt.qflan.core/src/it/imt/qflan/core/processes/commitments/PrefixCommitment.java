package it.imt.qflan.core.processes.commitments;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IAction;
import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;
import it.imt.qflan.core.variables.SideEffect;

public class PrefixCommitment implements ICommitment {

	private IAction action;
	private double rate;
	private IProcess continuation;
	private SideEffect[] sideEffects;

	public PrefixCommitment(IAction action, double rate, SideEffect[] sideEffects, IProcess continuation) {
		this.action=action;
		this.rate=rate;
		this.sideEffects=sideEffects;
		this.continuation=continuation;
	}

	@Override
	public String toString() {
		//return "<"+action+","+rate+","+SideEffect.sideEffectsToString(sideEffects)+","+continuation+">";
		//return "--("+action+","+rate+","+SideEffect.sideEffectsToString(sideEffects)+")->"+continuation;
		if(sideEffects==null || sideEffects.length==0){
			//return "<execute '"+action+"' with rate "+rate+ ", becoming "+continuation+">";
			return "< '"+action+"' with rate "+rate+ ", becoming "+continuation+" >";
		}
		else{
			return "< '"+action+"' with rate "+rate+ " and side-effects "+SideEffect.sideEffectsToString(sideEffects)+", becoming "+continuation+" >";
		}
		
	}

	/*@Override
	public boolean isAllowed(Set<IConstraint> constraints, Set<ConcreteFeature> installedFeatures) {
		return action.isAllowed(constraints,installedFeatures);
	}*/
	
	public boolean isAllowed(QFlanModel model) throws Z3Exception {
		//action.isAllowedByActionconstraints(model);
		
		//I first check if the action is allowed by the action constraints.
		boolean alloewdByActionConstraints=this.getAction().isAllowedByActionConstraints(model);
		if(!alloewdByActionConstraints){
			return false;
		}
		
		//then I check if the side-effects (or install/uninstall/replace) lead me to a false store
		//...
		
		if(action.modifiesStore() || (sideEffects!=null && sideEffects.length>0)){
			//apply side effects
			double[] oldValues=model.applySideEffects(sideEffects);
			boolean allowed=action.isAllowedByOtherConstraints(model);
			model.revertSideEffects(sideEffects, oldValues);
			//revert side effects
			if(!allowed){
				return false;
			}
		}
		/*for (int i = 0; i < sideEffects.length; i++) {
			allowed = sideEffects[i].isAllowed(model);
			if(!allowed){
				return false;
			}
		}*/
		return true;
	}

	/*private double[] applySideEffects(QFlanModel model) {
		double[] oldValues=null;
		if(sideEffects!=null&&sideEffects.length>0){
			oldValues = new double[sideEffects.length];
			double[] newValues = new double[sideEffects.length];
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				oldValues[i]=var.eval(null);
				String expr = sideEffects[i].getNewValue();
				newValues[i] = model.computeExpressionValue(expr);
			}
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				model.updateVariableValue(var, newValues[i]);
			}
		}
		return oldValues;
	}
	
	private void revertSideEffects(QFlanModel model,double[] oldValues){
		if(sideEffects!=null){
			for (int i = 0; i < sideEffects.length; i++) {
				QFLanVariable var = sideEffects[i].getVariable();
				model.updateVariableValue(var, oldValues[i]);
			}
		}
	}*/
	

	/*@Override
	public double getRealRate() {
		return getActionRate();
	}*/

	@Override
	public double getActionRate() {
		return rate;
	}
	
	@Override
	public IAction getAction() {
		return this.action;
	}
	
	@Override
	public IProcess getContinuation() {
		return this.continuation;
	}

	@Override
	public boolean modifiesStore() {
		return action.modifiesStore();
	}

	@Override
	public SideEffect[] getSideEffects() {
		return sideEffects;
	}
	
}
