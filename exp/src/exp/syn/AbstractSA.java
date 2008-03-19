package exp.syn;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import exp.lex.DocumentReader;
import exp.lex.LA;
import exp.lex.Lexeme;
import exp.lex.LexerException;
import exp.lex.Term;

public abstract class AbstractSA {
	private final ParseTable table;

	private final Stack<Pair> stack = new Stack<Pair>();

	private Pair current;

	private final Nonterm axiom;

	public AbstractSA(Grammar grammar) throws GrammarException {
		this.table = new ParseTable(grammar);
		this.axiom = grammar.getAxiom();
	}

	public SyntaxNode analyse(DocumentReader reader) throws SyntaxException {
		final SyntaxNode root = new NontermNode(axiom, null);
		push(axiom, root);
		Lexeme lexeme = null;
		while (!stack.isEmpty()) {
			current = stack.pop();
			if (lexeme == null)
				lexeme = readLexeme(current.sym, reader);
			if (current.sym instanceof Term) {
				if (current.sym.getName().equals(lexeme.getType())) {
					if (current.node instanceof TermNode)
						((TermNode) current.node).setValue(lexeme.getValue());
					else
						; // Терминал невидимый - не породил узла
					lexeme = null;
				} else
					throw new SyntaxException();
			} else {
				assert current.sym instanceof Nonterm;
				assert current.node instanceof NontermNode;
				final Production prod = select((Nonterm) current.sym, lexeme);
				final List<SymbolItem> body = prod.getBody();
				final List<SyntaxNode> children = new ArrayList<SyntaxNode>();
				for (int i = body.size() - 1; i >= 0; --i) {
					SymbolItem currentItem = body.get(i);
					if (currentItem.visible) {
						final SyntaxNode child = createNode(currentItem.sym, current.node);
						children.add(child);
						push(currentItem.sym, child);
					} else {
						push(currentItem.sym, current.node);
					}
				}
				for (int i = children.size() - 1; i >= 0; --i)
					((NontermNode) current.node).appendChild(children.get(i));
			}
		}
		if (reader.hasMore())
			throw new SyntaxException("Ожидался конец файла " + reader.getSourcePos());
		return root;
	}

	protected abstract LA getLA(Symbol sym);

	protected Production select(Nonterm state, Lexeme lexeme) throws SyntaxException {
		if (state == null)
			throw new IllegalArgumentException("state = null");
		if (lexeme == null)
			throw new IllegalArgumentException("lexeme = null");
		return getPart(state).getProduction(lexeme);
	}

	protected Term[] selectSet(Nonterm nonterm) {
		if (nonterm == null)
			throw new IllegalArgumentException("nonterm = null");
		return getPart(nonterm).getFirstTerms();
	}

	private ParseTableEntry getPart(Nonterm state) {
		final ParseTableEntry part = table.get(state);
		if (part == null) {
			for (Nonterm n : table.keySet()) {
				System.out.println(n);
			}
			throw new IllegalArgumentException("Неизвестный нетерминал " + state);
		}
		return part;
	}

	private SyntaxNode createNode(Symbol symbol, SyntaxNode parent) {
		if (symbol instanceof Term)
			return new TermNode((Term) symbol, parent);
		else
			return new NontermNode((Nonterm) symbol, parent);
	}

	private Lexeme readLexeme(Symbol sym, DocumentReader reader) throws SyntaxException {
		final LA la = getLA(sym);
		try {
			return la.getNext(reader);
		} catch (LexerException e) {
			throw new SyntaxException(e);
		}
	}

	private void push(Symbol symbol, SyntaxNode node) {
		stack.push(Pair.of(symbol, node));
	}

	private static final class Pair {
		public final Symbol sym;

		public final SyntaxNode node;

		private Pair(Symbol sym, SyntaxNode node) {
			this.sym = sym;
			this.node = node;
		}

		public static Pair of(Symbol sym, SyntaxNode node) {
			return new Pair(sym, node);
		}

	}
}
