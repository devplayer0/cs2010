import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for Doubly Linked List
 *
 * @author Jack O'Sullivan
 * @version 06/10/18 18:40:07
 */
@RunWith(JUnit4.class)
public class DoublyLinkedListTest {
	@Test
	public void testSize() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		assertEquals("size() on an empty list", 0, testList.size());

		testList.insertBefore(0, 123);
		testList.insertBefore(0, 456);
		testList.insertBefore(0, 789);
		assertEquals("size() on a non-empty list", 3, testList.size());
	}

	@Test
	public void testIsEmpty() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		assertTrue("isEmpty() on an empty list", testList.isEmpty());

		testList.insertBefore(0, 123);
		assertFalse("isEmpty() on a non-empty list", testList.isEmpty());
	}

	@Test
	public void testClear() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		testList.insertBefore(0, 123);
		testList.insertBefore(0, 456);
		testList.insertBefore(0, 789);
		testList.clear();

		assertTrue("isEmpty() after clear()", testList.isEmpty());
	}

	/**
	 * Check if the insertBefore works
	 */
	@Test
	public void testInsertBefore() {
		// test non-empty list
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<>();
		testDLL.insertBefore(0, 1);
		testDLL.insertBefore(1, 2);
		testDLL.insertBefore(2, 3);

		testDLL.insertBefore(0, 4);
		assertEquals("Checking insertBefore to a list containing 3 elements at position 0", "4,1,2,3", testDLL.toString());
		testDLL.insertBefore(1, 5);
		assertEquals("Checking insertBefore to a list containing 4 elements at position 1", "4,5,1,2,3", testDLL.toString());
		testDLL.insertBefore(2, 6);
		assertEquals("Checking insertBefore to a list containing 5 elements at position 2", "4,5,6,1,2,3", testDLL.toString());
		testDLL.insertBefore(-1, 7);
		assertEquals("Checking insertBefore to a list containing 6 elements at position -1 - expected the element at the head of the list", "7,4,5,6,1,2,3", testDLL.toString());
		testDLL.insertBefore(7, 8);
		assertEquals("Checking insertBefore to a list containing 7 elemenets at position 8 - expected the element at the tail of the list", "7,4,5,6,1,2,3,8", testDLL.toString());
		testDLL.insertBefore(700, 9);
		assertEquals("Checking insertBefore to a list containing 8 elements at position 700 - expected the element at the tail of the list", "7,4,5,6,1,2,3,8,9", testDLL.toString());

		// test empty list
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(0, 1);
		assertEquals("Checking insertBefore to an empty list at position 0 - expected the element at the head of the list", "1", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(10, 1);
		assertEquals("Checking insertBefore to an empty list at position 10 - expected the element at the head of the list", "1", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(-10, 1);
		assertEquals("Checking insertBefore to an empty list at position -10 - expected the element at the head of the list", "1", testDLL.toString());
	}

	@Test
	public void testGet() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		assertNull("get() on an empty list", testList.get(0));

		testList.insertBefore(0, 1);
		testList.insertBefore(1, 2);
		testList.insertBefore(2, 3);
		testList.insertBefore(3, 4);

		assertNull("get() on index less than 0", testList.get(-1));
		assertNull("get() on index greater than size - 1", testList.get(testList.size()));

		assertEquals("get() on start of list", 1, (int) testList.get(0));
		assertEquals("get() on end of list", 4, (int) testList.get(testList.size() - 1));
		assertEquals("get() on middle of list", 3, (int) testList.get(2));
	}

	@Test
	public void testDeleteAt() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		assertFalse("deleteAt() on empty list", testList.deleteAt(0));

		testList.insertBefore(0, 123);
		assertFalse("deleteAt() with index less than 0", testList.deleteAt(-1));
		assertTrue(testList.deleteAt(0));
		assertEquals("list contents after deleteAt()", "", testList.toString());

		testList.insertBefore(0, 1);
		testList.insertBefore(1, 2);
		testList.insertBefore(2, 3);
		assertTrue(testList.deleteAt(0));
		assertEquals("list contents after deleteAt(0) with 3 elements", "2,3", testList.toString());

		testList.clear();
		testList.insertBefore(0, 1);
		testList.insertBefore(1, 2);
		testList.insertBefore(2, 3);
		assertTrue(testList.deleteAt(2));
		assertEquals("list contents after deleteAt(2) with 3 elements", "1,2", testList.toString());

		testList.clear();
		testList.insertBefore(0, 1);
		testList.insertBefore(1, 2);
		testList.insertBefore(2, 3);
		assertTrue(testList.deleteAt(1));
		assertEquals("list contents after deleteAt(1) with 3 elements", "1,3", testList.toString());
	}

	@Test
	public void testReverse() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		testList.reverse();

		testList.insertBefore(0, 1);
		testList.insertBefore(1, 2);
		testList.insertBefore(2, 3);

		testList.reverse();
		assertEquals("reverse()", "3,2,1", testList.toString());
	}

	@Test
	public void testPush() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		testList.push(1);
		testList.push(2);
		testList.push(3);

		assertEquals("push()", "3,2,1", testList.toString());
	}

	@Test
	public void testPop() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		assertNull("pop() on empty stack", testList.pop());

		testList.push(1);
		testList.push(2);
		testList.push(3);

		assertEquals("pop() on stack of 3 elements", 3, (int) testList.pop());
		assertEquals("list after pop() on stack of 3 elements", "2,1", testList.toString());

		assertEquals("pop() on stack of 2 elements", 2, (int) testList.pop());
		assertEquals("pop() on stack of 1 element", 1, (int) testList.pop());
		assertTrue("stack is empty after 3 pop() on list of 3 elements", testList.isEmpty());
	}

	@Test
	public void testEnqueue() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		testList.enqueue(1);
		testList.enqueue(2);
		testList.enqueue(3);

		assertEquals("enqueue()", "1,2,3", testList.toString());
	}

	@Test
	public void testDequeue() {
		DoublyLinkedList<Integer> testList = new DoublyLinkedList<>();
		assertNull("dequeue() on empty queue", testList.dequeue());

		testList.enqueue(1);
		testList.enqueue(2);
		testList.enqueue(3);

		assertEquals("dequeue() on queue of 3 elements", 1, (int) testList.dequeue());
		assertEquals("dequeue()", "2,3", testList.toString());

		assertEquals("dequeue() on queue of 2 elements", 2, (int) testList.dequeue());
		assertEquals("dequeue() on queue  of 1 element", 3, (int) testList.dequeue());
		assertTrue("queue is empty after 3 pop() on list of 3 elements", testList.isEmpty());
	}

	@Test
	public void testMakeUnique() {
		DoublyLinkedList<String> testList = new DoublyLinkedList<>();
		testList.enqueue("A");
		testList.enqueue("B");
		testList.enqueue(null);
		testList.enqueue("C");
		testList.enqueue("B");
		testList.enqueue("D");
		testList.enqueue(null);
		testList.enqueue("A");

		testList.makeUnique();
		assertEquals("makeUnique()", "A,B,null,C,D", testList.toString());
	}
}
