package com.ontology;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;


public class SurvOnt implements IModel{
	private Model schema, data, dataModel;
	private String namespace;
	private String schemaFileName, dataFileName;
	public  SurvOnt(String schemaFileName, String dataFileName, String namespace, List<String> queries) {
		this.schemaFileName = schemaFileName;
		this.dataFileName = dataFileName;
    	this.namespace = namespace;
    	this.dataModel = ModelFactory.createDefaultModel();
	}
	
	
	
	@Override
	public void loadSchema() {
		this.schema = RDFDataMgr.loadModel(this.schemaFileName);
	}
	
	@Override
	public void createDataModel() {
		
		ArrayList<ArrayList<String>> statements = addStatements();
		this.dataModel.setNsPrefix("imprint", this.namespace );
		
		System.out.println("Adding statements into data model...");
		for(ArrayList<String> statement : statements) {
			String subject = statement.get(0);
			String predicate = statement.get(1);
			String object = statement.get(2);
		 
			Resource subjectRes = createResource(subject);
			Property predicateProp = this.schema.getProperty(this.namespace +predicate);
			RDFNode objectRes = createResource(object);
			
			this.dataModel.add(subjectRes, predicateProp, objectRes );
			
			System.out.println("Added " +  "\"" + subject + " " + predicate + " " + object + "\"" + " into data model successfully.");
		
		}
		
		try {
			System.out.println("Saving data model...");
			FileWriter out = new FileWriter(this.dataFileName);
			this.dataModel.write(out , "RDF/XML");
			out.close();
			this.data   = RDFDataMgr.loadModel(this.dataFileName);
			
		} catch (IOException e) {
			System.out.println("Failed to load the data model.");
		}
		
		
	}
	
	private ArrayList<ArrayList<String>> addStatements(){
		ArrayList<ArrayList<String>> statements = new ArrayList<ArrayList<String>>();
		// space 1
//		statements.add(new ArrayList<String>(Arrays.asList("space1", "containsBed", "bed1")));
//		statements.add(new ArrayList<String>(Arrays.asList("space1", "containsTable", "table1")));
//		statements.add(new ArrayList<String>(Arrays.asList("space1", "containsPillow", "pillow1")));
//		statements.add(new ArrayList<String>(Arrays.asList("space1", "containsPillow", "pillow2")));
//		statements.add(new ArrayList<String>(Arrays.asList("space1", "containsFloor", "floor1")));
//		statements.add(new ArrayList<String>(Arrays.asList("bed1", "on", "floor1")));
//		statements.add(new ArrayList<String>(Arrays.asList("table1", "on", "floor1")));
//		statements.add(new ArrayList<String>(Arrays.asList("bed1", "hasDistance", "102")));
//		statements.add(new ArrayList<String>(Arrays.asList("table1", "hasDistance", "172")));
//		statements.add(new ArrayList<String>(Arrays.asList("pillow1", "hasDistance", "156")));
//		statements.add(new ArrayList<String>(Arrays.asList("pillow2", "hasDistance", "171")));
		
		//space 2
//		statements.add(new ArrayList<String>(Arrays.asList("space2", "containsPlant", "plant1")));
//		statements.add(new ArrayList<String>(Arrays.asList("space2", "containsChair", "chair1")));
//		statements.add(new ArrayList<String>(Arrays.asList("space2", "containsChair", "chair2")));
//		statements.add(new ArrayList<String>(Arrays.asList("space2", "containsFloor", "floor2")));
//		statements.add(new ArrayList<String>(Arrays.asList("space2", "containsWall", "wall2")));
//		statements.add(new ArrayList<String>(Arrays.asList("plant1", "on", "floor2")));
//		statements.add(new ArrayList<String>(Arrays.asList("chair1", "on", "floor2")));
//		statements.add(new ArrayList<String>(Arrays.asList("chair2", "on", "floor2")));
//		statements.add(new ArrayList<String>(Arrays.asList("chair1", "hasDistance", "49")));
//		statements.add(new ArrayList<String>(Arrays.asList("chair2", "hasDistance", "100")));
//		statements.add(new ArrayList<String>(Arrays.asList("plant1", "hasDistance", "216")));
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter("/home/janakiram/IITM/Project/Jena/ontology/Files/data.txt"))){
//			for(ArrayList<String> statement : statements) {
//				String subject = statement.get(0);
//				String predicate = statement.get(1);
//				String object = statement.get(2);
//				writer.write(subject + "," + predicate + "," + object + "\n");
//			}
//			
//		}
//		catch(IOException e){
//			System.out.println("Unable to write to data.txt file.");
//		}
		System.out.println("Reading from file");
		try(BufferedReader reader  = new BufferedReader(new FileReader("/home/janakiram/IITM/Project/Jena/ontology/Files/data.txt"))){
			String input;
			while((input = reader.readLine()) != null) {
				String[] data = input.split(",");
				System.out.println(data[0] + "--->" + data[1] + "-->" + data[2]);
				statements.add(new ArrayList<String> (Arrays.asList(data[0], data[1], data[2])));
			}
		}
		catch(IOException e) {
			System.out.println("Unablae to read from data.txt file.");
		}
		return statements;
	}
	private Resource createResource(String name) {
		return this.dataModel.createResource(this.namespace + name);
	}
	
	
	private Property createProperty(String name) {
		return this.dataModel.createProperty(this.namespace + name);
	}
	
	
	private Literal createLiteral(String name) {
		return this.dataModel.createLiteral(name);
	}
	

	@Override
	public Model intiateInference() {
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(this.schema);
    	InfModel infModel = ModelFactory.createInfModel(reasoner, this.data);
		return infModel;
	}

	@Override
	public String query(int queryNumber, QueryGenerator queryGenerator, Model model) {
		List <String> queries = queryGenerator.getQueries(); 
		String queryString = queries.get(queryNumber);
		
    	Query query = QueryFactory.create(queryString);
    	QueryExecution qexec = QueryExecutionFactory.create(query, model);
    	
    	ResultSet results =  qexec.execSelect();
    	String result = "";
    	
    	try {
			while(results.hasNext() ) {
				QuerySolution soln =  results.nextSolution();
				RDFNode spaceName = soln.get("space");
				RDFNode objectName = soln.get("object");
				result += spaceName + " -----> " + objectName + "\n" ;
			}
		} 
    	finally {
    		qexec.close();
    	}
		return result;
	}

	
}
