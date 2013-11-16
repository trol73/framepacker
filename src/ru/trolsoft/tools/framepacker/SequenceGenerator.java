package ru.trolsoft.tools.framepacker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SequenceGenerator {
	
	private final List<ImageFrame> frames;
	
	
	enum SequenceType {
		AreaDec,
		WidthDec,
		HeightDec
	}

	/**
	 * 
	 * @param frames
	 */
	public SequenceGenerator(List<ImageFrame> frames) {
		this.frames = frames;
	}

	
	/**
	 * 
	 * @param seqType
	 * @return
	 */
	public int[] createSequence(SequenceType seqType) {
		switch ( seqType ) {
			case AreaDec:
				return getSequenceAreaDec();
			case WidthDec:
				return getSequenceWidthDec();
			case HeightDec:
				return getSequenceHeightDec();
		}
		throw new RuntimeException("invalid sequence type");
	}

	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private int[] getSequence(Comparator<Integer> c) {
		Integer sa[] = new Integer[frames.size()];
		for ( int i = 0; i < sa.length; i++ ) {
			sa[i] = i;
		}
		Arrays.sort(sa, 0, sa.length, c);
		int[] result = new int[sa.length];
		for ( int i = 0; i < sa.length; i++) {
			result[i] = sa[i];
		}
		return result;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private int[] getSequenceAreaDec() {
		return getSequence(new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				int a1 = frames.get(i1).getArea();
				int a2 = frames.get(i2).getArea();
				if ( a1 > a2 ) {
					return -1;
				} else if ( a1 < a2 ) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}
	
	
	/**
	 * 
	 * @return
	 */
	private int[] getSequenceWidthDec() {
		return getSequence(new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				int a1 = frames.get(i1).width;
				int a2 = frames.get(i2).width;
				if ( a1 > a2 ) {
					return -1;
				} else if ( a1 < a2 ) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	
	/**
	 * 
	 * @return
	 */
	private int[] getSequenceHeightDec() {
		return getSequence(new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				int a1 = frames.get(i1).height;
				int a2 = frames.get(i2).height;
				if ( a1 > a2 ) {
					return -1;
				} else if ( a1 < a2 ) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}
	

}
