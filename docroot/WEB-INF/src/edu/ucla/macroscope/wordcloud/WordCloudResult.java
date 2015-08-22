package edu.ucla.macroscope.wordcloud;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.liferay.portlet.documentlibrary.model.DLFileEntry;

import com.liferay.portlet.documentlibrary.model.DLFileEntry;

public class WordCloudResult {

	private DLFileEntry content;
	//private TreeMap<String,Integer> words;
	private String words[];
	private String filesize;
	private String frequencies[];
	
	/* content: contents of the Document Library file entry
	 * tree_words: map of words to frequencies
	 * count: number of bytes in the file
	 */
	
	public WordCloudResult(DLFileEntry content, TreeMap<String,Integer> tree_words,String count) {
		super();
		this.content = content;
		this.words = new String[tree_words.size()];
		this.frequencies = new String[tree_words.size()];
		int i=0;
		for(Entry<String, Integer> entry : tree_words.entrySet()) {
			  String key = entry.getKey();
			  Integer value = entry.getValue();
			  this.words[i]=new String(key);
			  this.frequencies[i++]=new String(Integer.toString(value));
			  //System.out.println(key + " => " + value);
			}
		//this.words = words;
		this.filesize = count;
	}
	
	public DLFileEntry getContent() {
		return content;
	}
	public String[] getWordArray() {
		return words;
	}
	public String getFileSize() {
		return filesize;
	}
	public String[] getFreqCounts() {
		return frequencies;
	}	
}