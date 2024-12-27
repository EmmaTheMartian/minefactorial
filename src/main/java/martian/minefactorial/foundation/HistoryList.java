package martian.minefactorial.foundation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A helper class for storing and managing a list representing history.
 * <p>
 * {@see TweakerulerHistory} for a usage example.
 */
public class HistoryList<T> {
	protected Stack<T> redoHistory = new Stack<>();
	protected List<T> data = new ArrayList<>();

	/**
	 * The maximum combined length of each list.
	 */
	protected int maxLength;

	public HistoryList(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Clears all data and history from this list.
	 */
	public void clear() {
		data.clear();
		redoHistory.clear();
	}

	public int dataSize() {
		return data.size();
	}

	public int historySize() {
		return redoHistory.size();
	}

	/**
	 * Pushes an item onto the history. This will remove elements after the index, if
	 * there are any.
	 *
	 * @param it The item to add.
	 */
	public void push(T it) {
		data.add(it);

		// Clear any now-invalid redo history.
		if (!redoHistory.isEmpty()) {
			redoHistory.clear();
		}

		// If we exceed the history, remove the oldest item on the list.
		if (data.size() > maxLength) {
			data.removeFirst();
		}
	}

	public boolean canUndo() {
		return !data.isEmpty();
	}

	public void undo() {
		if (canUndo()) {
			T undone = data.removeLast();
			onUndo(undone);
			redoHistory.push(undone);
		}
	}

	public boolean canRedo() {
		return !redoHistory.isEmpty();
	}

	public void redo() {
		if (canRedo()) {
			T redone = redoHistory.pop();
			onRedo(redone);
			data.add(redone);
		}
	}

	protected void onUndo(T undoneItem) {
	}

	protected void onRedo(T redoneItem) {
	}
}
