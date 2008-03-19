package exp.syn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

//TODO Тест!
public class GrammarBuilder {

	private final Dictionary<String, ProductionTree> index = new Hashtable<String, ProductionTree>();

	private final List<ProductionTree> wood = new ArrayList<ProductionTree>();
	
	public GrammarBuilder(NamesResolver provider) {
		super();
	}

	public void extend(Extension ext) {
		for (Production prod : ext.getProductions()) {
			add(ext, prod);
		}
		//kjhhj
	}

	private void add(Extension ext, Production prod) {
		ProductionTree tree = new ProductionTree(ext, prod);
		wood.add(tree);
		index.put(prod.getName(), tree);
	}

	public Grammar build() throws GrammarException {
		return new Grammar(Nonterm.axiom, getProductions());
	}

	private Collection<Production> getProductions() {
		List<Production> result = new ArrayList<Production>();
		for (ProductionTree tree : wood) {
			addProductions(tree, result);
		}
		return result;
	}

	private void addProductions(ProductionTree tree, List<Production> result) {
		if (tree.getTrunk() == null){
			result.add(tree.getProd());
		}
		else{
			addProductions(tree.getTrunk(), result);
			addProductions(tree.getBranch(), result);
		}
		
	}
}
