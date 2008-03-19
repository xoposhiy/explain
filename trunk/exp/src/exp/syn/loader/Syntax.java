package exp.syn.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import exp.syn.Grammar;
import exp.syn.GrammarException;
import exp.syn.Nonterm;
import exp.syn.Production;
import exp.syn.Symbol;
import exp.syn.SymbolItem;

public class Syntax {
	public static final Nonterm Header = nonterm("Header");
//	public static final Nonterm Uses = nonterm("Uses");
	public static final Nonterm Transforms = nonterm("Transforms");
	public static final Nonterm QName = nonterm("QName");
	public static final Nonterm QNameTail = nonterm("QNameTail");
	public static final Nonterm Use = nonterm("Use");
	public static final Nonterm UsesTail = nonterm("UseTail");
	public static final Nonterm UsesBody = nonterm("UsesBody");
	public static final Nonterm LexiconUsage = nonterm("LexiconUsage");
	public static final Nonterm SyntaxUsage = nonterm("SyntaxUsage");
	public static final Nonterm Transform = nonterm("Transform");
	public static final Nonterm TransformsTail = nonterm("TransformsTail");
	public static final Nonterm TransformBody = nonterm("TransformBody");
	public static final Nonterm AddTransform = nonterm("AddTransform");
	public static final Nonterm Head = nonterm("Head");
	public static final Nonterm Body = nonterm("Body");
	public static final Nonterm SymbolItem = nonterm("SymbolItem");
	public static final Nonterm BodyTail = nonterm("BodyTail");
	public static final Nonterm Visibility = nonterm("Visibility");
	public static final Nonterm Symbol = nonterm("Symbol");
	public static final Nonterm Invisible = nonterm("Invisible");
	public static final Nonterm Visible = nonterm("Visible");
	public static final Nonterm Nonterminal = nonterm("Nonterminal");
	public static final Nonterm Terminal = nonterm("Terminal");
	
	public static final Grammar grammar;
	private static final SymbolItem[] empty = {};
	static {
		try {
			Collection<Production> prods = new Productions(Nonterm.axiom).prods;
			grammar = new Grammar(Nonterm.axiom, prods);
		} catch (GrammarException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Nonterm nonterm(String name) {
		return new Nonterm("exp.core.syntax." + name);
	}

	private static class Productions {
		public final Collection<Production> prods = new ArrayList<Production>();
		public final Set<String> prodNames = new HashSet<String>();

		public Productions(Nonterm axiom) {
			prod("Root", axiom, node(Header), node(Transforms));
			
			prod("Header", Header, sym(Lex.syntax), node(QName), sym(Lex.semi));
			prod("QName", QName, node(Lex.ident), sym(QNameTail));
			prod("QName0", QNameTail, empty);
			prod("QName1", QNameTail, sym(Lex.dot), sym(QName));
			
//			prod("Uses", Uses, sym(Use), sym(UsesTail));
//			prod("Uses0", UsesTail, empty);
//			prod("Uses1", UsesTail, sym(Uses));
//			prod("Use", Use, sym(Lex.uses), sym(UsesBody), sym(Lex.semi));
//			prod("UseLex", UsesBody, node(LexiconUsage));
//			prod("UseLex1", LexiconUsage, sym(Lex.lexicon), node(QName));
//			prod("UseSyn", UsesBody, node(SyntaxUsage));
//			prod("UseSyn1", SyntaxUsage, sym(Lex.syntax), node(QName));
			
			prod("Trans", Transforms, node(Transform), sym(TransformsTail));
			prod("Trans0", TransformsTail, empty);
			prod("Trans1", TransformsTail, sym(Transforms));
			prod("Tran", Transform, node(Lex.ident), sym(Lex.colon), sym(TransformBody));
			
			prod("Add", TransformBody, node(AddTransform));
			prod("AddBody", AddTransform, node(Head), sym(Lex.eq), node(Body), sym(Lex.semi));
			prod("TranHead", Head, node(QName));
			prod("TranBody0", Body, empty);
			prod("TranBody1", Body, node(SymbolItem), sym(BodyTail));
			prod("SymbolItem", SymbolItem, sym(Visibility), sym(Symbol));
			prod("BodyTail0", BodyTail, empty);
			prod("TranBodyTail1", BodyTail, sym(Body));
			prod("Vis0", Visibility, node(Invisible));
			prod("Vis1", Visibility, node(Visible));
			prod("Vis00", Invisible, empty);
			prod("Vis11", Visible, sym(Lex.star));
			prod("Sym1", Symbol, node(Nonterminal));
			prod("Sym2", Symbol, node(Terminal));
			prod("Nonterm", Nonterminal, node(QName));
			prod("Terminal", Terminal, sym(Lex.at), node(QName));
		}

		private void prod(String name, Nonterm head, SymbolItem... body) {
			if (prodNames.contains(name))
				throw new RuntimeException("Правило вывода с именем " + name + " существует в нескольких экземплярах");
			prods.add(new Production(name, head, Arrays.asList(body)));
		}


		private static SymbolItem sym(Symbol sym) {
			return new SymbolItem(sym, false);
		}

		private static SymbolItem node(Symbol sym) {
			return new SymbolItem(sym, true);
		}
	}


}
