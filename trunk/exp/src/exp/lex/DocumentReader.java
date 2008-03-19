package exp.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class DocumentReader {
	private int pos = 0;

	private final DocumentText text;

	private final int length;

	public DocumentReader(DocumentText text) {
		if (text == null)
			throw new IllegalArgumentException("text = null");
		this.text = text;
		this.length = text.getText().length();
	}

	public DocumentReader(String input) {
		this(new DocumentText(input));
	}
	
	public DocumentReader(BufferedReader r) throws IOException {
		this(readAll(r));
	}

	public DocumentReader(Reader r) throws IOException {
		this(new BufferedReader(r));
	}

	private static String readAll(BufferedReader r) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}

	public boolean hasMore() {
		return pos < length;
	}

	public char getNext() {
		return text.getText().charAt(pos++);
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public DocumentText getText() {
		return text;
	}

	public char peekNext() {
		return text.getText().charAt(pos);
	}

	public SourcePos getSourcePos() {
		return text.getSourcePos(pos);
	}

}
