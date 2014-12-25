package org.peg4d.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.peg4d.Main;
import org.peg4d.ParsingObject;
import org.peg4d.UMap;

public abstract class ParsingWriter {
	protected String fileName = null;
	protected PrintWriter out = null;
	public void writeTo(String fileName, ParsingObject po) throws IOException {
		this.fileName = fileName;
		if(fileName != null) {
			this.out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
			this.write(po);
			this.out.close();
		}
		else {
			this.out = new PrintWriter(System.out);
			this.write(po);
		}
		this.out = null;
	}
	protected abstract void write(ParsingObject po);
	
	// data
	private final static UMap<Class<?>> extClassMap = new UMap<Class<?>>();
	public final static void registerExtension(String ext, Class<?> c) {
		assert(c.isAssignableFrom(ParsingWriter.class));
		extClassMap.put(ext, c);
	}
	
	public final static ParsingWriter newInstance(String fileName, ParsingWriter defValue) {
		int loc = fileName.lastIndexOf(".");
		if(loc != -1) {
			String ext = fileName.substring(loc+1);
			Class<?> c = extClassMap.get(ext);
			if(c != null) {
				try {
					return (ParsingWriter)c.newInstance();
				} catch (InstantiationException e) {
					Main.reportException(e);
				} catch (IllegalAccessException e) {
					Main.reportException(e);
				}
			}
		}
		return defValue; 
	}
	
}
