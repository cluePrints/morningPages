package com.mpages.treededup;
import java.io.InputStream;


public class FilePart {
	@Override
	public String toString() {
		return "FilePart [file=" + file + ", startPos=" + getStartPos()
				+ ", length=" + length + "]";
	}
	private final InputStream is;
	private final String file;
	private final long startPos;
	private final long length;
	
	public FilePart(InputStream is, String file, long startPos, long length) {
		super();
		this.is = is;
		this.file = file;
		this.startPos = startPos;
		this.length = length;
	}
	public String getFile() {
		return file;
	}
	public long getStartPos() {
		return startPos;
	}
	public long getEndPos() {
		return startPos+length-1;
	}
	public long getLength() {
		return length;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + (int) (length ^ (length >>> 32));
		result = prime * result + (int) (startPos ^ (startPos >>> 32));
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
		FilePart other = (FilePart) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (length != other.length)
			return false;
		if (startPos != other.startPos)
			return false;
		return true;
	}
	
}
