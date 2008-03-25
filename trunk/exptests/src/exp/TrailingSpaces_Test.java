package exp;

import exp.lex.DFA;
import exp.lex.Term;
import exp.syn.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

public class TrailingSpaces_Test extends TestCase
{
	public void test() throws GrammarException, SyntaxException
	{
		GrammarAsserter ga = new GrammarAsserter(createBracketGrammar());
		ga.assertAccepts("()");
		ga.assertAccepts("");
		ga.assertAccepts("\n");
		ga.assertAccepts("([ ])  \t\n ");
		ga.assertRejects("(((]] \n");
		ga.assertRejects("abc   ");
	}

	private Grammar createBracketGrammar() throws GrammarException
	{
		Term ob = new Term("OpenBracket", DFA.forWord("("));
		Term cb = new Term("CloseBracket", DFA.forWord(")"));
		Term osb = new Term("OpenSquareBracket", DFA.forWord("["));
		Term csb = new Term("CloseSquareBracket", DFA.forWord("]"));
		Nonterm S = new Nonterm("S");

		final Collection<Production> prods = new ArrayList<Production>();
		addProd(prods, S, new Symbol[] { osb, S, csb });
		addProd(prods, S, new Symbol[] { ob, S, cb });
		addEmptyProd(prods, S);

		return new Grammar(S, prods);
	}

	private void addEmptyProd(Collection<Production> prods, Nonterm head)
	{
		addProd(prods, head, new Symbol[0]);
	}

	private void addProd(Collection<Production> prods, Nonterm head, Symbol[] symbols)
	{
		Collection<SymbolItem> body = new ArrayList<SymbolItem>();
		for (Symbol s : symbols)
			body.add(new SymbolItem(s, true));
		prods.add(new Production("p" + prodNum, head, body));
		prodNum++;
	}

	private static int prodNum = 1;
}

