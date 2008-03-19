package exp.syn;

import java.util.Collection;

public class Grammar {

	private final Nonterm axiom;

	private final Collection<Production> productions;

	public Grammar(Nonterm axiom, Collection<Production> productions) throws GrammarException {
		this.axiom = axiom;
		this.productions = productions;
	}

	public Nonterm getAxiom() {
		return axiom;
	}

	public Collection<Production> getProductions() {
		return productions;
	}

}
