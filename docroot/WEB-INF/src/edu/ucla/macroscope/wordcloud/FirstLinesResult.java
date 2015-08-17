package edu.ucla.macroscope.wordcloud;

import com.liferay.portlet.documentlibrary.model.DLFileEntry;

public class FirstLinesResult {
	private DLFileEntry content;
	private String line;
	private String filecount;
	
	/*public FirstLinesResult(DLFileEntry content, String line) {
		super();
		this.content = content;
		this.line = line;
	}*/
	
	public FirstLinesResult(DLFileEntry content, String line,String count) {
		super();
		this.content = content;
		this.line = line;
		this.filecount = count;
	}
	
	public DLFileEntry getContent() {
		return content;
	}
	public String getLine() {
		return line;
	}
	public String getFileCount() {
		return filecount;
	}	
}