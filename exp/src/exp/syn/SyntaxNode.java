package exp.syn;


public abstract class SyntaxNode {
	private final SyntaxNode parent;

	public SyntaxNode(SyntaxNode parent) {
		this.parent = parent;
	}

	public SyntaxNode getParent() {
		return parent;
	}

	public abstract Symbol getSymbol();

	public abstract String toString();
}
