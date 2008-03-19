package exp.lex;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/// Предполагается, что это класс будет использоваться перед вызовом конструктора LA.
public final class Conflicts {
	private final Hashtable<State2, String> markedStates = new Hashtable<State2, String>();

	private final Queue<State2> q = new LinkedList<State2>();

	private final List<Conflict> conflicts = new ArrayList<Conflict>();

	public Conflicts(DFA[] dfas) {
		if (dfas == null)
			throw new IllegalArgumentException("dfas");
		for (int i1 = 0; i1 < dfas.length - 1; ++i1)
			for (int i2 = i1 + 1; i2 < dfas.length; ++i2) {
				Conflict conflict = searchConflicts(dfas[i1], dfas[i2]);
				if (conflict != null)
					conflicts.add(conflict);
			}
	}

	public boolean hasConflicts() {
		return conflicts.size() > 0;
	}

	public List<Conflict> getConflicts() {
		return conflicts;
	}

	private Conflict searchConflicts(DFA a1, DFA a2) {
		q.clear();
		markedStates.clear();
		try {
			enqueue(new State2(a1.getInitialState(), a2.getInitialState()), "");
			while (!q.isEmpty()) {
				State2 s = dequeue();
				String mark = markedStates.get(s);
				for (Neighbour n : getNeighbours(s, a1, a2)) {
					if (!markedStates.containsKey(n.s))
						enqueue(n.s, mark + n.ch);
				}
			}
			return null;
		} catch (FoundException e) {
			return new Conflict(e.conflict, a1, a2);
		}
	}

	private List<Neighbour> getNeighbours(State2 s, DFA a1, DFA a2) {
		final ArrayList<Neighbour> result = new ArrayList<Neighbour>();
		final Iterable<Transition> ts1 = a1.getTransitions(s.s1);
		final Iterable<Transition> ts2 = a2.getTransitions(s.s2);
		assert ts1 != null;
		assert ts2 != null;
		for (Transition t1 : ts1) {
			for (Transition t2 : ts2) {
				final Character ch = t1.chars.intersect(t2.chars);
				if (ch != null) {
					State2 ns = new State2(t1.state, t2.state);
					Neighbour n = new Neighbour(ns, ch);
					result.add(n);
				}
			}
		}
		return result;
	}

	private void enqueue(State2 s, String mark) throws FoundException {
		assert !s.s1.isError();
		assert !s.s2.isError();
		if (s.isFinal())
			throw new FoundException(mark);
		markedStates.put(s, mark);
		q.add(s);
	}

	private State2 dequeue() {
		return q.remove();
	}

	private static final class Neighbour {
		public final State2 s;

		public final char ch;

		public Neighbour(State2 s, char ch) {
			this.s = s;
			this.ch = ch;
		}
	}

	private static final class State2 {
		public final State s1, s2;

		public State2(State s1, State s2) {
			this.s1 = s1;
			this.s2 = s2;
		}

		public boolean isFinal() {
			return s1.isFinal() && s2.isFinal();
		}

		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != State2.class)
				return false;
			State2 o = (State2) other;
			return s1 == o.s1 && s2 == o.s2;
		}

		@Override
		public int hashCode() {
			return s1.hashCode() ^ s2.hashCode();
		}

	}

	@SuppressWarnings("serial")
	private static final class FoundException extends Exception {

		public final String conflict;

		public FoundException(String conflict) {
			this.conflict = conflict;
		}

	}

	public static final class Conflict {
		public final String text;

		public final DFA dfa1;

		public final DFA dfa2;

		public Conflict(String text, DFA dfa1, DFA dfa2) {
			this.text = text;
			this.dfa1 = dfa1;
			this.dfa2 = dfa2;
		}
	}
}
