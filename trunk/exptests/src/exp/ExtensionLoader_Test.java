package exp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import junit.framework.TestCase;
import exp.lex.DocumentReader;
import exp.lex.Term;
import exp.syn.Extension;
import exp.syn.Nonterm;
import exp.syn.Production;
import exp.syn.SymbolItem;
import exp.syn.SyntaxException;
import exp.syn.loader.ExtensionLoader;

public class ExtensionLoader_Test extends TestCase {
	public void test() throws SyntaxException, IOException {
		FakeNamesResolver resolver = new FakeNamesResolver();
		resolver.addLexicon(new FakeLexicon());
		ExtensionLoader loader = new ExtensionLoader(resolver);
		InputStream s = this.getClass().getResourceAsStream("ExtensionLoader_Test.gr");
		Extension ext = loader.parseExtension(new DocumentReader(new InputStreamReader(s)));
		assertEquals("syn.name", ext.getName());
		Iterator<Production> iprods = ext.getProductions().iterator();
		Production prod = iprods.next();
		assertEquals("syn.name.R1", prod.getName());
		assertEquals(Nonterm.axiom, prod.getHead());
		Iterator<SymbolItem> ibody = prod.getBody().iterator();
		checkBody("A", true, false, ibody.next());
		checkBody("used.syntax2.B", false, false, ibody.next());
		checkBody("c", false, true, ibody.next());
		assertFalse(ibody.hasNext());
		
		prod = iprods.next();
		assertEquals("syn.name.R2", prod.getName());
		assertEquals("A", prod.getHead().getName());
		ibody = prod.getBody().iterator();
		assertFalse(ibody.hasNext());
		
		prod = iprods.next();
		assertEquals("syn.name.R3", prod.getName());
		assertEquals("used.syntax2.B", prod.getHead().getName());
		ibody = prod.getBody().iterator();
		checkBody("used.lexicon2.b", true, true, ibody.next());
		checkBody("d", false, true, ibody.next());
		assertFalse(ibody.hasNext());
	}

	private void checkBody(String name, boolean visible, boolean terminal, SymbolItem item) {
		assertEquals(name, item.sym.getName());
		assertEquals(visible, item.visible);
		assertEquals(terminal, item.sym instanceof Term);
	}
}
