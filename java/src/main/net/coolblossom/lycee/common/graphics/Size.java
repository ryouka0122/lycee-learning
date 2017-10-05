package net.coolblossom.lycee.common.graphics;

/**
 * 縦幅と横幅の値を保持するサイズクラス
 * @author ryouka0122@github
 *
 */
public class Size {
	/** 幅 */
	public int width;

	/** 高さ */
	public int height;

	public Size() {
		this(0, 0);
	}

	public Size(int sz) {
		this(sz, sz);
	}

	public Size(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public Size(Rect rect) {
		this.width = rect.right - rect.left ;
		this.height = rect.bottom - rect.top;
	}

	public Size(Point pt1, Point pt2) {
		if(pt1.x < pt2.x) {
			this.width = pt2.x - pt1.x;
		}else{
			this.width = pt1.x - pt2.x;
		}
		if(pt1.y < pt2.y) {
			this.height = pt2.y - pt1.y;
		}else{
			this.height = pt1.y - pt2.y;
		}
	}

}
