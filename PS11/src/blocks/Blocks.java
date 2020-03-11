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

			// now do stuff with all the possible orientations of the blocks
			// sort the blocks in order from largest area to smallest
			Collections.sort(runner.setOfBlocks);

			// for debugging
			for (int i = 0; i < runner.setOfBlocks.size(); i++) {
				System.out.println(runner.setOfBlocks.get(i));
			}

			/*
			 * //for testing recursive solution ArrayList<Integer> maxHeights = new
			 * ArrayList<Integer>(); for (int i = 0; i < runner.setOfBlocks.size(); i++) {
			 * maxHeights.add(runner.maxHeightRecur(runner.setOfBlocks.get(i))); }
			 */

			/*
			 * for debugging for(int i = 0; i < maxHeights.size(); i++) {
			 * System.out.println(maxHeights.get(i)); }
			 */

			Block lastBlock = runner.setOfBlocks.get(runner.setOfBlocks.size() - 1);
			int maxHeight = runner.maxHeightDPM(lastBlock);

			// int maxHeight = runner.finalMaxHeight(maxHeights); // this was for recursive
			// solution
			System.out.println(maxHeight);

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

	// FIXME This is not correct. Delete for submission
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

			// If the current block is greater in area than the last block on the tower, add
			// it to the tower
			if (curBlock.area > blocksInTower.get(blocksInTower.size() - 2).area) {

				// Adds the height of this block to the height of the tower
				return curBlock.height
						+ maxHeightRecurK((ArrayList<Block>) blocksInTower.subList(0, blocksInTower.size() - 2));
			}

			// If the current block is not greater in area than the last block on the tower,
			// the height of the tower remains the same
			else {

				// Returns the height of the tower without this block
				return maxHeightRecurK((ArrayList<Block>) blocksInTower.subList(0, blocksInTower.size() - 2));
			}
		}
	}

	/**
	 * Recursive solution Given a block b which is on top of a stack of boxes, finds
	 * max possible height of tower
	 * 
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
	 * @return the max height of the tower
	 */
	public int maxHeightDP(Block b) {

		int numBlocks = setOfBlocks.indexOf(b) + 1;

		// Initializing DP table
		int[] dp = new int[numBlocks];
		

		// Going from the top of the tower to the bottom
		// Mercy: should we instead go bottom to top? since
		// top of tower is our solution - unsure though
		for (int i = 0; i < dp.length; i++) {

			// Current block
			Block curBlock = setOfBlocks.get(i);
			System.out.println("Current block: " + curBlock);

			// initialize current block's height to be its max Height in the DP table
			int max = dp[i] = curBlock.height();
			;

			// Going through the previous blocks
			for (int j = i - 1; j >= 0; j--) {

				// check if current block is stackable on one of the blocks prior to it
				// if it is, then we can simple add the current block's height
				// to the entry in the table for the prior block
				if (curBlock.stackableOn(setOfBlocks.get(j))) {

					// Checking if this is the new max for the current block
					if (curBlock.height + dp[j] > max) {
						max = curBlock.height + dp[j];
					}
				}
			}

			dp[i] = max;
			System.out.println("Max for this block on top: " + max);
		}

		// find maximum of all entries in DP table to get final solution
		int finalMax = 0;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i] > finalMax) {
				finalMax = dp[i];
			}
		}

		return finalMax;
	}

	/**
	 * Mercy messing around with solution
	 * 
	 * Dynamic programming approach to finding max height of a tower using only
	 * certain blocks
	 * 
	 * @param b
	 *            the block which is on top of a tower of blocks
	 * @return the max height of the tower
	 */
	public int maxHeightDPM(Block b) {
		int numBlocks = setOfBlocks.indexOf(b) + 1;

		// Initializing DP table
		BlockPair[] dp = new BlockPair[numBlocks];

		// Going from the top of the tower to the bottom
		// Mercy: should we instead go bottom to top? since
		// top of tower is our solution - unsure though
		for (int i = 0; i < dp.length; i++) {

			// Current block
			Block curBlock = setOfBlocks.get(i);
			System.out.println("Current block: " + curBlock);

			// initialize current block's height to be its max Height in the DP table
			int max = curBlock.height();
			dp[i] = new BlockPair(max, i);

			// Going through the previous blocks
			for (int j = i - 1; j >= 0; j--) {

				// check if current block is stackable on one of the blocks prior to it
				// if it is, then we can simple add the current block's height
				// to the entry in the table for the prior block
				if (curBlock.stackableOn(setOfBlocks.get(j))) {

					// Checking if this is the new max for the current block
					if (curBlock.height + dp[j].getMaxHeight() > max) {
						max = curBlock.height + dp[j].getMaxHeight();
						dp[i].setIndex(j);
					}
				}
			}

			dp[i].setMaxHeight(max);
			System.out.println("Max for this block on top: " + max);
		}

		// find maximum of all entries in DP table to get final solution
		int finalMax = 0;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i].getMaxHeight() > finalMax) {
				finalMax = dp[i].getMaxHeight();
			}
		}

		return finalMax;
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
