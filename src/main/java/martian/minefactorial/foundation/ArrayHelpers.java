package martian.minefactorial.foundation;

public final class ArrayHelpers {
	private ArrayHelpers() { }

	public static int[] rangeOf(int min, int max, int step) {
		assert max > min;
		int[] range = new int[max - min];
		int i = 0;
		for (int value = min; value < max; value += step) {
			range[i++] = value;
		}
		return range;
	}

	public static int[] rangeOf(int min, int max) {
		return rangeOf(min, max, 1);
	}

	public static int[] rangeOf(int max) {
		return rangeOf(0, max, 1);
	}

	public static <T> boolean contains(T[] array, T entry) {
		for (T it : array) {
			if (it == entry) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(int[] array, int entry) {
		for (int it : array) {
			if (it == entry) {
				return true;
			}
		}
		return false;
	}
}
