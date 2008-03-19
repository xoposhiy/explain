package exp;

import exp.lex.DFA;
import exp.lex.DFABuilder;
import exp.lex.State;

public class DFAs {
	public static DFA integers() {
		State initial = State.create("initial");
		State nums = State.createFinal("nums");
		State sign = State.create("sign");
		DFABuilder b = new DFABuilder(initial);
		b.add('0', '9', initial, nums);
		b.add("+-", initial, sign);
		b.add('0', '9', sign, nums);
		b.add('0', '9', nums, nums);
		return b.build();
	}
	
	public static DFA reals() {
		State initial = State.create();
		State sign = State.create();
		State integer = State.create();
		State dot = State.create();
		State fract = State.createFinal();
		State e = State.create();
		State eSign = State.create();
		State exp = State.createFinal();
		DFABuilder b = new DFABuilder(initial);
		b.add('0', '9', initial, integer);
		b.add("+-", initial, sign);
		b.add('0', '9', sign, integer);
		b.add('0', '9', integer, integer);
		b.add('.', integer, dot);
		b.add("eE", integer, e);
		b.add('0', '9', dot, fract);
		b.add('0', '9', fract, fract);
		b.add("eE", fract, e);
		b.add("+-", e, eSign);
		b.add('0', '9', e, exp);
		b.add('0', '9', eSign, exp);
		b.add('0', '9', exp, exp);		
		return b.build();
	}

	public static DFA simpleReals() {
		State initial = State.create("initial");
		State sign = State.create("sign");
		State integer = State.createFinal("integer");
		State dot = State.create("dot");
		State fract = State.createFinal("fract");
		DFABuilder b = new DFABuilder(initial);
		b.add('0', '9', initial, integer);
		b.add("+-", initial, sign);
		b.add('0', '9', sign, integer);
		b.add('0', '9', integer, integer);
		b.add('.', integer, dot);
		b.add('0', '9', dot, fract);
		b.add('0', '9', fract, fract);
		return b.build();
	}

	public static DFA createStringReader() {
		State initial = State.create();
		State open = State.create();
		State close = State.createFinal();
		State escape = State.create();
		DFABuilder b = new DFABuilder(initial);
		b.add('"', initial, open);
		b.add('\\', open, escape);
		b.add('"', open, close);
		b.addAllExcept("\\\"", open, open);
		b.add("tnr\"\\", escape, open);
		return b.build();
	}
}
