package it.imt.qflan.core.processes.interfaces;

import java.util.Collection;

public interface IProcess {

	public Collection<ICommitment> getCommitments();
	
	public int getNumberOfProcessesCommittments();

	public void startGettingCommitments();

	public boolean hasCommitments();

	public ICommitment next();
	
	public String getName();
	
}
