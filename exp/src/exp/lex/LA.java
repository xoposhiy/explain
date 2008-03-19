package exp.lex;

public class LA {
	private final Term[] terms;

	public LA(Term[] terms) {
		super();
		this.terms = terms;
	}

	public Lexeme getNext(DocumentReader reader) throws LexerException {
		skipSpaces(reader);
		if (!reader.hasMore())
			return Lexeme.createEOT(reader.getText(), reader.getPos());
		final int start = reader.getPos();
		int end = start;
		Term bestReader = null;
		for (Term r : terms)
			r.reset();
		boolean cont = true;
		while (cont && reader.hasMore()) {
			cont = false;
			final char ch = reader.getNext();
			for (Term r : terms) {
				r.readChar(ch);
				if (r.getState().isFinal()) {
					bestReader = r;
					end = reader.getPos();
					cont = true;
				} else if (!r.getState().isError()) {
					cont = true;
				}
			}
		}
		final int realEnd = reader.getPos();
		reader.setPos(end);
		if (bestReader == null) {
			String expected = getExpected();
			SourceContext context = new SourceContext(reader.getText(), start, realEnd);
			throw new LexerException(String.format("Встретилось '%s' вместо %s положение %s", context.getText(), expected, context.toString()));
		}
		return bestReader.createLexeme(new SourceContext(reader.getText(), start, end));
	}
	
	private void skipSpaces(DocumentReader reader) {
		while (reader.hasMore() && isSpace(reader.peekNext()))
			reader.getNext();
	}

	private boolean isSpace(char c) {
		switch (c) {
		case ' ':
		case '\n':
		case '\r':
		case '\t':
			return true;
		default:
			return false;
		}
	}

	private String getExpected() {
		StringBuilder sb = new StringBuilder();
		for (Term r : terms) {
			String example = r.getExample();
			if (example != null)
				sb.append(example + ", ");
		}
		return sb.toString();
	}
}
