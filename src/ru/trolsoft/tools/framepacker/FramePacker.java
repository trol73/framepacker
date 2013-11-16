package ru.trolsoft.tools.framepacker;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ru.trolsoft.utils.Console;
import ru.trolsoft.utils.Console.Color;
import ru.trolsoft.utils.Image;
import ru.trolsoft.utils.InvalidArgumentException;
import ru.trolsoft.utils.StrUtils;

public class FramePacker {
	private static final String VERSION = "1.0";


	private static int transparencyLevel = 0;
	

	public static Console con = new Console();


	/**
	 * 
	 */
	static void usage() {
		con.nl();
		con.color(Color.White, true).out("FramePacker. ").reset().out("Version ").color(Color.Green, true).out(VERSION).reset().nl();
		con.out("Copyright (c) Oleg Trifonov, 2012").nl();
		con.color(Color.White, true).out("Usage: ").reset().out("framepacker: <source_image> <frame_width>x<frame_height> <output_image> [options]").nl();
		con.color(Color.White, true).out("Options:").nl();
		con.color(Color.Cyan, true).out("      -t=<transparency>    ").reset().out("set maximum aplha for transparent pixels").nl();
	}

	
	
	
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		if ( args.length < 3 ) {
			usage();
			return;
		}

		String srcFileName = args[0];
		String strFrames = args[1];
		String outFileName = args[2];
		String[] arrFrames = strFrames.split("x");
		if ( arrFrames.length != 2 ) {
			con.error("invalid frames argument: ", strFrames);
			usage();
			return;
		}
		int framesX, framesY;
		try {
			framesX = Integer.parseInt(arrFrames[0]);
			framesY = Integer.parseInt(arrFrames[1]);
		} catch (Exception e) {
			con.error("invalid frames argument: ", strFrames);
			usage();
			return;
		}

		try {
			Map<String, String> argsMap = StrUtils.parseArgsFrom(args, new String[]{"-t"}, 3);
			transparencyLevel = StrUtils.getIntArgValue(argsMap, "-t", 0);
		} catch (InvalidArgumentException e) {
			usage();
			con.error(e.getMessage());
			return;
		}
		
		
		Image img = new Image();
		try {
			img.loadImage(srcFileName);
		} catch (Exception e) {
			con.error("Can't read input file: ", srcFileName);
			return;
		}
		
		int imageArea = img.getWidth()*img.getHeight() ;
		con.info("Source image: ", srcFileName).info(",  ", img.getWidth()).info("x", img.getHeight()).info(" = ", imageArea + " px").nl();
		con.info("Total: ", framesX*framesY + " frames").nl();
		
		if ( img.getWidth() % framesX != 0 ) {
			con.error("invalid image width for specified frames count: ", img.getWidth());
			return;
		}
		if ( img.getHeight() % framesY != 0 ) {
			con.error("invalid image height for specified frames count: ", img.getHeight());
			return;
		}
		int frameWidth = img.getWidth() / framesX;
		int frameHeight = img.getHeight() / framesY;
		int frameArea = frameWidth*frameHeight;  
		con.info("Size of frame: ", frameWidth + "x" + frameHeight + ",  " + frameArea + " px").nl();

		FramePackerTool fpt = new FramePackerTool(img, framesX, framesY, transparencyLevel);
		List<ImageFrame> frames = fpt.getSrcFrames();
		con.info("Frames:").nl();
		int totalFrameSize = 0;
		for ( ImageFrame frame : frames ) {
			con.out("\t" + frame + " ").color(Color.White, true).out(frame.getArea() + " px").reset().nl();
			totalFrameSize += frame.getArea();
		}
		con.info("Total compacted frames size: ", totalFrameSize + " px (" + 100*totalFrameSize/imageArea + "%)").nl();
		con.info("Total frames size delta: ", (imageArea - totalFrameSize) + " px").nl();
		if ( imageArea == totalFrameSize ) {
			con.info("Can't pack frames.").nl();
			return;
		}

		fpt.process();
		Image outImage = fpt.getCompactImage();
		outImage.saveImage(outFileName, Image.FORMAT_PNG);
		
		List<ImageFrame> finalFrames = fpt.getCompactFrames();
		FileOutputStream fs = new FileOutputStream(outFileName + ".xml");
		String s = "frameWidth=\"" + frameWidth + "\" frameHeight=\"" + frameHeight + "\"\n"; 
		fs.write(s.getBytes());
		for ( ImageFrame frm : finalFrames ) {
			s = "\t" + frm.toXmlString() + "\n";
			fs.write(s.getBytes());
		}
		fs.close();
		int outArea = outImage.getWidth()*outImage.getHeight();
		con.info("Output image size: ", outImage.getWidth() + "x" + outImage.getHeight() + ", " + outArea + " px (" + (100*outArea/imageArea) + "%)").nl();
	}
}