package exp.syn;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import exp.lex.Lexeme;
import exp.lex.Term;

public final class ParseTableEntry {
	private final Hashtable<String, Production> table = new Hashtable<String, Production>();

	private final List<Term> firstTermsList = new ArrayList<Term>();

	private Term[] firstTerms;

	public Production getProduction(Lexeme lexeme) throws SyntaxException {
		final Production production = table.get(lexeme.getType());
		if (production == null)
			throw new SyntaxException("cant select production by lexeme "
					+ lexeme.getType());
		return production;
	}

	public void addDecisionRule(Term term, Production production) {
		assert term != null;
		assert production != null;
		table.put(term.getName(), production);
		firstTermsList.add(term);
	}

	public Term[] getFirstTerms() {
		if (firstTerms == null)
			firstTerms = firstTermsList.toArray(new Term[firstTermsList.size()]);
		return firstTerms;
	}

}
