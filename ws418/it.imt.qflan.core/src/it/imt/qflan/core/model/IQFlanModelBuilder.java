package it.imt.qflan.core.model;

import com.microsoft.z3.Z3Exception;

public interface IQFlanModelBuilder {

	QFlanModel createModel() throws Z3Exception;
	
}
