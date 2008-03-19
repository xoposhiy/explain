package exp.syn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import exp.lex.Term;

public final class ParseTableFiller {

	private final Collection<Production> productions;

	private final Set<Nonterm> calculating1 = new HashSet<Nonterm>();

	private final Set<ProdSubstring> calculating2 = new HashSet<ProdSubstring>();

	private final Hashtable<ProdSubstring, Set<Term>> first1 = new Hashtable<ProdSubstring, Set<Term>>();

	private final Hashtable<Nonterm, Set<Term>> first2 = new Hashtable<Nonterm, Set<Term>>();

	private final Hashtable<Nonterm, Set<Term>> follow = new Hashtable<Nonterm, Set<Term>>();

	private final Hashtable<Nonterm, Set<Production>> prodsByHead = new Hashtable<Nonterm, Set<Production>>();

	public ParseTableFiller(Collection<Production> productions) {
		this.productions = productions;
		for (Production prod : productions) {
			Set<Production> set = prodsByHead.get(prod.getHead());
			if (set == null) {
				set = new HashSet<Production>();
				prodsByHead.put(prod.getHead(), set);
			}
			set.add(prod);
		}
	}

	public void fill(ParseTable table) throws GrammarException {
		if (table == null)
			throw new IllegalArgumentException("table = null");
		for (Production prod : productions) {
			final ProdSubstring word = new ProdSubstring(prod, 0);
			if (!first1.containsKey(word)) {
				calculateFirst(word);
			}
		}
		calculating1.clear();
		for (Production prod : productions) {
			final Nonterm n = prod.getHead();
			if (!follow.containsKey(n)) {
				calculateFollow(n, new HashSet<Term>());
			}
		}
		fillTable(table);
	}

	private void fillTable(ParseTable table) throws GrammarException {
		for (Production prod : productions) {
			final Set<Term> fi = calculateFirst(new ProdSubstring(prod, 0));
			table.addDecisionRules(fi, prod);
			if (fi.contains(Term.empty)) {
				final Set<Term> fo = new HashSet<Term>();
				calculateFollow(prod.getHead(), fo);
				follow.put(prod.getHead(), fo);
				logSelectSet("fo", prod.getHead(), fo);
				table.addDecisionRules(fo, prod);
			}
		}
	}

	private void logSelectSet(String name, Nonterm arg, final Set<Term> fo) {
		StringBuilder sb = new StringBuilder(name + " " + arg + " ");
		for (Term term : fo) {
			sb.append(term.toString() + ", ");
		}
		System.out.println(sb);
	}

	private boolean calculateFollow(Nonterm n, Set<Term> result) throws GrammarException {
		if (calculating1.contains(n))
			return false;
		final Set<Term> r = follow.get(n);
		if (r != null)
			return result.addAll(r);
		return doCalcFollow(n, result);
	}

	private boolean doCalcFollow(Nonterm n, Set<Term> result) throws GrammarException {
		calculating1.add(n);
		log("calcing " + n);
		final Set<Term> r = new HashSet<Term>();
		boolean completed = true;
		for (Production prod : productions) {
			final List<SymbolItem> body = prod.getBody();
			for (int i = 0; i < body.size(); i++) {
				if (body.get(i).sym.equals(n)) {
					boolean sufCompleted = addSuffix(new ProdSubstring(prod, i + 1), r);
					completed = completed && sufCompleted;
				}
			}
		}
		result.addAll(r);
		if (completed){
			logSelectSet("fo", n, r);
			follow.put(n, r);
		}
		else{
			logSelectSet("f*", n, r);

		}
		log("calced " + n);
		calculating1.remove(n);
		return completed;
	}

	private boolean addSuffix(ProdSubstring word, Set<Term> result)
			throws GrammarException {
		final Set<Term> fi = calculateFirst(word);
		result.addAll(fi);
		if (fi.contains(Term.empty))
			return calculateFollow(word.prod.getHead(), result);
		return true;
	}

	private Set<Term> calculateFirst(Symbol sym) throws GrammarException {
		if (sym instanceof Term)
			return asSet((Term) sym);
		else {
			if (calculating1.contains((Nonterm) sym))
				throw new GrammarException();
			final Nonterm n = (Nonterm) sym;
			final Set<Term> set = first2.get(n);
			if (set != null)
				return set;
			return doCalcFirst(n);
		}
	}

	private Set<Term> doCalcFirst(Nonterm n) throws GrammarException {
		calculating1.add(n);
		final Set<Term> set = new HashSet<Term>();
		final Set<Production> prods = prodsByHead.get(n);
		if (prods == null)
			throw new RuntimeException(n + " is not defined!");
		for (Production prod : prods)
			set.addAll(calculateFirst(new ProdSubstring(prod, 0)));
		first2.put(n, set);
		calculating1.remove(n);
		return set;
	}

	private Set<Term> asSet(Term term) {
		final Set<Term> set = new HashSet<Term>();
		set.add(term);
		return set;
	}

	private Set<Term> calculateFirst(ProdSubstring word)
			throws GrammarException {
		if (word.startIndex >= word.prod.getBody().size())
			return asSet(Term.empty);
		else {
			if (calculating2.contains(word))
				throw new GrammarException(word.toString());
			final Set<Term> result = first1.get(word);
			if (result != null)
				return result;
			return doCalcFirst(word);
		}
	}

	private static void log(String s){
		System.out.println(s);		
	}
	
	private Set<Term> doCalcFirst(ProdSubstring word) throws GrammarException {
		calculating2.add(word);
		log("calculating " + word);
		final Set<Term> result = new HashSet<Term>();
		final List<SymbolItem> body = word.prod.getBody();
		for (int i = word.startIndex; i < body.size(); i++) {
			SymbolItem item = body.get(i);
			final Set<Term> set = calculateFirst(item.sym);
			result.addAll(set);
			if (!set.contains(Term.empty))
				break;
			else log(item.sym + " is nullable. continue...");
			if (i < body.size()-1)
				result.remove(Term.empty);
		}
		first1.put(word, result);
		calculating2.remove(word);
		log("calculated " + word);
		return result;
	}

	private static class ProdSubstring {
		public final Production prod;

		public final int startIndex;

		public ProdSubstring(Production prod, int startIndex) {
			this.prod = prod;
			this.startIndex = startIndex;
		}
		
		

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			for (SymbolItem item : prod.getBody())
				sb.append(item.sym.toString() + " ");
			return sb.toString();
		}



		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != ProdSubstring.class)
				return false;
			final ProdSubstring o = (ProdSubstring) other;
			return prod.equals(o.prod) && startIndex == o.startIndex;
		}

		@Override
		public int hashCode() {
			return prod.hashCode() ^ startIndex;
		}

	}

}
