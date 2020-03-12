package blocks;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * A program which builds the tallest tower possible out of a collection of
 * rectangular blocks
 * 
 * A block can be stacked on top of another block if and only if the two
 * dimensions on the base of the top block are smaller than the two dimensions
 * on the base of the lower block
 * 
 * @authors Kristine Cho and Mercy Bickell
 * @version 3/12/20
 *
 */
public class Blocks {

	ArrayList<Block> setOfBlocks = new ArrayList<>();
	BlockPair[] dp;

	/**
	 * reads in input and runs the algorithm to find the tallest possible tower
	 * using the given blocks
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 *             if file not found
	 */
	public static void main(String[] args) throws IOException {
		File infile = new File(args[0]);
		File outfile = new File(args[1]);
		Blocks runner = new Blocks();

		try {
			Scanner readIn = new Scanner(infile); // to read in from infile
			int arrayLength = Integer.valueOf(readIn.nextLine());
			Block[] blockTypes = new Block[arrayLength];

			// read in the blocks from the file
			for (int i = 0; i < arrayLength; i++) {
				String currentBlock = readIn.nextLine();
				String[] dimensions = currentBlock.split(" ");

				int l = Integer.parseInt(dimensions[0]);
				int w = Integer.parseInt(dimensions[1]);
				int h = Integer.parseInt(dimensions[2]);
				blockTypes[i] = runner.new Block(w, l, h);
			}

			runner.setOfBlocks = runner.getVariations(blockTypes);
			
			// sort the blocks in order from largest area to smallest
			Collections.sort(runner.setOfBlocks);

			// for debugging
			for (int i = 0; i < runner.setOfBlocks.size(); i++) {
				System.out.println(runner.setOfBlocks.get(i));
			}

			Block lastBlock = runner.setOfBlocks.get(runner.setOfBlocks.size() - 1);
			
			int[] results = runner.maxHeightDPM(lastBlock);
			int maxHeight = results[0];
			int indexOfTopBlock = results[1];
			
			// FIXME
			System.out.println(maxHeight);
			System.out.println(indexOfTopBlock);
			
			int i = results[1];
			ArrayList<Block> blocks = new ArrayList<Block>();
			while (i > 0) {
				blocks.add(runner.setOfBlocks.get(i));
			}
			
			System.out.println("The tallest tower has " + blocks.size() + " blocks and a height of " + results[0]);

			/*
			 * ArrayList<Block> tallestTowerBlocks = new ArrayList<>(); //store tallest
			 * blocks somehow in an array int numBlocks = tallestTowerBlocks.size();
			 * 
			 * readIn.close();
			 * 
			 * BufferedWriter writer = new BufferedWriter(new FileWriter(outfile)); //to
			 * write to outfile
			 * 
			 * writer.write(numBlocks); //first line of outfile should be number of blocks
			 * in tallest tower writer.newLine();
			 * 
			 * // loop which writes out all the blocks in the tallest possible tower to the
			 * outfile for(int i = 0; i < numBlocks; i++) {
			 * writer.write(tallestTowerBlocks.get(i).toString()); writer.newLine(); }
			 * 
			 * writer.close();
			 */
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method which, given a list of blocks, returns a list with all possible block
	 * configurations by looping through each given block, finding its 3 rotations,
	 * and adding it to our list
	 * 
	 * @param inputBlocks
	 * @return an arrayList of all possible blocks to use in the tower
	 */
	public ArrayList<Block> getVariations(Block[] inputBlocks) {
		ArrayList<Block> allBlocks = new ArrayList<Block>();
		for (int i = 0; i < inputBlocks.length; i++) {
			allBlocks.add(inputBlocks[i].rotation1());
			allBlocks.add(inputBlocks[i].rotation2());
			allBlocks.add(inputBlocks[i].rotation3());
		}
		return allBlocks;
	}

	/**
	 * Recursive solution: Given a block b which is on top of a stack of boxes,
	 * finds max possible height of tower
	 */
	public int maxHeightRecur(Block b) {
		int bIndex = setOfBlocks.indexOf(b);

		// if largest block is on "top", then maxHeight is simply its height
		if (bIndex == 0) {
			return b.height();
		}

		int maxHeight = 0;
		int possibleMax = 0;

		// loops through all blocks with larger base area than Block b
		// if they can be stacked underneath b
		for (int j = bIndex - 1; j > -1; j--) {
			Block blockBelow = setOfBlocks.get(j);
			if (b.stackableOn(blockBelow)) {
				possibleMax = b.height() + maxHeightRecur(blockBelow);
			}

			if (possibleMax > maxHeight) {
				maxHeight = possibleMax;
			}
		}

		return maxHeight;
	}

	/**
	 * finds max height of all maxHeights for each block, assuming they are stored
	 * in an arrayList
	 * 
	 * @return actual max height of all the blocks
	 */
	public int finalMaxHeight(ArrayList<Integer> maxHeights) {
		int max = 0;
		for (int i = 0; i < maxHeights.size(); i++) {
			if (maxHeights.get(i) > max) {
				max = maxHeights.get(i);
			}
		}
		return max;
	}

	/**
	 * Dynamic programming approach to finding max height of a tower using only
	 * certain blocks
	 * 
	 * @param b
	 *            the block which is on top of a tower of blocks
	 * @return array, index 0 is the max height of the tower, index 1 is the pointer
	 *         to the topmost block in the tower with the maximum height
	 */
	public int[] maxHeightDPM(Block b) {
		
		int numBlocks = setOfBlocks.indexOf(b) + 1;

		// Initializing DP table, which will contain the maximum height for each tower
		// that contains the block and the pointer to the previous block in the tower,
		// if it exists
		dp = new BlockPair[numBlocks];

		// Going from the bottom of the tower to the top
		for (int i = 0; i < dp.length; i++) {

			// Current block
			Block curBlock = setOfBlocks.get(i);

			// FIXME
			System.out.println("Current block: " + curBlock);

			// Initialize current block's height to be its max Height in the DP table
			int max = curBlock.height();
			dp[i] = new BlockPair(max, i);

			// Going through the previous blocks
			for (int j = i - 1; j >= 0; j--) {

				// Check if current block is stackable on one of the blocks prior to it
				// if it is, then we can simple add the current block's height
				// to the entry in the table for the prior block
				if (curBlock.stackableOn(setOfBlocks.get(j))) {

					// Checking if this is the new max for the current block
					if (curBlock.height + dp[j].getMaxHeight() > max) {
						max = curBlock.height + dp[j].getMaxHeight();

						// If it is, setting the pointer to the previous block on the tower
						dp[i].setIndex(j);
					}
				}
			}

			// Assigning the maximum height for the tower containing this block
			dp[i].setMaxHeight(max);

			// FIXME
			System.out.println("Max for this block on top: " + max);
		}

		// Find maximum of all entries in DP table to get final solution
		int finalMax = 0;
		int indexOfTopBlock = -1;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i].getMaxHeight() > finalMax) {
				finalMax = dp[i].getMaxHeight();
				indexOfTopBlock = i;
			}
		}

		return new int[] {finalMax, indexOfTopBlock};
	}
	
	public void printResults(int[] results) {
		
		int i = results[1];
		ArrayList<Block> blocks = new ArrayList<Block>();
		while (i > 0) {
			blocks.add(setOfBlocks.get(i));
		}
		
		System.out.println("The tallest tower has " + blocks.size() + " blocks and a height of " + results[0]);
	}

	/**
	 * Class block to represent rectangular building blocks with base dimensions of
	 * length x width and a height dimension
	 * 
	 */
	class Block implements Comparable<Block> {
		int length;
		int width;
		int height;
		int area; // area of base

		/**
		 * length should be <= width
		 * 
		 * @param length
		 * @param width
		 * @param height
		 */
		public Block(int length, int width, int height) {
			this.length = length;
			this.width = width;
			this.height = height;
			this.area = length * width;
		}

		/**
		 * @return the length of a block
		 */
		public int length() {
			return length;
		}

		/**
		 * @return the width of a block
		 */
		public int width() {
			return width;
		}

		/**
		 * @return the height of a block
		 */
		public int height() {
			return height;
		}

		/**
		 * @return the base area of a block
		 */
		public int area() {
			return area;
		}

		/**
		 * allows for built-in sorting of the blocks in order from largest base area to
		 * smallest
		 */
		public int compareTo(Block other) {
			return (other.area - this.area()); // returns neg number if this block's area is bigger, pos if smaller, and
												// 0 if equal
		}

		/**
		 * given a block, returns true if this block is stackable on top of the other
		 * block
		 */
		public boolean stackableOn(Block other) {
			return (width < other.width() && length < other.length());
		}

		/**
		 * returns a new block rotated such that the 1st value inputed represents the
		 * height and
		 * 
		 * the other two dimensions, the base dimensions, are placed such that the 1st
		 * dimension <= 2nd dimension
		 */
		public Block rotation1() {
			if (height >= width) {
				return new Block(width, height, length);
			}
			return new Block(height, width, length);
		}

		/**
		 * returns a new block rotated such that the 2nd value inputed represents the
		 * height and
		 * 
		 * the other two dimensions, the base dimensions, are placed such that the 1st
		 * dimension <= 2nd dimension
		 */
		public Block rotation2() {
			if (height >= length) {
				return new Block(length, height, width);
			}
			return new Block(height, length, width);
		}

		/**
		 * returns a new block rotated such that the 3rd value inputed represents the
		 * height and
		 * 
		 * the other two dimensions, the base dimensions, are placed such that the 1st
		 * dimension <= 2nd dimension
		 */
		public Block rotation3() {
			if (width >= length) {
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

	class BlockPair {

		private int maxHeight;
		private int index;

		public BlockPair(int maxHeight, int index) {
			this.maxHeight = maxHeight;
			this.index = index;
		}

		public int getMaxHeight() {
			return maxHeight;
		}

		public int getIndex() {
			return index;
		}

		public void setMaxHeight(int newHeight) {
			maxHeight = newHeight;
		}

		public void setIndex(int newIndex) {
			index = newIndex;
		}
	}
}
