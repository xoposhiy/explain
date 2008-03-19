package exp.syn;

public final class Nonterm implements Symbol {

	public static final Nonterm axiom = new Nonterm("exp.core.Axiom");

	private final String name;

	public Nonterm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != Nonterm.class)
			return false;
		return ((Nonterm)other).name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	

}
