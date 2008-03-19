package exp;

import exp.lex.Conflicts;
import exp.lex.DFA;
import junit.framework.TestCase;

public class Conflicts_Test extends TestCase {
	public void testConflicts() {
		DFA a1 = DFAs.reals();
		DFA a2 = DFAs.simpleReals();
		DFA a3 = DFAs.integers();
		Conflicts cs = new Conflicts(new DFA[] {a1, a2, a3});
		assertTrue(cs.hasConflicts());
		for (Conflicts.Conflict c : cs.getConflicts()) {
			System.out.println(c.text);
		}
		assertEquals(2, cs.getConflicts().size());
	}

	public void testNoConflicts() {
		DFA a1 = DFAs.integers();
		DFA a2 = DFAs.reals();
		Conflicts cs = new Conflicts(new DFA[] {a1, a2});
		assertFalse(cs.hasConflicts());
	}
}
