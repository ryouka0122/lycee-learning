package net.coolblossom.lycee.common.graphics;

/**
 * X座標とY座標を保持したクラス
 * @author ryouka0122@github
 *
 */
public class Point {

	/** X座標 */
	public int x;

	/** Y座標 */
	public int y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
