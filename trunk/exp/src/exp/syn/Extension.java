package exp.syn;


public class Extension {
	private final String name;

	private final Iterable<Production> productions;

	private final Iterable<Extraction> extractions;

	public Extension(String name, Iterable<Production> productions, Iterable<Extraction> extractions) {
		super();
		this.name = name;
		this.productions = productions;
		this.extractions = extractions;
	}

	public Iterable<Extraction> getExtractions() {
		return extractions;
	}

	public String getName() {
		return name;
	}

	public Iterable<Production> getProductions() {
		return productions;
	}
}
