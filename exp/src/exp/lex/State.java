package exp.lex;

public final class State {
	private final boolean fin;

	private final boolean err;

	private final String name;

	public static State createError(){
		return new State("ERROR", false, true);
	}

	public static State createFinal(){
		return new State(true, false);
	}
	public static State createFinal(String name){
		return new State(name, true, false);
	}
	public static State create(){
		return new State(false, false);
	}

	public static State create(String name){
		return new State(name, false, false);
	}

	private State(boolean isFinal, boolean isError) {
		this("", isFinal, isError);
	}

	private State(String name, boolean isFinal, boolean isError) {
		super();
		this.fin = isFinal;
		this.err = isError;
		this.name = name;
	}
	
	public boolean isFinal() {
		return fin;
	}

	public boolean isError() {
		return err;
	}

	@Override
	public String toString() {
		return name + "(" + fin + ", " + err + ")";
	}
	
	

}
