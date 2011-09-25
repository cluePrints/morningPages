package com.mpages.parsing.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Tries expand left and right boundaries char by char making sure all the chunks still could be derived.
 */
// TODO: good opportunity to demo skills earned after reading the Clean Code :) 
public class GreedyBoundaryGeneralizer {
	public Boundary predict(Collection<Chunk> chunks) {
		if (chunks == null || chunks.size() < 2)
			return null;
		
		List<Chunk> sortedChunks = new LinkedList<Chunk>(chunks);
		Collections.sort(sortedChunks);

		int leftBoundaryWidth = 0;
		int rightBoundaryWidth = 0;
		

		boolean leftMatches = true;
		boolean rightMatches = true;
		
		Chunk chunk0 = sortedChunks.get(0);
		String whole = chunk0.getWhole();

		do {
			Character leftBoundaryChar = chunk0.getSymbolOutside(-leftBoundaryWidth-1);
			Character rightBoundaryChar = chunk0.getSymbolOutside(rightBoundaryWidth+1);

			if (leftBoundaryChar != null) {
				for (int i = 1; i < sortedChunks.size(); i++) {
					Chunk iChunk = sortedChunks.get(i);
					if (!leftBoundaryChar.equals(iChunk.getSymbolOutside(-leftBoundaryWidth-1)))
						leftMatches = false;
				}
				if (leftMatches) {
					leftBoundaryWidth++;
				}
			} else {
				leftMatches = false;
			}

			if (rightBoundaryChar != null) {
				for (int i = 1; i < sortedChunks.size(); i++) {
					Chunk iChunk = sortedChunks.get(i);
					if (!rightBoundaryChar.equals(iChunk.getSymbolOutside(rightBoundaryWidth+1)))
						rightMatches = false;
				}
				if (rightMatches) {
					rightBoundaryWidth++;
				}
			} else {
				rightMatches = false;
			}

			if (!leftMatches && !rightMatches && leftBoundaryWidth == 0 && rightBoundaryWidth == 0)
				return null;

		} while (leftMatches || rightMatches);
		
		String start = whole.substring(chunk0.getStartIndex() - leftBoundaryWidth, chunk0.getStartIndex());
		String end = whole.substring(chunk0.getEndIndex(), chunk0.getEndIndex() + rightBoundaryWidth);	
		
		return new Boundary(start, end);
	}
}
