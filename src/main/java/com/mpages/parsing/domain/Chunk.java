package com.mpages.parsing.domain;

public class Chunk implements Comparable<Chunk> {
	private String whole;
	private int startIndex;
	private int endIndex;

	public Chunk(String whole, int startIndex, int endIndex) {
		super();
		this.whole = whole;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public Character getSymbolOutside(int offset) {
		int targetPos;
		if (offset < 0) {
			targetPos = startIndex + offset;
		} else if (offset > 0) {
			targetPos = endIndex + offset -1;
		} else {
			throw new IllegalStateException(
					"Symbol outside with 0 pos is not allowed");
		}

		if (targetPos < 0 || targetPos > whole.length() - 1)
			return null;

		return whole.charAt(targetPos);
	}

	@Override
	public String toString() {
		return getText()+"(" + startIndex + ".." + endIndex + ")";
	}

	public String getText() {
		return whole.substring(startIndex, endIndex);
	}
	public String getWhole() {
		return whole;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public int compareTo(Chunk o) {
		if (whole.equals(o.whole)) {
			if (startIndex == o.startIndex) {
				return endIndex - o.endIndex;
			} else {
				return startIndex - o.endIndex;
			}
		}
		throw new IllegalStateException(
				"There's no interest in comparing chunks of different texts");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndex;
		result = prime * result + startIndex;
		result = prime * result + ((whole == null) ? 0 : whole.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chunk other = (Chunk) obj;
		if (endIndex != other.endIndex)
			return false;
		if (startIndex != other.startIndex)
			return false;
		if (whole == null) {
			if (other.whole != null)
				return false;
		} else if (!whole.equals(other.whole))
			return false;
		return true;
	}
}
