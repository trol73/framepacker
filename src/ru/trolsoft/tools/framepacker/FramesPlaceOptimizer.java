package ru.trolsoft.tools.framepacker;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ferox.util.geom.text.RectanglePacker;

import ru.trolsoft.utils.Console;
import ru.trolsoft.utils.Image;

/**
 * Version 1.0.0
 * 
 * @author trol
 *
 */
public class FramesPlaceOptimizer {
	private final Image srcImage;
	private final List<ImageFrame> src;
	private Map<ImageFrame, ImageFrame> compact;
	
	static Random rnd = new Random();
	


	/**
	 *
	 */
	public FramesPlaceOptimizer(List<ImageFrame> src, Image srcImage) {
		this.src = src;
		this.srcImage = srcImage;

	}
	
	/**
	 * 
	 * @return
	 */
	public Map<ImageFrame, ImageFrame> compact() {
		RectanglePacker<ImageFrame> rp = new RectanglePacker<ImageFrame>(srcImage.getWidth()-2, srcImage.getHeight());

		int minArea = -1;
		
		int[] minSequence = new int[src.size()];
		int minWidth = 0;
		int minHeight = 0;
		int minWidth0 = 0;
		int minHeight0 = 0;


		long t0 = System.currentTimeMillis();
		int area;

		int startWidth = 0;
		int startHeight = 0;
		for (ImageFrame f : src) {
			if (f.width > startWidth) {
				startWidth = f.width;
			}
			if (f.height > startHeight) {
				startHeight = f.height;
			}
		}
		final int maxSize = (srcImage.getWidth() + srcImage.getHeight())*10;
		final int srcArea = srcImage.getWidth() * srcImage.getHeight();
		
		SequenceGenerator sequenceGenerator = new SequenceGenerator(src);
		for ( SequenceGenerator.SequenceType seqType : SequenceGenerator.SequenceType.values() ) {
			int sequence[] = sequenceGenerator.createSequence(seqType);
			for ( int ww = startWidth; ww < maxSize; ww += 1 ) {
				for ( int hh = startHeight; hh < maxSize; hh += 1 ) {
					int s = ww*hh;
					if ( s >= srcArea || (s >= minArea && minArea > 0) ) {
						continue;
					}
					rp = createRectanglePacker(ww, hh, sequence, minArea);
					if ( rp == null ) {
						continue;
					}
					area = rp.getWidth() * rp.getHeight();
					if ( area < minArea || minArea < 0 ) {
						minArea = area;
						minWidth = rp.getWidth();
						minHeight = rp.getHeight();
						minWidth0 = ww;
						minHeight0 = hh;
						System.arraycopy(sequence, 0, minSequence, 0, sequence.length);
					}
				} /// height
			} // width
			//System.out.println(seqType+". MIN area "+minArea + " " +(srcArea-minArea) + "   "+minWidth+"x"+minHeight + "   |"+minWidth0+"x"+minHeight0);
		} // seqType
System.out.println("------");
		//long t01 = System.currentTimeMillis();
		//System.out.println("Minimization time: " + (t01-t0)/1000.0 + " ms");
		
		
		int loop1 = minSequence.length;
		int loop2 = minSequence.length;
		for ( int rndIndex1 = 0 ; rndIndex1 < loop1; rndIndex1++ ) {
			int sequence[] = new int[minSequence.length];
			System.arraycopy(minSequence, 0, sequence, 0, minSequence.length);
			for ( int rndIndex2 = 0 ; rndIndex2 < loop2; rndIndex2++ ) {
				int i1 = random(minSequence.length);
				int i2 = random(minSequence.length);
				if ( i1 == i2 ) {
					continue;
				}
				int t = sequence[i1];
				sequence[i1] = sequence[i2];
				sequence[i2] = t;

				final int dw = 10*minWidth0/100;
				final int dh = 10*minHeight0/100;
				final int ds = dw > dh ? dw : dh;

				int fromW = minWidth0 - ds;
				int fromH = minHeight0 - ds;
				int toW = minWidth0 + ds;
				int toH = minHeight0 + ds;
				if ( fromW < 0 ) {
					fromW = 0;
				}
				if ( fromH < 0 ) {
					fromH = 0;
				}
				for ( int ww = fromW; ww <= toW; ww += 1 ) {
					for ( int hh = fromH; hh < toH; hh += 1 ) {
						int s = ww*hh;
						if ( s >= srcArea || (s >= minArea && minArea > 0) ) {
							continue;
						}
						rp = createRectanglePacker(ww, hh, sequence, minArea);
						if ( rp == null ) {
							continue;
						}
						area = rp.getWidth() * rp.getHeight();
						if ( area < minArea || minArea < 0 ) {
							minArea = area;
							minWidth = rp.getWidth();
							minHeight = rp.getHeight();
							minWidth0 = ww;
							minHeight0 = hh;
							System.arraycopy(sequence, 0, minSequence, 0, sequence.length);
							//System.out.println("RND. MIN area "+minArea + " " +(srcArea-minArea) + "   "+minWidth+"x"+minHeight + "   |"+minWidth0+"x"+minHeight0);
							new Console().info("Random min area found: ", minWidth+"x"+minHeight + " px").nl();
						}
					} /// height
				} // width
			}
			
		}
		
		long t1 = System.currentTimeMillis();
		new Console().info("Minimization time: ", (t1-t0)/1000.0 + " ms").nl();
		
		// create image
		rp = new RectanglePacker<ImageFrame>(minWidth0, minHeight0);
		for ( int i = 0; i < minSequence.length; i++ ) {
			ImageFrame frame = src.get(minSequence[i]);
			rp.insert(frame, frame.width, frame.height);
		}
		compact = new HashMap<ImageFrame, ImageFrame>();
		for ( ImageFrame srcFrame : src ) {
			RectanglePacker.Rectangle rect = rp.get(srcFrame);
			compact.put(srcFrame, new ImageFrame(rect.getX(), rect.getY(), srcFrame.width, srcFrame.height, srcFrame.anchorX, srcFrame.anchorY));
		}
	
		return compact;
	}
	
	
	/**
	 * 
	 * @param max
	 * @return
	 */
	private static int random(int max) {
		int result = rnd.nextInt() % max;
		return result < 0 ? -result : result;
	}
	
	
	/**
	 * 
	 * @param width0
	 * @param height0
	 * @param sequence
	 * @param minArea
	 * @return
	 */
	private RectanglePacker<ImageFrame> createRectanglePacker(int width0, int height0, int[] sequence, int minArea) {
		RectanglePacker<ImageFrame> rp = new RectanglePacker<ImageFrame>(width0, height0);
		for ( int i = 0; i < sequence.length; i++ ) {
			ImageFrame frame = src.get(sequence[i]);
			rp.insert(frame, frame.width, frame.height);
			if ( rp.getWidth()*rp.getHeight() >= minArea && minArea > 0 ) {
				//return rp;
				return null;
			}
		}
		return rp;
	}
	
	

	/**
	 * 
	 * @param srcImage
	 * @return
	 */
	public Image generateImage() {
		int width = 0;
		int height = 0;
		for ( ImageFrame inFrame : compact.keySet() ) {
			ImageFrame outFrame = compact.get(inFrame);
			int w = outFrame.x + outFrame.width; 
			if ( w > width ) {
				width = w;
			}
			int h = outFrame.y + outFrame.height;
			if ( h > height ) {
				height = h;
			}
		}
		Image img = new Image(width, height, Image.TYPE_INT_ARGB);
		for ( ImageFrame inFrame : compact.keySet() ) {
			ImageFrame outFrame = compact.get(inFrame);
			img.drawImage(srcImage, inFrame.x, inFrame.y, new Rectangle(outFrame.x, outFrame.y, outFrame.width, outFrame.height));
		}
		return img;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ImageFrame> getCompactFrames() {
		List<ImageFrame> result = new ArrayList<ImageFrame>();
		for ( ImageFrame inFrame : src ) {
			ImageFrame outFrame = compact.get(inFrame);
			ImageFrame frame = new ImageFrame(outFrame.x, outFrame.y, outFrame.width, outFrame.height, inFrame.anchorX, inFrame.anchorY);
			result.add(frame);
		}
		return result;
	}

}
