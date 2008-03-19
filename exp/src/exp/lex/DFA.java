package exp.lex;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public final class DFA {
	private final State initialState;
	private final Hashtable<State, Set<Transition>> trans;
	private Set<State> states = null; 

	public static DFA forWord(String text){
		State s0 = State.create();
		DFABuilder b = new DFABuilder(s0);
		b.addSequence(text, s0, State.createFinal());
		return b.build();
	}
	
	public DFA(State initialState, Hashtable<State, Set<Transition>> transitions) {
		if (initialState == null)
			throw new IllegalArgumentException("initialState = null");
		if (transitions == null)
			throw new IllegalArgumentException("transitions = null");
		this.initialState = initialState;
		this.trans = transitions;
	}

	public Iterable<Transition> getTransitions(State state){
		Set<Transition> result = trans.get(state);
		if (result == null) result = new HashSet<Transition>(0);
		return result;
	}

	public Set<State> getStates(){
		if (states == null) calculateStates();
		assert states != null;
		return states;
	}

	private void calculateStates() {
		states = new HashSet<State>();
		states.add(initialState);
		states.addAll(trans.keySet());
		for (Set<Transition> ts : trans.values()) {
			for(Transition t : ts)
				states.add(t.state);
		}
	}

	public State getInitialState() {
		return initialState;
	}

}
