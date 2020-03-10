package blocks;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
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
	 * reads in input and 
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
				int l = Integer.parseInt(dimensions[0]);
				int w = Integer.parseInt(dimensions[1]);
				int h = Integer.parseInt(dimensions[2]);
				blockTypes[i] = runner.new Block(w, l, h);
			}
			
			ArrayList<Block> enumBlock = runner.getVariations(blockTypes);
			
			//now do stuff with all the possible orientations of the blocks
			//sort the blocks in order from largest area to smallest
			Collections.sort(enumBlock);
			
			int maxHeight = 0; //run alg here
			
			ArrayList<Block> tallestTowerBlocks = new ArrayList<>(); //store tallest blocks somehow in an array
			int numBlocks = tallestTowerBlocks.size();
			
			readIn.close();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile)); //to write to outfile
			
			writer.write(numBlocks); //first line of outfile should be number of blocks in tallest tower
			writer.newLine();
			
			// loop which writes out all the blocks in the tallest possible tower to the outfile
			for(int i = 0; i < numBlocks; i++) {
				writer.write(tallestTowerBlocks.get(i).toString());
				writer.newLine();
			}
			
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
	
	public int maxHeightRecurK(ArrayList<Block> blocksInTower) {
		
		// Base case where list is empty
		if (blocksInTower.isEmpty()) {
			return 0;
		}
		
		// Base case where there is only 1 block
		else if (blocksInTower.size() == 1) {
			return (blocksInTower.get(0).height);
		}
		
		else {
			
			// The current block we are working with
			Block curBlock = blocksInTower.get(blocksInTower.size() - 1);
			
			// If the current block is greater in area than the last block on the tower, add it to the tower
			if (curBlock.area > blocksInTower.get(blocksInTower.size() - 2).area) {
				
				// Adds the height of this block to the height of the tower
				return curBlock.height + maxHeightRecurK((ArrayList<Block>) blocksInTower.subList(0, blocksInTower.size() - 2));
			}
			
			// If the current block is not greater in area than the last block on the tower, the height of the tower remains the same
			else {
				
				// Returns the height of the tower without this block
				return maxHeightRecurK((ArrayList<Block>) blocksInTower.subList(0, blocksInTower.size() - 2));
			}
		}
	}
	
	/**
	 * Recursive solution in progress
	 * 
	 * @param allBlocks - a list of block sorted from largest base area to smallest
	 * @param j
	 * @return
	 */
	public int maxHeightRecur(ArrayList<Block> allBlocks, int j) {
		
		//base case where list is empty
		if(j >= allBlocks.size()) {
			return 0;
		}
		//base case where list has 1 element left
		else if(j == allBlocks.size() -1) {
			if(allBlocks.get(j).stackableOn(allBlocks.get(j-1))){
				return allBlocks.get(j).height();
			}
			else {
				return 0;
			}
		}
		int currentMax = 0;
		for(int i = j; i < allBlocks.size(); i++) {
			int possibleMax = allBlocks.get(i).height() + maxHeightRecur(allBlocks, j+1);
			if(possibleMax > currentMax) {
				currentMax = possibleMax;
			}
		}
		return currentMax;
	}
	
	/**
	 * Class block to represent rectangular building blocks
	 * with base dimensions of length x width and a height dimension
	 * 
	 */
	class Block implements Comparable<Block>{
		int length;
		int width;
		int height;
		int area;
		
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
		
		public int area() {
			return area;
		}
		
		/*
		 * allows for built-in sorting of the blocks
		 * in order from largest area to smallest
		 */
		public int compareTo(Block other) {
			return (other.area - this.area()); //returns neg number if this block's area is bigger, pos if smaller, and 0 if equal
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
		
		/**
		 * return a string representation of Block, which gives its three dimensions
		 */
		public String toString() {
			return length + " " + width + " " + height;
		}
	}
}
