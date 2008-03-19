package exp.syn;

public final class Extraction {
	public final String targetProductionName;

	public final Segment cutSegment;

	public final String extractedNontermName;

	public final String extractedProductionName;

	public final String substituteProductionName;

	public Extraction(String targetProductionName, Segment cutSegment,
			String extractedNontermName, String extractedProductionName,
			String substituteProductionName) {
		this.targetProductionName = targetProductionName;
		this.cutSegment = cutSegment;
		this.extractedNontermName = extractedNontermName;
		this.extractedProductionName = extractedProductionName;
		this.substituteProductionName = substituteProductionName;
	}
}
