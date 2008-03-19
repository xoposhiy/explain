package exp.lex;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class DFABuilder {
	private final State initialState;

	private final Hashtable<State, Set<Transition>> trans = new Hashtable<State, Set<Transition>>();

	public DFABuilder(State initialState) {
		super();
		if (initialState == null)
			throw new IllegalArgumentException("initialState = null");
		this.initialState = initialState;
	}
	
	public void add(char ch, State s1, State s2){
		add(ch, ch, s1, s2);
	}

	public void addSequence(String sequence, State s1, State s2){
		int len = sequence.length();
		State s = s1;
		for (int i = 0; i < len-1; ++i){
			State ss = State.create();
			add(sequence.charAt(i), s, ss);
			s = ss;
		}
		add(sequence.charAt(len-1), s, s2);
	}

	public void add(String chars, State s1, State s2){
		add(new CharSet(chars), s1, s2);
	}

	public void addAllExcept(String chars, State s1, State s2) {
		add(new CharSet(chars, true), s1, s2);
	}

	public void add(char minChar, char maxChar, State s1, State s2){
		add(new CharSet(minChar, maxChar), s1, s2);
	}

	public void add(CharSet chars, State s1, State s2){
		Set<Transition> t = getTransition(s1);
		t.add(new Transition(chars, s2));
	}

	public DFA build(){
		return new DFA(initialState, trans);
	}

	private Set<Transition> getTransition(State state) {
		Set<Transition> ts = trans.get(state);
		if (ts == null) {
			ts = new HashSet<Transition>();
			trans.put(state, ts);
		}
		return ts;
	}


}
