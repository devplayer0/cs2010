/**
 * This class contains the methods of Doubly Linked List.
 *
 * @author Jack O'Sullivan
 * @version 06/10/18 18:30:16
 */

/**
 * Class DoublyLinkedList: implements a *generic* Doubly Linked List.
 * @param <T> This is a type parameter. T is used as a class name in the
 * definition of this class.
 *
 * When creating a new DoublyLinkedList, T should be instantiated with an
 * actual class name that extends the class Comparable.
 * Such classes include String and Integer.
 *
 * For example to create a new DoublyLinkedList class containing String data:
 *    DoublyLinkedList<String> myStringList = new DoublyLinkedList<String>();
 *
 * The class offers a toString() method which returns a comma-separated sting of
 * all elements in the data structure.
 *
 * This is a bare minimum class you would need to completely implement.
 * You can add additional methods to support your code. Each method will need
 * to be tested by your jUnit tests -- for simplicity in jUnit testing
 * introduce only public methods.
 */
class DoublyLinkedList<T extends Comparable<T>> {
	/**
	 * private class DLLNode: implements a *generic* Doubly Linked List node.
	 */
	private class DLLNode {
		final T data; // this field should never be updated. It gets its
		// value once from the constructor DLLNode.
		DLLNode next;
		DLLNode prev;

		/**
		 * Constructor
		 * @param theData : data of type T, to be stored in the node
		 * @param prevNode : the previous Node in the Doubly Linked List
		 * @param nextNode : the next Node in the Doubly Linked List
		 */
		DLLNode(T theData, DLLNode prevNode, DLLNode nextNode) {
			data = theData;
			prev = prevNode;
			next = nextNode;
		}
	}

	// Fields head and tail point to the first and last nodes of the list.
	private DLLNode head, tail;
	private int size;

	/**
	 * Get the number of elements in the linked list
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification: Reading a value from is Θ(1)
	 *
	 * @return the number of elements
	 */
	public int size() {
		return size;
	}

	/**
	 * Tests if the doubly linked list is empty
	 * @return true if list is empty, and false otherwise
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification: size() takes Θ(1), as do comparisons
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Remove all elements in the linked list
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification: Assigning values takes Θ(1)
	 */
	public void clear() {
		head = tail = null;
		size = 0;
	}


	/**
	 * Returns the data stored at a particular position
	 * @param pos : the position
	 * @return the node at pos, if pos is within the bounds of the list, and null otherwise.
	 *
	 * Worst-case asymptotic running time cost: Θ(n)
	 *
	 * Justification:
	 *  This method performs a number of operations (comparisons, assignments) which take Θ(1),
	 *  but since it must seek to the desired position (since this is a linked list and not an array),
	 *  up to n/2 steps will be taken in the worst case (a doubly linked list can start the search at
	 *  either end based on the position, reducing the number of required elements to search) which simplifies
	 *  to n (where n is the number of elements in the list)
	 */
	private DLLNode getNode(int pos) {
		if (pos < 0 || pos >= size) {
		    return null;
		}

		boolean backwards = pos >= size - pos;
		DLLNode current;
		if (backwards) {
			current = tail;
			for (int i = size - 1; i != pos; i--) {
				current = current.prev;
			}
		} else {
			current = head;
			for (int i = 0; i != pos; i++) {
				current = current.next;
			}
		}
		return current;
	}
	/**
	 * Inserts an element in the doubly linked list
	 * @param pos : The integer location at which the new data should be
	 *      inserted in the list. We assume that the first position in the list
	 *      is 0 (zero). If pos is less than 0 then add to the head of the list.
	 *      If pos is greater or equal to the size of the list then add the
	 *      element at the end of the list.
	 * @param data : The new data of class T that needs to be added to the list
	 *
	 * Worst-case asymptotic running time cost: Θ(n)
	 *
	 * Justification:
	 *   This method makes use of a number of operations which take only Θ(1), but
	 *   calls getNode(), which takes Θ(n)
	 */
	public void insertBefore(int pos, T data) {
		if (pos < 0) {
			pos = 0;
		}

		if (pos >= size) {
			DLLNode toInsert = new DLLNode(data, tail, null);
			if (tail != null) {
				tail.next = toInsert;
			} else {
				head = toInsert;
			}
			tail = toInsert;
		} else {
			DLLNode before = getNode(pos);
			DLLNode toInsert = new DLLNode(data, before.prev, before);

            if (before.prev != null) {
                before.prev.next = toInsert;
            }
            before.prev = toInsert;
			if (pos == 0) {
				head = toInsert;
			}
		}

	    size++;
	}

	/**
	 * Returns the data stored at a particular position
	 * @param pos : the position
	 * @return the data at pos, if pos is within the bounds of the list, and null otherwise.
	 *
	 * Worst-case asymptotic running time cost: Θ(n)
	 *
	 * Justification:
	 *  This method simply retrieves the data in a node obtained via getNode(), which takes Θ(n)
	 */
	public T get(int pos) {
		DLLNode node = getNode(pos);
	    return node != null ? node.data : null;
	}

	/**
	 * Removes the node provided from the list
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification:
	 *  Since this method merely manipulates the links of the neighbouring elements
	 *  of an already located provided node, Θ(1) is achieved
	 */
	private void deleteNode(DLLNode toDelete) {
		if (toDelete != head) {
			toDelete.prev.next = toDelete.next;
		} else {
			head = toDelete.next;
		}
		if (toDelete != tail) {
			toDelete.next.prev = toDelete.prev;
		} else {
			tail = toDelete.prev;
		}

		size--;
	}
	/**
	 * Deletes the element of the list at position pos.
	 * First element in the list has position 0. If pos points outside the
	 * elements of the list then no modification happens to the list.
	 * @param pos : the position to delete in the list.
	 * @return true : on successful deletion, false : list has not been modified.
	 *
	 * Worst-case asymptotic running time cost: Θ(n)
	 *
	 * Justification:
	 *  Although deleteNode() only takes Θ(1), the node to delete must first be found,
	 *  which through getNode() takes Θ(n)
	 */
	public boolean deleteAt(int pos) {
		if (pos < 0 || pos >= size) {
			return false;
		}

		deleteNode(getNode(pos));
		return true;
	}

	/**
	 * Reverses the list.
	 * If the list contains "A", "B", "C", "D" before the method is called
	 * Then it should contain "D", "C", "B", "A" after it returns.
	 *
	 * Worst-case asymptotic running time cost: Θ(n)
	 *
	 * Justification:
	 *  The entire list must be traversed in order to swap the prev and next
	 *  links, requiring Θ(n)
	 *
	 *  Note that if this method merely switched a flag (e.g. "reversed") when called,
	 *  any traversing the list could start from the end of the list if the flag was
	 *  set (at no increase to the worst-case asymptotic running time cost) and this
	 *  method would then take Θ(1)
	 */
	public void reverse() {
		if (isEmpty()) {
			return;
		}

		for (DLLNode current = head; current != null; current = current.prev) {
			DLLNode tmp = current.next;
			current.next = current.prev;
			current.prev = tmp;
		}

		DLLNode tmp = head;
		head = tail;
		tail = tmp;
	}

	/**
	 * Removes all duplicate elements from the list.
	 * The method should remove the _least_number_ of elements to make all elements unique.
	 * If the list contains "A", "B", "C", "B", "D", "A" before the method is called
	 * Then it should contain "A", "B", "C", "D" after it returns.
	 * The relative order of elements in the resulting list should be the same as the starting list.
	 *
	 * Worst-case asymptotic running time cost: Θ(n^2)
	 *
	 * Justification:
	 *  Since this list searches linearly for duplicates of each element in the list, a nested loop
	 *  is used, taking Θ(n^2)
	 */
	public void makeUnique() {
		for (DLLNode current = head; current != null; current = current.next) {
			for (DLLNode duplicate = current.next; duplicate != null; duplicate = duplicate.next) {
			    if (current.data == null && duplicate.data == null) {
			    	deleteNode(duplicate);
				} else if (current.data != null && duplicate.data != null && current.data.compareTo(duplicate.data) == 0) {
			    	deleteNode(duplicate);
				}
			}
		}
	}


	/*----------------------- STACK API
	 * If only the push and pop methods are called the data structure should behave like a stack.
	 */

	/**
	 * This method adds an element to the data structure.
	 * How exactly this will be represented in the Doubly Linked List is up to the programmer.
	 * @param item : the item to push on the stack
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification:
	 *  Although this method calls insertBefore(), which has worst case running time of Θ(n),
	 *  since the element is to be inserted at the beginning of the list, insertBefore() will
	 *  always take Θ(1) here
	 */
	public void push(T item) {
		insertBefore(0, item);
	}

	/**
	 * This method returns and removes the element that was most recently added by the push method.
	 * @return the last item inserted with a push; or null when the list is empty.
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification: Similarly to push(), although getNode() takes Θ(n) in the worst case,
	 * since the element being removed is always at the front, it will always take Θ(1)
	 */
	public T pop() {
		if (size == 0) {
			return null;
		}

		DLLNode item = getNode(0);
		deleteNode(item);
		return item.data;
	}

	/*----------------------- QUEUE API
	 * If only the enqueue and dequeue methods are called the data structure should behave like a FIFO queue.
	 */

	/**
	 * This method adds an element to the data structure.
	 * How exactly this will be represented in the Doubly Linked List is up to the programmer.
	 * @param item : the item to be enqueued to the stack
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification:
	 *  Identical to push(), since this is a doubly-linked list
	 */
	public void enqueue(T item) {
	    insertBefore(size, item);
	}

	/**
	 * This method returns and removes the element that was least recently added by the enqueue method.
	 * @return the earliest item inserted with an equeue; or null when the list is empty.
	 *
	 * Worst-case asymptotic running time cost: Θ(1)
	 *
	 * Justification:
	 *   This method delegates to pop() since the functionality is the same
	 */
	public T dequeue() {
		return pop();
	}


	/**
	 * @return a string with the elements of the list as a comma-separated
	 * list, from beginning to end
	 *
	 * Worst-case asymptotic running time cost: Θ(n)
	 *
	 * Justification:
	 *  We know from the Java documentation that StringBuilder's append() method runs in Θ(1) asymptotic time.
	 *  We assume all other method calls here (e.g., the iterator methods above, and the toString method) will execute in Theta(1) time.
	 *  Thus, every one iteration of the for-loop will have cost Theta(1).
	 *  Suppose the doubly-linked list has 'n' elements.
	 *  The for-loop will always iterate over all n elements of the list, and therefore the total cost of this method will be n*Theta(1) = Theta(n).
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		boolean isFirst = true;

		// iterate over the list, starting from the head
		for (DLLNode iter = head; iter != null; iter = iter.next) {
			if (!isFirst) {
				s.append(",");
			} else {
				isFirst = false;
			}
			s.append(iter.data != null ? iter.data.toString() : "null");
		}

		return s.toString();
	}
}


