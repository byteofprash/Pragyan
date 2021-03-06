package nlpart;

import java.util.*;
import java.util.Comparator;

public class LexiconLiteral implements Comparable<LexiconLiteral>
{
	public List<String> typeOfOwner;
	// public List<String> domains;
	// public List<String> ranges;
	// public String type;
	public String URI;
	public String label;
	public String QuestionMatch;
	// public String identifier ;
	public int score;

	public LexiconLiteral() {

		typeOfOwner = new ArrayList<String>();
	}

	public LexiconLiteral(String URI, String label, String QuestionMatch, List<String> typeOfOwnerList) {
		this.URI = URI;
		this.label = label;
		this.QuestionMatch = QuestionMatch;
		this.typeOfOwner = typeOfOwnerList;
		System.out.println("=====================\nI have added Current Values:literalURI => " + URI
				+ "\nliteralLabel => " + label + "\nQuestionmatch => " + QuestionMatch + "\nType of owner =>"
				+ typeOfOwner + "\n=====================");
	}

	public LexiconLiteral(String URI, String label, String QuestionMatch, String typeOfOwner) {
		this.URI = URI;
		this.label = label;
		this.QuestionMatch = QuestionMatch;
		List<String> typeOfOwnerList = new ArrayList<String>();
		typeOfOwnerList.add(typeOfOwner);
		this.typeOfOwner = typeOfOwnerList;
		System.out.println("=====================\nI have added Current Values:literalURI => " + URI
				+ "\nliteralLabel => " + label + "\nQuestionmatch => " + QuestionMatch + "\nType of owner =>"
				+ typeOfOwner + "\n=====================");
	}

	public int compareTo(LexiconLiteral lexlit) {
		int score2 = ((LexiconLiteral) lexlit).score;
		// ascending order
		return this.score - score2;
		// descending order
		// return compareQuantity - this.quantity;

	}

	public static Comparator<LexiconLiteral> scoreComparator = new Comparator<LexiconLiteral>() {

		public int compare(LexiconLiteral lexlit1, LexiconLiteral lexlit2) {
			int score1 = lexlit1.score;
			int score2 = lexlit2.score;
			// ascending order
			return lexlit1.compareTo(lexlit2);
		}

	};

}
