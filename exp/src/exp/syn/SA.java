package exp.syn;

import java.util.Hashtable;

import exp.lex.LA;
import exp.lex.Term;

public class SA extends AbstractSA {
	private final Hashtable<Symbol, LA> laCache = new Hashtable<Symbol, LA>();
	public SA(Grammar grammar) throws GrammarException {
		super(grammar);
	}

	protected LA getLA(Symbol sym){
		LA la = laCache.get(sym);
		if (la == null){
			la = createLA(sym);
			laCache.put(sym, la);
		}
		return la;
		
	}

	private LA createLA(Symbol sym) {
		if (sym instanceof Term)
			return new LA(new Term[] {(Term)sym});
		else{
			return new LA(selectSet((Nonterm)sym));
		}
	}
}
