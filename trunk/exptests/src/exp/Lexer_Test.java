package exp;

import junit.framework.TestCase;
import exp.lex.DocumentReader;
import exp.lex.LA;
import exp.lex.Lexeme;
import exp.lex.LexerException;
import exp.lex.Term;

public class Lexer_Test extends TestCase {
	public void test(){
		Term str = new Term("str", DFAs.createStringReader());
		Term nums = new Term("num", DFAs.reals());
		Term[] lrs = new Term[] {str, nums};
		checkLexerOk(lrs, "123.1", nums.getName());
		checkLexerOk(lrs, "\"123\"", str.getName());
		checkLexerOk(lrs, " \t123.1e+1", nums.getName());
		checkLexerOk(lrs, " \t\"123\"", str.getName());
	}
	
	private void checkLexerOk(Term[] lrs, String input, String expectedLexemeType) {
		LA lex = new LA(lrs);
		Lexeme lexeme;
		try {
			lexeme = lex.getNext(new DocumentReader(input));
		} catch (LexerException e) {
			throw new RuntimeException(e);
		}
		assertEquals(expectedLexemeType, lexeme.getType());
	}
}
