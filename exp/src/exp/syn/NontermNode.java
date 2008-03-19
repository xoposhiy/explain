package exp.syn;

import java.util.ArrayList;
import java.util.List;

public final class NontermNode extends SyntaxNode {

	private final Nonterm nonterm;

	private final List<SyntaxNode> children = new ArrayList<SyntaxNode>();

	public NontermNode(Nonterm nonterm, SyntaxNode parent) {
		super(parent);
		if (nonterm == null)
			throw new IllegalArgumentException("nonterm = null");
		this.nonterm = nonterm;
	}

	@Override
	public Nonterm getSymbol() {
		return nonterm;
	}

	public void appendChild(SyntaxNode child) {
		if (child == null)
			throw new IllegalArgumentException("child = null");
		children.add(child);
	}

	public SyntaxNode findNode(String fullname) {
		for (SyntaxNode child : children) {
			if (child.getSymbol().getName().equals(fullname))
				return child;
		}
		return null;
	}

	public SyntaxNode getChild(String fullname) throws SyntaxException {
		SyntaxNode result = findNode(fullname);
		if (result == null)
			throw new SyntaxException("ќжидалс€ узел " + fullname.toString());
		return result;
	}

	public Iterable<SyntaxNode> getChildren(String fullname) throws SyntaxException {
		ArrayList<SyntaxNode> result = new ArrayList<SyntaxNode>();
		for (SyntaxNode child : children) {
			if (child.getSymbol().getName().equals(fullname))
				result.add(child);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(nonterm.getName().toString());
		for (int i = 0; i < children.size(); i++) {
			SyntaxNode ch = children.get(i);
			writeChild(ch, sb);
		}
		return sb.toString();
	}

	private void writeChild(SyntaxNode ch, StringBuilder sb) {
		String[] lines = ch.toString().split("\n");
		sb.append("\n");
		for (int i = 0; i < lines.length; i++) {
			sb.append("  ");
			sb.append(lines[i]);
			if (i != lines.length - 1)
				sb.append("\n");
		}
	}
}
