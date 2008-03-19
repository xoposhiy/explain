package exp.lex;

import java.util.Hashtable;
import java.util.Set;

import exp.syn.Symbol;

public class Term implements Symbol {
	public static final String emptyName = "exp.core.empty";

	public static final Term empty = new Term(emptyName, new DFA(State.create(),
			new Hashtable<State, Set<Transition>>()));

	private final String typeName;

	private final State errorState;

	private final State initialState;

	private State currentState;

	private final Hashtable<State, Transitions> trans;

	public Term(String typeName, DFA dfa) {
		if (typeName == null)
			throw new IllegalArgumentException("typeName = null");
		if (dfa == null)
			throw new IllegalArgumentException("dfa = null");
		this.typeName = typeName;
		trans = createTransitions(dfa);
		initialState = dfa.getInitialState();
		currentState = initialState;
		errorState = State.createError();
		assert this.typeName != null;
		assert this.initialState != null;
		assert this.currentState == initialState;
		assert this.errorState != null;
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != Term.class)
			return false;
		return ((Term) other).getName().equals(getName());
	}

	@Override
	public String toString() {
		return typeName.toString();
	}

	public final Lexeme createLexeme(SourceContext context) {
		return new Lexeme(typeName, getValue(context), context);
	}

	public final String getName() {
		return typeName;
	}

	public final void reset() {
		currentState = initialState;
		onReset();
	}

	public final void readChar(char ch) {
		assert currentState != null;
		State oldState = currentState;
		Transitions t = trans.get(currentState);
		if (t == null)
			currentState = errorState;
		else {
			currentState = t.state(ch);
			if (currentState == null)
				currentState = errorState;
		}
		onStateChanged(ch, oldState);
	}

	public final State getState() {
		return currentState;
	}

	protected Object getValue(SourceContext context) {
		return null;
	}

	protected void onReset() {
	}

	protected void onStateChanged(char ch, State oldState) {
	}

	private static Hashtable<State, Transitions> createTransitions(DFA dfa) {
		Hashtable<State, Transitions> result = new Hashtable<State, Transitions>();
		for (State s : dfa.getStates()) {
			Transitions ts = new Transitions();
			for (Transition t : dfa.getTransitions(s))
				ts.add(t.chars, t.state);
			result.put(s, ts);
		}
		return result;
	}

	public String getExample() {
		String result = dfs(initialState, "");
		assert this == Term.empty || result != null : this;
		return result;
	}

	private String dfs(State state, String prefix) {
		if (state.isFinal())
			return prefix;
		if (state.isError())
			return null;
		Transitions ts = trans.get(state);
		for (char ch = 0; ch < 128; ch++) {
			State s = ts.state(ch);
			if (s == null)
				continue;
			String result = dfs(s, prefix + ch);
			if (result != null)
				return result;
		}
		return null;
	}
}
