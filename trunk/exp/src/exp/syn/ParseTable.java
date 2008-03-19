package exp.syn;

import java.util.Hashtable;

import exp.lex.Term;

@SuppressWarnings("serial")
public class ParseTable extends Hashtable<Nonterm, ParseTableEntry> {

	public ParseTable(Grammar grammar) throws GrammarException {
		super();
		new ParseTableFiller(grammar.getProductions()).fill(this);
	}

	public void addDecisionRules(Iterable<Term> terms, Production production) {
		if (terms == null)
			throw new IllegalArgumentException("terms = null");
		if (production == null)
			throw new IllegalArgumentException("production = null");
		ParseTableEntry entry = get(production.getHead());
		if (entry == null){
			entry = new ParseTableEntry();
			put(production.getHead(), entry);
		}
		for (Term term : terms)
			entry.addDecisionRule(term, production);
	}

}
