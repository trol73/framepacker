package ru.trolsoft.tools.framepacker;

import java.util.ArrayList;
import java.util.List;

import ru.trolsoft.utils.Image;

/**
 * Version 1.0.0
 * 
 * @author trol
 *
 */
public class FramePackerTool {
	private Image imgSrc;
	private int transparencyLevel;
	private List<ImageFrame> srcFrames;
	private List<ImageFrame> outFrames;
	private Image imgOut;
	
	/**
	 * 
	 * @param imgSrc
	 * @param srcFramesX
	 * @param srcFramesY
	 */
	public FramePackerTool(Image imgSrc, int srcFramesX, int srcFramesY, int transparencyLevel) {
		this.imgSrc = imgSrc;
		this.transparencyLevel = transparencyLevel;
		int frameWidth = imgSrc.getWidth() / srcFramesX;
		int frameHeight = imgSrc.getHeight() / srcFramesY;		
		srcFrames = findAllCompactFrames(imgSrc, frameWidth, frameHeight);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ImageFrame> getSrcFrames() {
		return srcFrames;
	}
	

	/**
	 * 
	 */
	public void process() {
		FramesPlaceOptimizer compact = new FramesPlaceOptimizer(srcFrames, imgSrc);
		compact.compact();
		imgOut = compact.generateImage();
		outFrames = compact.getCompactFrames();
	}
	
	/**
	 * 
	 * @return
	 */
	public Image getCompactImage() {
		return imgOut;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<ImageFrame> getCompactFrames() {
		return outFrames;
	}
	 
	
	/**
	 * Decomposes source image into its frame with cropped transparent space 
	 *  
	 * @param img
	 * @param frameWidth
	 * @param frameHeight
	 * @return
	 */
	private List<ImageFrame> findAllCompactFrames(Image img, int frameWidth, int frameHeight) {
		List<ImageFrame> result = new ArrayList<ImageFrame>();
		if ( img.getWidth() % frameWidth != 0 ) {
			throw new RuntimeException("invalid image width");
		}		
		if ( img.getHeight() % frameHeight != 0 ) {
			throw new RuntimeException("invalid image height");
		}
		int framesXcnt = img.getWidth() / frameWidth;
		int framesYcnt = img.getHeight() / frameHeight;
		for ( int fy = 0; fy < framesYcnt; fy++ ) {
			for ( int fx = 0; fx < framesXcnt; fx++ ) {
				ImageFrame frm = getCompactImageFrame(img, fx*frameWidth, fy*frameHeight, frameWidth, frameHeight); 
				if ( frm.getArea() > 0 ) {
					result.add(frm);
				}
			}
		}
		return result;
	}
	
	
	
	/**
	 * Cropped transparent space for image area and return the new compact area
	 * 
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	private ImageFrame getCompactImageFrame(Image img, int x, int y, int width, int height) {
		int x0 = x;
		int y0 = y;
		int w0 = width;
		int h0 = height;
		int ancX = 0;
		int ancY = 0;
		
		// search empty space left
		for ( int xx = x0; xx < x0+w0; xx++ ) {
			if ( !isImageAreaEmpty(img, xx, y0, 1, h0) ) {
				break;
			}
			x0++;
			w0--;
			ancX--;
		}
		// search empty space top
		for ( int yy = y0; yy < y0+h0; yy++ ) {
			if ( !isImageAreaEmpty(img, x0, yy, w0, 1) ) {
				break;
			}
			y0++;
			h0--;
			ancY--;
		}
		// search empty space right
		for ( int xx = x0+w0-1; xx >= x0; xx-- ) {
			if ( !isImageAreaEmpty(img, xx, y0, 1, h0) ) {
				break;
			}
			w0--;
		}
		// search empty space bottom
		for ( int yy = y0+h0-1; yy >= y0; yy-- ) {
			if ( !isImageAreaEmpty(img, x0, yy, w0, 1) ) {
				break;
			}
			h0--;
		}
		return new ImageFrame(x0, y0, w0, h0, ancX, ancY);
	}
	
	
	/**
	 * 
	 * @param img
	 * @param frame
	 * @return
	 */
	private ImageFrame getCompactImageFrame(Image img, ImageFrame frame) {
		return getCompactImageFrame(img, frame.x, frame.y, frame.width, frame.height);
	}
	
	
	/**
	 * 
	 * @param img
	 * @param x0
	 * @param y0
	 * @param width
	 * @param height
	 * @return
	 */
	private boolean isImageAreaEmpty(Image img, int x0, int y0, int width, int height) {
		for ( int x = x0; x < x0 + width; x++ ) {
			for ( int y = y0; y < y0 + height; y++ ) {
				if ( !isPixelTranparent(img, x, y) ) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param img
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isPixelTranparent(Image img, int x, int y) {
		return img.getPixelAlpha(x, y) <= transparencyLevel ;
	}
	
}
