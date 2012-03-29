package com.mpages.treededup;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.services.s3.internal.InputSubstream;


public class HashCalculator {
	private static final int BUFFER_SIZE = 16 * 1024;
	
	public HashedPart calc(FilePart part) throws Exception{		
		InputStream is = new FileInputStream(part.getFile());
		
		String hash = partDigest(part, is);
		HashedPart result = new HashedPart(hash, part.getFile(), part.getStartPos(), part.getLength());
		return result;
	}

	String partDigest(FilePart part, InputStream is)
			throws Exception {
		is = new InputSubstream(is,  part.getStartPos()-1, part.getLength(), true);
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = new byte[BUFFER_SIZE];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			md.update(bytes, 0, numBytes);
		}
		part.getStartPos();
		byte[] digest = md.digest();
		String hash = new String(Hex.encodeHex(digest));
		return hash;
	}
	
	public static class HashedPart{
		private final String hash;
		private final String fileName;
		private final long startPos;
		private final long length;
		public HashedPart(String hash, String fileName, long startPos,
				long length) {
			super();
			this.hash = hash;
			this.fileName = fileName;
			this.startPos = startPos;
			this.length = length;
		}
		public String getHash() {
			return hash;
		}
		public String getFileName() {
			return fileName;
		}
		@Override
		public String toString() {
			return hash + " fileName=" + fileName
					+ ", startPos=" + startPos + ", length=" + length + "]";
		}
		
		
	}
}
