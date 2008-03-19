package exp.syn;

import exp.lex.Term;

public final class TermNode extends SyntaxNode {
	private final Term term;

	private Object value;

	public TermNode(Term term, SyntaxNode parent) {
		super(parent);
		if (term == null)
			throw new IllegalArgumentException("term = null");
		this.term = term;
	}

	@Override
	public String toString() {
		if (value != null)
			return String.format("[%s: %s]", term.getName(), value);
		else
			return String.format("[%s]", term.getName());
	}

	public Term getSymbol() {
		return term;
	}

	public Object value() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
