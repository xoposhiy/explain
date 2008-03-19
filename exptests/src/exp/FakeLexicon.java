package exp;

import java.util.ArrayList;

import exp.lex.DFA;
import exp.lex.LexerException;
import exp.lex.Term;
import exp.lex.loader.Lexicon;

public class FakeLexicon extends Lexicon {
	public FakeLexicon() {
		super(new ArrayList<Term>());
	}

	@Override
	public Term getTerm(String name) throws LexerException {
		return new Term(name, DFA.forWord(name));
	}

	@Override
	public boolean hasTerm(String name) {
		return true;
	}
	

}
