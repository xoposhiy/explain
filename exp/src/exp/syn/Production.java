package exp.syn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Production {
	private final Nonterm head;

	private final List<SymbolItem> body;

	private final String name;

	public Production(String name, Nonterm head, Collection<SymbolItem> body) {
		this.name = name;
		this.head = head;
		this.body = new ArrayList<SymbolItem>(body);
	}

	public List<SymbolItem> getBody() {
		return Collections.unmodifiableList(body);
	}

	public String getName() {
		return name;
	}

	public Nonterm getHead() {
		return head;
	}

}
