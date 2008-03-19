package exp;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import exp.lex.LexerException;
import exp.lex.Term;
import exp.lex.loader.Lexicon;
import exp.syn.NamesResolver;
import exp.syn.Nonterm;

public class FakeNamesResolver implements NamesResolver {

	private final List<Lexicon> lexicons = new ArrayList<Lexicon>();
	private final Dictionary<String, Nonterm> nonterms = new Hashtable<String, Nonterm>();
	
	public void addLexicon(Lexicon lexicon){
		lexicons.add(lexicon);
	}
	
	public Nonterm getNonterm(String name) {
		if (name.equals(Nonterm.axiom.getName())) 
			return Nonterm.axiom;
		Nonterm n = nonterms.get(name);
		if (n == null)
		{
			n = new Nonterm(name);
			nonterms.put(name, n);
		}
		return n;
	}

	public Term getTerm(String name) throws LexerException {
		for (Lexicon l : lexicons) {
			if (l.hasTerm(name))
				return l.getTerm(name);
			//TODO искать дубликаты
		}
		throw new LexerException(name);
	}


}
