package exp.lex;

public class Transition {

	public final CharSet chars;

	public final State state;

	public Transition(CharSet chars, State state) {
		this.chars = chars;
		this.state = state;
	}
}
