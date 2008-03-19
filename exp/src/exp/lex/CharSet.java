package exp.lex;

import java.util.HashSet;
import java.util.Iterator;

public class CharSet implements Iterable<Character>{
	private final HashSet<Character> chars;

	private final boolean inverted;

	public CharSet(HashSet<Character> chars, boolean invert){
		this.chars = chars;
		this.inverted = invert;
	}
	
	public CharSet(String chars){
		this(chars, false);
	}

	public CharSet(String chars, boolean invert){
		this(createHashSet(chars), invert);
	}

	public CharSet(char min, char max){
		this(min, max, false);
	}

	public CharSet(char min, char max, boolean invert){
		this(createHashSet(min, max), invert);
	}
	
	public Character intersect(CharSet other) {
		if (inverted) {
			if (other.inverted)
				return '?';
			else
				return other.intersect(this);
		} else {
			for (char ch : chars) {
				if (other.contains(ch))
					return ch;
			}
			return null;
		}
	}

	private static HashSet<Character> createHashSet(String chars) {
		final HashSet<Character> result = new HashSet<Character>();
		for (char ch : chars.toCharArray())
			result.add(ch);
		return result;		
	}

	private static HashSet<Character> createHashSet(char min, char max) {
		final HashSet<Character> result = new HashSet<Character>();
		for (char ch = min; ch <= max; ++ch)
			result.add(ch);
		return result;		
	}

	public boolean contains(char ch) {
		return chars.contains(ch) ^ inverted;
	}

	public boolean isInverted() {
		return inverted;
	}

	public Iterator<Character> iterator() {
		return chars.iterator();
	}
}
