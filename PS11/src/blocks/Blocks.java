package blocks;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * A program which builds the tallest tower possible out of a collection
 * of rectangular blocks
 * 
 *  A block can be stacked on top of another block if and only if the
 *  two dimensions on the base of the top block are smaller than the two dimensions on
 *  the base of the lower block
 * 
 * @authors Kristine Cho and Mercy Bickell
 * @version 3/12/20
 *
 */
public class Blocks {
	
	/**
	 * runs the algorithm to find the tallest possible tower using the given blocks
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File infile = new File(args[0]);
		File outfile = new File(args[1]);
		Blocks runner = new Blocks();
		
		try {
			Scanner readIn = new Scanner(infile); //to read in from infile
			int arrayLength= readIn.nextInt();
			Block[] blockTypes = new Block[arrayLength];
			
			//read in the blocks from the file
			for(int i = 0; i < arrayLength; i++) {
				String currentBlock = readIn.next();
				String[] dimensions = currentBlock.split(" ");
				int w = Integer.parseInt(dimensions[0]);
				int l = Integer.parseInt(dimensions[1]);
				int h = Integer.parseInt(dimensions[2]);
				blockTypes[i] = runner.new Block(w, l, h);
			}
			
			ArrayList<Block> enumBlock = runner.getVariations(blockTypes);
			//now do stuff with all the possible orientations of the blocks
			//sort the blocks?
			
			readIn.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile)); //to write to outfile
			
			writer.close();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * method which, given a list of blocks, returns a list with all possible block
	 * configurations by looping through each given block, finding its 3 rotations,
	 * and adding it to our list 
	 * 
	 * @param inputBlocks
	 * @return
	 */
	public ArrayList<Block> getVariations(Block[] inputBlocks){
		ArrayList<Block> allBlocks = new ArrayList<Block>();
		for(int i = 0; i < inputBlocks.length; i++) {
			allBlocks.add(inputBlocks[i].rotation1());
			allBlocks.add(inputBlocks[i].rotation2());
			allBlocks.add(inputBlocks[i].rotation3());
		}
		return new ArrayList<Block>();
	}
	
	/**
	 * Class block to represent rectangular building blocks
	 * with base dimensions of length x width and a height dimension
	 * 
	 */
	class Block{
		int length;
		int width;
		int height;
		
		/**
		 * length should be <= width
		 * 
		 * @param length
		 * @param width
		 * @param height
		 */
		public Block(int length, int width,int height) {
			this.length = length;
			this.width = width;
			this.height = height;
		}
		
		public int length() {
			return length;
		}
		
		public int width() {
			return width;
		}
		
		public int height() {
			return height;
		}
		
		/**
		 * given a block, returns true if this block is stackable on top of
		 * the other block
		 */
		public boolean stackableOn(Block other) {
			return (width < other.width() && length < other.length());
		}
		
		/*
		 * returns a new block rotated such that the 1st value inputed
		 * represents the height and
		 * 
		 * the other two dimensions, the base dimensions, are placed
		 * such that the 1st dimension <= 2nd dimension
		 */
		public Block rotation1() {
			if(height >= width) {
				return new Block(width, height, length);
			}
			return new Block(height, width, length);
		}
		
		/*
		 * returns a new block rotated such that the 2nd value inputed
		 * represents the height and
		 * 
		 * the other two dimensions, the base dimensions, are placed
		 * such that the 1st dimension <= 2nd dimension
		 */
		public Block rotation2() {
			if(height >= length) {
				return new Block(length, height, width);
			}
			return new Block(height, length, width);
		}
		
		/*
		 * returns a new block rotated such that the 3rd value inputed
		 * represents the height and
		 * 
		 * the other two dimensions, the base dimensions, are placed
		 * such that the 1st dimension <= 2nd dimension
		 */
		public Block rotation3() {
			if(width >= length) {
				return new Block(length, width, height);
			}
			return new Block(width, length, height);
		}
		
	}
}
