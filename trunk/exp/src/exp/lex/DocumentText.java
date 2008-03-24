package exp.lex;

public final class DocumentText {
	private int[] lineOffsets = null;

	private int lines = 0;

	private final String text;

	public DocumentText(String text) {
		if (text == null)
			throw new IllegalArgumentException("text = null");
		this.text = text;
	}

	public SourcePos getSourcePos(int pos) {
		if (pos < 0 || pos >= text.length())
			throw new IllegalArgumentException("pos");
		int index = findLineIndex(pos);
		return new SourcePos(index, Math.max(0, pos - lineOffsets[index] - 1));
	}

	public String getText() {
		return text;
	}

	public String substring(int startPos, int endPos) {
		return text.substring(startPos, endPos);
	}

	private void addOffset(int pos) {
		assert lineOffsets != null;
		assert lines >= 0;
		final int len = lineOffsets.length;
		if (lines >= len) {
			int[] buffer = new int[2 * (len > 0 ? len : 10)];
			assert buffer.length > lineOffsets.length;
			System.arraycopy(buffer, 0, lineOffsets, 0, len);
			lineOffsets = buffer;
		}
		lineOffsets[lines++] = pos;
	}

	private void calcLineOffsets() {
		lineOffsets = new int[text.length() / 10 + 1];
		lines = 0;
		addOffset(0);
		final int len = text.length();
		for (int i = 0; i < len; i++) {
			char ch = text.charAt(i);
			if (ch == '\n')
				addOffset(i);
			else if (ch == '\r') {
				if (i + 1 < len && text.charAt(i + 1) == '\n')
					i++;
				addOffset(i);
			}
		}
		addOffset(len);
		assert lineOffsets != null && lines > 0;
	}

	private int findLineIndex(int pos) {
		if (lineOffsets == null)
			calcLineOffsets();
		int left = 0;
		int right = lines - 1;
		assert pos >= lineOffsets[left] && pos < lineOffsets[right] : pos + " " + lineOffsets[left] + " "
				+ lineOffsets[right] + " " + right;
		assert right > left;
		while (right - left > 1) {
			final int index = (left + right) / 2;
			if (lineOffsets[index] > pos)
				right = index;
			else
				left = index;
			assert pos >= lineOffsets[left] && pos < lineOffsets[right];
			assert right > left;
		}
		assert pos >= lineOffsets[left] && pos < lineOffsets[left + 1];
		return left;
	}

}
