package exp.lex;

public class Lexeme {
	private final String type;

	private final Object value;

	private final SourceContext context;

	public Lexeme(String type, Object value, SourceContext context) {
		this.type = type;
		this.value = value;
		this.context = context;
	}

	public String getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public SourceContext getContext() {
		return context;
	}

	public static Lexeme createEOT(DocumentText text, int pos) {
		return new Lexeme(Term.emptyName, null, new SourceContext(text, pos, pos));
	}
	
}
