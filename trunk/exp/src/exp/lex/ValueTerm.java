package exp.lex;

public class ValueTerm extends Term {

	public ValueTerm(String typeName, DFA dfa) {
		super(typeName, dfa);
	}

	@Override
	protected Object getValue(SourceContext context) {
		return context.getText();
	}
	
	

}
