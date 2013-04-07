package nlpart;

import java.util.*;
import java.util.logging.Level;

import org.apache.log4j.pattern.LiteralPatternConverter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class answerGenerator
{

	private String parsedQuestion;
	private String question;
	private int questionType;
	private Lexicon lexicon;

	public answerGenerator() {
		lexicon = new Lexicon();
	}

	/*
	 * Before generating the queries we need to sanitize the queries. Remove the
	 * question mark, other symbols and other stuff
	 */

	public void generateQueries(String question) throws Exception {

		this.question = question.toLowerCase();
		String tmplog = "";
		// Sanitize the question.

		sanitizeQuery();
		//System.out.println("Sanitized question: "+parsedQuestion);
		Util.writeToLog(Level.INFO, "Sanitized question: "+parsedQuestion);
		List<String> queries = buildQueries();
		System.out.println("\n\n\n\n\n********************The answer to the Life, Universe and Everything :***************************** ");
		for (String query : queries)
		{
			//System.out.println("The query is: "+query);
			tmplog+="The query is: "+query+"\r\n\r\n";
			try
			{
				Query queryObj = QueryFactory.create(query);
				String sparqlEndpoint = "http://dbpedia.org/sparql";
				QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryObj);
				ResultSet answersList = qe.execSelect();
				while (answersList.hasNext())
				{
					QuerySolution answer = answersList.nextSolution();
					RDFNode ans = answer.get("x");
					if(ans!=null)
					{
					System.out.println("The query is: "+query);
					System.out.println("\n**************************\n");
					System.out.println("The answer is : "+ans.toString());
					System.out.println("\n**************************\n");
					tmplog+="The answer is: "+ans.toString()+"\r\n****************************************************\r\n\r\n";
					}
				}
			}
			catch (Exception e)
			{
				System.out.println("Houston, we have a problem! " + e.getMessage());
			}
		}
		Util.writeToLog(Level.INFO,"Queries and their result \r\n"+tmplog);

	}

	public void sanitizeQuery() {
		parsedQuestion = question.replace("?", "");
		parsedQuestion = parsedQuestion.replace(",", "");
		parsedQuestion = parsedQuestion.replace(";", "");
		parsedQuestion = parsedQuestion.replace("?", "");
		parsedQuestion = parsedQuestion.replace("]", "");
		parsedQuestion = parsedQuestion.replace("[", "");
		parsedQuestion = parsedQuestion.replace(")", "");
		parsedQuestion = parsedQuestion.replace("(", "");
		parsedQuestion = parsedQuestion.replace("  ", " ");
	}

	/*
	 * THIS IS THE MOST IMPORTANT CLASS: 1. Takes the sanitized question. 2.
	 * Gets the predicate and the literal for that.
	 */
	
	public List<String> buildQueries() throws Exception {
		
		// Finding the type of the question
		List<String> questionAndType = Util.getQuestionType(parsedQuestion);
		List<String> queries = new ArrayList<String>();
		
		// Get the predicates of the question
		List<LexiconPredicate> predicateList = lexicon.getPredicates(parsedQuestion, 50, 20); // find
																							 // all
																							 // matching
																							 // predicates
		List<LexiconLiteral> literalList = lexicon.getLiterals(parsedQuestion, 50, 20);
		String query = "";
		for (LexiconPredicate lexiconPredicate : predicateList)
		{
			for (LexiconLiteral lexiconLiteral : literalList)
			{
				//making sure literal and predicate aren't from the same permutation
				if(!lexiconLiteral.QuestionMatch.equals(lexiconPredicate.QuestionMatch))
				{
				query = "Select ?x ?label where{ <" + lexiconLiteral.URI + "> <" + lexiconPredicate.URI + "> ?x . "
						+ "OPTIONAL { ?x <http://www.w3.org/2000/01/rdf-schema#label> ?label }FILTER ( lang(?x) = 'en' )}";
				queries.add(query);
				}
			}

		}
		return queries;
	}
}
