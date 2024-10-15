package martian.minefactorial.foundation;

public final class Mathematics {
	private Mathematics() { }

	public static boolean pointWithinRectangle(int pointX, int pointY, int rectX, int rectY, int rectWidth, int rectHeight) {
		return pointX >= rectX &&
				pointX < rectX + rectWidth &&
				pointY >= rectY &&
				pointY < rectY + rectHeight;
	}
}
