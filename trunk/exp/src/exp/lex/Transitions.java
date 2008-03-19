package exp.lex;

import java.util.Hashtable;

public final class Transitions{

	private final State trans[] = new State[128];

	private State others;

	private final Hashtable<Character, State> trans2 = new Hashtable<Character, State>();

	public State state(char ch) {
		State result;
		if (ch < 128)
			result = trans[ch];
		else
			result = trans2.get(new Character(ch));
		return result != null ? result : others;
	}
	
	public void add(CharSet chars, State newState) {
		if (chars.isInverted()){
			if (others != null && others != newState) 
				throw new IllegalArgumentException("<others> is already filled");
			others = newState;
		}
		else {
			for (char ch : chars)
				addChar(ch, newState);
		}
	}

	private boolean defined(char ch) {
		if (ch < 128)
			return trans[ch] != null;
		return trans2.containsKey(new Character(ch));
	}

	private void addChar(char ch, State newState) {
		if (newState == null)
			throw new IllegalArgumentException("newState = null");
		assert !defined(ch) : ch + " code: " + (int)ch;
		if (ch < 128)
			trans[ch] = newState;
		else
			trans2.put(new Character(ch), newState);
		assert defined(ch);
	}
}
