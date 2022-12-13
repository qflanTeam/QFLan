package it.imt.qflan.core.processes;

import java.util.Collection;

import it.imt.qflan.core.processes.interfaces.ICommitment;
import it.imt.qflan.core.processes.interfaces.IProcess;

public class ProcessDefinition implements IProcess {

	private String name;
	private IProcess body;
	private Collection<ICommitment> commitments;
	private int realNumberOfProcesses=-1;
	
	//public static final IProcess = new 
	
	public ProcessDefinition(String univoqueName) {
		super();
		this.name = univoqueName;
	}
	
	@Override
	public String toString() {
		return name;
	}
	@Override
	public String getName() {
		return name;
	}
	
	
	public void setBody(IProcess body){
		if(this.body!=null){
			throw new UnsupportedOperationException("The body of a process definition is assumed to e unmutable");
		}
		this.body=body;
	}
	
	public String toStringLong() {
		return name+" := "+body;
	}
	

	@Override
	public Collection<ICommitment> getCommitments() {
		if(commitments==null){
			computeCommitments();
		}
		return commitments;
	}

	private void computeCommitments() {
		commitments=body.getCommitments();
	}

	@Override
	public int getNumberOfProcessesCommittments() {
		if(realNumberOfProcesses==-1){
			computeRealNumberOfProcesses();
		}
		return realNumberOfProcesses;
	}

	private void computeRealNumberOfProcesses() {
		realNumberOfProcesses=body.getNumberOfProcessesCommittments();
	}

	@Override
	public void startGettingCommitments() {
		body.startGettingCommitments();
	}

	@Override
	public boolean hasCommitments() {
		return body.hasCommitments();
	}

	@Override
	public ICommitment next() {
		return body.next();
	}
	
}
