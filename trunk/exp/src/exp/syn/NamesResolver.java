package exp.syn;

import exp.lex.LexerException;
import exp.lex.Term;

public interface NamesResolver {
	public Nonterm getNonterm(String name) throws SyntaxException;
	public Term getTerm(String name) throws LexerException;
}
