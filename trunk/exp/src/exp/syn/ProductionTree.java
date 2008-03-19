package exp.syn;

public class ProductionTree {
	private final Extension extension;

	private final Production prod;

	private ProductionTree trunk;

	private ProductionTree branch;

	public ProductionTree(Extension extension, Production prod) {
		super();
		this.extension = extension;
		this.prod = prod;
	}

	public ProductionTree getBranch() {
		return branch;
	}

	public Extension getExtension() {
		return extension;
	}

	public String getName() {
		return prod.getName();
	}

	public Production getProd() {
		return prod;
	}

	public ProductionTree getTrunk() {
		return trunk;
	}

	// TODO сигнатура не додумана...
	public void extract() {

	}
}
