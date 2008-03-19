package exp.syn.loader;

import java.util.Arrays;

import exp.lex.DFA;
import exp.lex.DFABuilder;
import exp.lex.State;
import exp.lex.Term;
import exp.lex.ValueTerm;
import exp.lex.loader.Lexicon;

public class Lex extends Lexicon{
	public Lex() {
		super(Arrays.asList(terms));
	}

	public static final DFA syntaxDFA = DFA.forWord("syntax");

	public static final DFA lexiconDFA = DFA.forWord("lexicon");

	public static final DFA usesDFA = DFA.forWord("uses");

	public static final DFA semiDFA = DFA.forWord(";");

	public static final DFA dotDFA = DFA.forWord(".");

	public static final DFA eqDFA = DFA.forWord("=");

	public static final DFA starDFA = DFA.forWord("*");

	public static final DFA colonDFA = DFA.forWord(":");
	
	public static final DFA atDFA = DFA.forWord("@");

	public static final DFA identDFA = createIdentDFA();

	public static final Term syntax = term("exp.core.syntax.syntax", syntaxDFA);

	public static final Term lexicon = term("exp.core.syntax.lexicon", lexiconDFA);

	public static final Term uses = term("exp.core.syntax.uses", usesDFA);

	public static final Term semi = term("exp.core.syntax.semi", semiDFA);

	public static final Term dot = term("exp.core.syntax.dot", dotDFA);

	public static final Term eq = term("exp.core.syntax.eq", eqDFA);

	public static final Term star = term("exp.core.syntax.star", starDFA);

	public static final Term colon = term("exp.core.syntax.colon", colonDFA);
	
	public static final Term at = term("exp.core.syntax.at", atDFA);

	public static final Term ident = value("exp.core.syntax.ident", identDFA);

	public static final Term[] terms = new Term[] { syntax, lexicon, uses, semi, colon, dot, eq, star, ident, at };

	private static Term term(String termName, DFA dfa) {
		return new Term(termName, dfa);
	}
	
	private static Term value(String termName, DFA dfa) {
		return new ValueTerm(termName, dfa);
	}

	private static DFA createIdentDFA() {
		State s0 = State.create();
		State s1 = State.createFinal();
		DFABuilder b = new DFABuilder(s0);
		b.add('a', 'z', s0, s1);
		b.add('A', 'Z', s0, s1);
		b.add('a', 'z', s1, s1);
		b.add('A', 'Z', s1, s1);
		b.add('0', '9', s1, s1);
		return b.build();
	}
}