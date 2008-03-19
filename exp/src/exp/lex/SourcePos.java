package exp.lex;

/** 
 * Нумерация строк и позиций начинается с нуля
 */
public final class SourcePos {
	public final int line;

	public final int col;

	public SourcePos(int line, int col) {
		this.line = line;
		this.col = col;
	}

	@Override
	public String toString() {
		return line + ":" + col;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != SourcePos.class) return false;
		SourcePos otherPos = (SourcePos) other;
		return otherPos.col == col && otherPos.line == line;
	}

	@Override
	public int hashCode() {
		return 100*line ^ col;
	}
	

}
