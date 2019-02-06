import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for BST
 *
 *  @version 3.1 09/11/15 11:32:15
 *  @author  Jack O'Sullivan
 */

@RunWith(JUnit4.class)
public class BSTTest {
	private static BST<Integer, Integer> exampleTree() {
		BST<Integer, Integer> bst = new BST<>();
		bst.put(7, 7);		//        _7_
		bst.put(8, 8);		//      /     \
		bst.put(3, 3);		//    _3_      8
		bst.put(1, 1);		//  /     \
		bst.put(2, 2);		// 1       6
		bst.put(6, 6);		//  \     /
		bst.put(4, 4);		//   2   4
		bst.put(5, 5);		//        \
									//         5

		return bst;
	}

	@Test
	public void testEmpty() {
		BST<Integer, Integer> bst = new BST<>();
		assertTrue(bst.isEmpty());

		bst.put(1, 1);
		assertFalse(bst.isEmpty());
	}

	@Test
	public void testContains() {
		assertFalse(new BST<Integer, Integer>().contains(3));

		BST<Integer, Integer> bst = exampleTree();
		assertTrue(bst.contains(4));
		assertFalse(bst.contains(10));
	}

	@Test
	public void testPut() {
		BST<Integer, Integer> bst = new BST<>();
		bst.put(1, 1);
		assertEquals("(()1())", bst.printKeysInOrder());

		bst.put(2, 2);
		assertEquals("(()1(()2()))", bst.printKeysInOrder());

		bst.put(0, 0);
		assertEquals("((()0())1(()2()))", bst.printKeysInOrder());
		bst.put(0, 0);
		assertEquals("((()0())1(()2()))", bst.printKeysInOrder());
	}

	@Test
	public void testHeight() {
		assertEquals(-1, new BST().height());
		assertEquals(4, exampleTree().height());
	}

	@Test
	public void testSelect() {
		assertNull(new BST().median());

		BST<Integer, Integer> bst = exampleTree();
		assertNull(bst.select(-1));
		assertNull(bst.select(bst.size()));

		assertEquals(4, (int)bst.select(3));
		assertEquals(6, (int)bst.select(5));
	}

	@Test
	public void testMedian() {
		BST<Integer, Integer> bst = new BST<>();
		assertNull(bst.median());

		bst.put(7, 7);
		assertEquals(7, (int)bst.median());

		bst = exampleTree();
		assertEquals(4, (int)bst.median());

		bst.delete(4);
		assertEquals(5, (int)bst.median());
	}

	@Test
	public void testPrintKeysInOrder() {
		assertEquals("()", new BST().printKeysInOrder());
		assertEquals("(((()1(()2()))3((()4(()5()))6()))7(()8()))", exampleTree().printKeysInOrder());
	}

	/** <p>Test {@link BST#prettyPrintKeys()}.</p> */
	@Test
	public void testPrettyPrint() {
		BST<Integer, Integer> bst = new BST<>();
		assertEquals("Checking pretty printing of empty tree",
				"-null", bst.prettyPrintKeys());

		String result =
				"-7\n" +
				" |-3\n" +
				" | |-1\n" +
				" | | |-null\n" +
				" | |  -2\n" +
				" | |   |-null\n" +
				" | |    -null\n" +
				" |  -6\n" +
				" |   |-4\n" +
				" |   | |-null\n" +
				" |   |  -5\n" +
				" |   |   |-null\n" +
				" |   |    -null\n" +
				" |    -null\n" +
				"  -8\n" +
				"   |-null\n" +
				"    -null\n";
		assertEquals("Checking pretty printing of non-empty tree", result, exampleTree().prettyPrintKeys());
	}

	/** <p>Test {@link BST#delete(Comparable)}.</p> */
	@Test
	public void testDelete() {
		BST<Integer, Integer> bst = new BST<>();
		bst.delete(1);
		assertEquals("Deleting from empty tree", "()", bst.printKeysInOrder());

		bst.put(123, 123);
		bst.delete(123);
		assertEquals("Deleting the only element in a tree", "()", bst.printKeysInOrder());

		bst = exampleTree();
		assertEquals("Checking order of constructed tree",
				"(((()1(()2()))3((()4(()5()))6()))7(()8()))", bst.printKeysInOrder());

		bst.delete(9);
		assertEquals("Deleting non-existent key",
				"(((()1(()2()))3((()4(()5()))6()))7(()8()))", bst.printKeysInOrder());

		bst.delete(8);
		assertEquals("Deleting leaf", "(((()1(()2()))3((()4(()5()))6()))7())", bst.printKeysInOrder());

		bst.delete(6);
		assertEquals("Deleting node with single child",
				"(((()1(()2()))3(()4(()5())))7())", bst.printKeysInOrder());

		bst.delete(3);
		assertEquals("Deleting node with two children",
				"(((()1())2(()4(()5())))7())", bst.printKeysInOrder());

		bst.delete(4);
		bst.put(1, null);
		assertEquals("Deleting node by put(k, null)",
						"((()2(()5()))7())", bst.printKeysInOrder());
	}
}
