package net.coolblossom.lycee.common.graphics;

/**
 * 矩形情報を保持したクラス
 * @author ryouka0122@github
 *
 */
public class Rect {

	/** 左側のX座標 */
	public int left;

	/** 上側のY座標 */
	public int top;

	/** 右側のX座標 */
	public int right;

	/** 下側のY座標 */
	public int bottom;

	public Rect() {
		this(0, 0, 0, 0);
	}

	public Rect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public Rect(Size size) {
		this(0, 0, size.width, size.height);
	}

	public Rect(Point pt1, Point pt2) {
		if(pt1.x<pt2.x) {
			this.left = pt1.x;
			this.right = pt2.x;
		}else{
			this.left = pt2.x;
			this.right = pt1.x;
		}
		if(pt1.y<pt2.y) {
			this.top = pt1.y;
			this.bottom = pt2.y;
		}else{
			this.top = pt2.y;
			this.bottom = pt1.y;
		}
	}

	public Size toSize() {
		return new Size(right-left, bottom-top);
	}

	public Point[] toPoints() {
		return new Point[] {
				new Point(left, top),
				new Point(right, bottom)
		};
	}

}
