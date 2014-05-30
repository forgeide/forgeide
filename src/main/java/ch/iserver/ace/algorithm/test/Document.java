package ch.iserver.ace.algorithm.test;

import ch.iserver.ace.algorithm.Operation;

public interface Document {
	
	void apply(Operation op);
	
	String getContent();
	
}
