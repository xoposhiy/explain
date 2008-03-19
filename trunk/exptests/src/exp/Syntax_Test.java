package exp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;
import exp.lex.DocumentReader;
import exp.syn.GrammarException;
import exp.syn.SA;
import exp.syn.SyntaxException;
import exp.syn.SyntaxNode;
import exp.syn.loader.Syntax;

public class Syntax_Test extends TestCase {
	public void test() throws GrammarException, IOException, SyntaxException {
		InputStream s = this.getClass().getResourceAsStream("Syntax_Test.gr");
		SA sa = new SA(Syntax.grammar);
		SyntaxNode tree = sa.analyse(new DocumentReader(new InputStreamReader(s)));
		System.out.println(tree.toString());
		
	}

}
