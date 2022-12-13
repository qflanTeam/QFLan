package it.imt.qflan.core.processes.actions;

import com.microsoft.z3.Z3Exception;

import it.imt.qflan.core.model.QFlanModel;
import it.imt.qflan.core.processes.interfaces.IConstraint;

public class AskAction extends ActionCommonCode {

	private IConstraint question;
	
	public AskAction(IConstraint question) {
		super("ask("+question+")");
		this.question=question;
	}	
	
	@Override
	public boolean isAllowedByOtherConstraints(QFlanModel model) throws Z3Exception {
		//return model.checkSATAsk(question);
		//I commented this out, because we check if the question is valid before the application of the side-effects. 
		//return model.checkAskNoZ3(this);
		//return true;
		
		//I need to check if the application of the side effects made the store false
		return model.checkOtherConstraints(this);
	}
	
	@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception {
		/*
		//Here I check if some action constraint forbids me from executing the ask.
		//Then, I check if the question is valid in the current store (the current values of the variable).
		boolean sat = model.checkExecuteNormalActionOrFeatureNoZ3(this);
		if(!sat){
			return false;
		}
		return model.checkAskNoZ3(this);
		*/
		//I don't have to check if same action constraint forbids me from executing the ask because I forbid do(ask(c1)) -> c2
		//I only check if the question is valid in the current store (the current values of the variable).
		return model.checkAskNoZ3(this);
	}
	
	/*@Override
	public boolean isAllowedByActionConstraints(QFlanModel model) throws Z3Exception {
		//return model.checkSATAsk(question);
		return model.checkAskNoZ3(this);
	}*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AskAction other = (AskAction) obj;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		return true;
	}

	public IConstraint getQuestion(){
		return question;
	}
	
	
	

}
