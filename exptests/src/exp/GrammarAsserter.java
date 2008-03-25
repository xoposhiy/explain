package exp;

import exp.syn.Grammar;
import exp.syn.GrammarException;
import exp.syn.SyntaxException;
import exp.syn.SA;
import exp.lex.DocumentReader;

class GrammarAsserter
{
	GrammarAsserter(Grammar grammar)
	{
		this.grammar = grammar;
	}

	public void assertAccepts(String s) throws GrammarException, SyntaxException
	{
		parse(s);
	}

	public void assertRejects(String s) throws GrammarException, SyntaxException
	{
		try
		{
			parse(s);
			junit.framework.Assert.fail(String.format("'%s' should NOT be accepted by this grammar", s));
		}
		catch (SyntaxException ex)
		{}
	}

	private void parse(String s) throws GrammarException, SyntaxException
	{
		DocumentReader rdr = new DocumentReader(s);
		SA sa = new SA(grammar);
		sa.analyse(rdr);
	}

	private Grammar grammar;
}
