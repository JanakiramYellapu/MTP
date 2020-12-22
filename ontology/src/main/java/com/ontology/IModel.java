package com.ontology;

import org.apache.jena.rdf.model.Model;

public interface IModel {
	void loadSchema();
	void createDataModel();
	Model intiateInference();
	String query(int queryNumber, QueryGenerator queryGenerator, Model model);
}
