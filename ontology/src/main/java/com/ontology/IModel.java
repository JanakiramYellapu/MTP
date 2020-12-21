package com.ontology;

import org.apache.jena.rdf.model.Model;

public interface IModel {
	void createDataModel();
	Model intiateInference();
	String query(int queryNumber, Model model);
}
