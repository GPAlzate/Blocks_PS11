package blocks;

import java.io.*;
import java.util.ArrayList;
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
	 * configurations
	 * 
	 * @param inputBlocks
	 * @return
	 */
	public ArrayList<Block> getVariations(Block[] inputBlocks){
		return new ArrayList<Block>();
	}
	
	class Block{
		int width;
		int length;
		int height;
		
		/**
		 * width should be less than length
		 * @param width
		 * @param length
		 * @param height
		 */
		public Block(int width, int length, int height) {
			this.width = width;
			this.length = length;
			this.height = height;
		}
		
		public int width() {
			return width;
		}
		
		public int length() {
			return length;
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
		//maybe add method(s) for getting different configurations of one block?
		
	}
}
