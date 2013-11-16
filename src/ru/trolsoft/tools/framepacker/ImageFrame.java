package ru.trolsoft.tools.framepacker;

public class ImageFrame {
	public int x;
	public int y;
	public int width;
	public int height;
	public int anchorX;
	public int anchorY;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public ImageFrame(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param anchorX
	 * @param anchorY
	 */
	public ImageFrame(int x, int y, int width, int height, int anchorX, int anchorY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}

	
	/**
	 * 
	 * @return
	 */
	public int getArea() {
		return width*height;
	}
	
	@Override
	public String toString() {
		return "[ x=" + x + " y=" + y + " width=" + width + " height=" + height + " anchorX=" + anchorX + " anchorY=" + anchorY + " ]";
	}

	public String toXmlString() {
		return "<frame x=\"" + x +"\" y=\"" +y + "\" width=\"" + width + "\" height=\"" + height + "\" anchorX=\"" + anchorX + "\" anchorY=\"" + anchorY+"\" />";
	}
}
