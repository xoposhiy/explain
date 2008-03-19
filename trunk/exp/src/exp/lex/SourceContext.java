package exp.lex;

public final class SourceContext {

	private final DocumentText text;

	private final int startPos, endPos;

	public SourceContext(DocumentText text, int startPos, int endPos) {
		super();
		this.text = text;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public SourcePos getStart() {
		return text.getSourcePos(startPos);
	}

	public SourcePos getEnd() {
		return text.getSourcePos(endPos);
	}

	public String getText() {
		return text.substring(startPos, endPos);
	}

	@Override
	public String toString() {
		if (getStart().equals(getEnd()))
			return getStart().toString();
		else
			return getStart() + " - " + getEnd();
	}

}
