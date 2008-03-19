package exp.lex.loader;

import java.util.Hashtable;

import exp.lex.LexerException;
import exp.lex.Term;

public class Lexicon {
	private final Hashtable<String, Term> terms = new Hashtable<String, Term>();

	public Lexicon(Iterable<Term> terms) {
		super();
		for (Term term : terms)
			this.terms.put(term.getName(), term);
	}

	public Term getTerm(String name) throws LexerException {
		Term result = terms.get(name);
		if (result == null)
			throw new LexerException("Не найден терминал " + name);
		return result;
	}

	public boolean hasTerm(String name) {
		return terms.containsKey(name);
	}
}
