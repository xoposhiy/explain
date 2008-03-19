package exp.syn.loader;

import java.util.ArrayList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import exp.lex.DocumentReader;
import exp.lex.LexerException;
import exp.lex.Term;
import exp.syn.Extension;
import exp.syn.Extraction;
import exp.syn.GrammarException;
import exp.syn.NamesResolver;
import exp.syn.Nonterm;
import exp.syn.NontermNode;
import exp.syn.Production;
import exp.syn.SA;
import exp.syn.SymbolItem;
import exp.syn.SyntaxException;
import exp.syn.SyntaxNode;
import exp.syn.TermNode;

public class ExtensionLoader {
	private final NamesResolver resolver;

	public ExtensionLoader(NamesResolver resolver) {
		super();
		this.resolver = resolver;
	}

	public Extension parseExtension(DocumentReader reader) throws SyntaxException {
		SyntaxNode tree = readSyntaxTree(reader);
		return parseExtension((NontermNode) tree);
	}

	public Extension parseExtension(NontermNode tree) throws SyntaxException {
		NontermNode header = getChild(tree, Syntax.Header);
		String name = readQName(header);
		ArrayList<Production> productions = new ArrayList<Production>();
		ArrayList<Extraction> extractions = new ArrayList<Extraction>();
		NontermNode transforms = (NontermNode) tree.getChild(Syntax.Transforms.getName());
		readTransforms(name, transforms, productions, extractions);
		return new Extension(name, productions, extractions);
	}

	private void readTransforms(String extName, NontermNode transforms, ArrayList<Production> productions,
			ArrayList<Extraction> extractions) throws SyntaxException {
		for (SyntaxNode n : transforms.getChildren(Syntax.Transform.getName())) {
			String name = extName + "." + getValue(n, Lex.ident);
			NontermNode node = (NontermNode) ((NontermNode) n).findNode(Syntax.AddTransform.getName());
			if (node != null)
				productions.add(readProduction(extName, node, name));
			else {
				throw new NotImplementedException();
			}
		}
	}

	private Production readProduction(String extName, NontermNode node, String name) throws SyntaxException {
		Nonterm head = resolver.getNonterm(readQName(getChild(node, Syntax.Head)));
		NontermNode body = getChild(node, Syntax.Body);
		ArrayList<SymbolItem> items = new ArrayList<SymbolItem>();
		for (SyntaxNode itemNode : body.getChildren(Syntax.SymbolItem.getName())) {
			NontermNode n = (NontermNode) itemNode;
			boolean visible = (n.findNode(Syntax.Visible.getName()) != null);
			SyntaxNode termNode = n.findNode(Syntax.Terminal.getName());
			if (termNode != null) {
				String symName = readQName(termNode);
				Term term;
				try {
					term = resolver.getTerm(symName);
				} catch (LexerException e) {
					throw new RuntimeException(e);
				}
				items.add(new SymbolItem(term, visible));
			} else {
				NontermNode nontermNode = getChild(n, Syntax.Nonterminal);
				String symName = readQName(nontermNode);
				Nonterm nonterm = resolver.getNonterm(symName);
				items.add(new SymbolItem(nonterm, visible));
			}
		}
		return new Production(name, head, items);
	}

	private String getValue(SyntaxNode n, Term terminal) throws SyntaxException {
		SyntaxNode child = ((NontermNode) n).getChild(terminal.getName());
		return (String) ((TermNode) child).value();
	}

	private String readQName(SyntaxNode node) throws SyntaxException {
		StringBuilder sb = new StringBuilder();
		NontermNode qnameNode = getChild(node, Syntax.QName);
		for (SyntaxNode child : qnameNode.getChildren(Lex.ident.getName())) {
			sb.append(getValue(child)).append(".");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	private NontermNode getChild(SyntaxNode parent, Nonterm n) throws SyntaxException {
		return (NontermNode) ((NontermNode) parent).getChild(n.getName());
	}

	private String getValue(SyntaxNode node) throws SyntaxException {
		return (String) ((TermNode) node).value();
	}

	private SyntaxNode readSyntaxTree(DocumentReader reader) throws SyntaxException {
		try {
			return new SA(Syntax.grammar).analyse(reader);
		} catch (GrammarException e) {
			throw new RuntimeException(e);
		}
	}
}
