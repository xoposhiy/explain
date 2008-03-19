package exp;

import junit.framework.TestCase;
import exp.lex.DFA;
import exp.lex.State;
import exp.lex.Term;

public class LexemeReader_Test extends TestCase {

	public void testIntegers() {
		Term r = createLexemeDFA("int", DFAs.integers());

		checkReadOk(r, "123");
		checkReadOk(r, "0");
		checkReadOk(r, "+1");
		checkReadOk(r, "-123");
		checkRead(r, "", false, false);
		checkRead(r, "-", false, false);
		checkRead(r, "+", false, false);
		checkRead(r, "+-", false, true);
		checkRead(r, "+-1", false, true);
		checkRead(r, "1+", false, true);
		checkRead(r, "-1+", false, true);
	}

	private Term createLexemeDFA(String lexemeTypeName, DFA dfa) {
		return new Term(lexemeTypeName, dfa);
	}

	public void testReals() {
		Term r = createLexemeDFA("real", DFAs.reals());

		checkReadOk(r, "123.1");
		checkReadOk(r, "-123.1");
		checkReadOk(r, "-123.123");
		checkReadOk(r, "0.1");
		checkReadOk(r, "0.123");
		checkReadOk(r, "1e1");
		checkReadOk(r, "1e-1");
		checkReadOk(r, "1E-123");
		checkReadOk(r, "-1E+123");
		checkReadOk(r, "-1.2E+123");
		checkReadOk(r, "-0123.0123E+0123");
		checkRead(r, "123", false, false);
		checkRead(r, "0", false, false);
		checkRead(r, "+1", false, false);
		checkRead(r, "-123", false, false);
		checkRead(r, "", false, false);
		checkRead(r, "-", false, false);
		checkRead(r, "+", false, false);
		checkRead(r, "+-", false, true);
		checkRead(r, "+-1", false, true);
		checkRead(r, "1+", false, true);
		checkRead(r, "-1+", false, true);
		checkRead(r, "-1e", false, false);
		checkRead(r, "-1e+", false, false);
		checkRead(r, "-1e1e", false, true);
		checkRead(r, "-1.", false, false);
		checkRead(r, "-1.e1", false, true);
		checkRead(r, ".1", false, true);
		checkRead(r, "-.1", false, true);
		checkRead(r, "-1+1e1", false, true);
	}

	public void testStrings() {
		Term r = createLexemeDFA("str", DFAs.createStringReader());

		checkReadOk(r, "\"\"");
		checkReadOk(r, "\" \"");
		checkReadOk(r, "\" h \"");
		checkReadOk(r, "\" ! \"");
		checkReadOk(r, "\" \t \"");
		checkReadOk(r, "\" hello world! \"");
		checkReadOk(r, "\" \\t \"");
		checkReadOk(r, "\" \\r \"");
		checkReadOk(r, "\" \\n \"");
		checkReadOk(r, "\" \\\\ \"");
		checkReadOk(r, "\" \\\" \"");
		checkReadOk(r,
				"\" \\tHello \\r \\n world! \\\\ \\\" slash in quotes \\\" \\\\ \"");
	}

	private void checkReadOk(Term r, String input) {
		checkRead(r, input, true, false);
	}

	private void checkRead(Term r, String input, boolean ok, boolean error) {
		r.reset();
		for (char ch : input.toCharArray()) {
			r.readChar(ch);
		}
		State state = r.getState();
		assertEquals(ok, state.isFinal());
		assertEquals(error, state.isError());
	}
}
