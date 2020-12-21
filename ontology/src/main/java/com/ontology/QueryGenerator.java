package com.ontology;

import java.util.ArrayList;
import java.util.List;

public class QueryGenerator {
	private String prefix;
	
	//Fetch all objects.
	final String query1 =  "SELECT  ?space ?object  WHERE {"
			+ "?space imprint:containsObject ?object ."
			+ "}"
			+ "ORDER BY ?space ?object";
	
	//Fetch objects on floor.
	final String query2 =  "SELECT  ?space ?object  WHERE {"
			+ "?space imprint:containsFloor ?floor ."
			+ "?space imprint:containsObject ?object ."
			+ "?floor imprint:on ?object."
			+ "}"
			+ "ORDER BY ?space ?object";
	
	//Nearest object in space1.
	final String query3 =  "SELECT ?depth ?object  WHERE {"
			+ "imprint:space1 imprint:containsObject ?object ."
			+ "?object imprint:hasVIP ?depth."
			+ "}"
			+ "ORDER BY ?depth";
//			+ "LIMIT 1";
	
	//Farthest object in space2.
	final String query4 =  "SELECT ?object ?depth WHERE {"
			+ "imprint:space2 imprint:containsObject ?object ."
			+ "?object imprint:hasVIP ?depth."
			+ "}"
			+ "ORDER BY DESC(?depth)";

	
	private List <String> queries;
	private List <String> descriptions;
	public QueryGenerator(String prefix) {
		this.prefix = prefix;
		this.queries = new ArrayList<String>();
		this.descriptions = new ArrayList<String>();
		System.out.println("Adding Queries...");
		this.addQuery(this.query1, "Fetch all objects.");
		this.addQuery(this.query2, "Fetch objects on floor.");
		this.addQuery(this.query3, "Nearest object in space1.");
		this.addQuery(this.query4, "Farthest object in space2.");
	}
	
	private void addQuery(String query, String description) {
		this.queries.add(this.prefix + query);
		this.descriptions.add(description);
		System.out.println("\"" + description + "\"" + " ------> query added successfully.");
	}

	public List<String> getQueries() {
		return queries;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}
	
	public void  printQuery(List <String> data) {
		for(int i = 0; i < data.size(); i++) {
			System.out.println("Query" + (i+1) + " is : ");
			System.out.println(data.get(i));
			System.out.println();
		}
	}
	
	
}
