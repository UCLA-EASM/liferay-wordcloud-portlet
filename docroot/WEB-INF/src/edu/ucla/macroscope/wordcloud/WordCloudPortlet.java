package edu.ucla.macroscope.wordcloud;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class WordCloudPortlet
 */
public class WordCloudPortlet extends MVCPortlet {
	
	public void tokenize_file(ActionRequest request, ActionResponse response) 
			throws InvalidParameterException, PortalException, SystemException, SQLException, IOException, PortletException {
		
		//System.out.println("Tokenizing file");
		
		ArrayList<Long> selectedDocumentIds = new ArrayList<Long>();
		String count = "0";
		HashMap<String,Integer> dict = new HashMap<String,Integer>();
		ValueComparator bvc =  new ValueComparator(dict);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        GetStopWords stop= new GetStopWords();
        HashSet h = stop.getStopList();
		
		for (Enumeration<String> parameterNames = request.getParameterNames(); parameterNames.hasMoreElements();) {
			String parameterName = parameterNames.nextElement();
			
			if (!parameterName.startsWith("document-")) {
				continue;
			}
			
			if(ParamUtil.getBoolean(request, parameterName)) {
				// NOTE: Potential bug if document IDs get more complex
				Long documentId = Long.parseLong(parameterName.replaceAll("document-", ""));
				selectedDocumentIds.add(documentId);
			}
		}
		
		if (selectedDocumentIds.isEmpty()) {
			throw new InvalidParameterException("No document IDs selected");
		}
		
		List<WordCloudResult> results = new ArrayList<WordCloudResult>();
		
		for (Long documentId : selectedDocumentIds) {;
			DLFileEntry document = DLFileEntryLocalServiceUtil.getDLFileEntry(documentId);
			//BufferedReader br=null;
			//br = new BufferedReader(new FileReader(document.getTitle()));
			//br = new BufferedReader(bew FileReader(document));
			
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[Math.abs((int)document.getSize())];
			
			InputStream stream = document.getContentStream();
			if(stream.read(buffer)==-1){
				//System.out.println("Entire file parsed");
			}
			
			stream.close();
			
			String lines = new String(buffer);
			//System.out.println("Line: "+lines);
			//Making the dictionary
			Pattern p = Pattern.compile("[\\w']+");
			Matcher m = p.matcher(lines);
			//System.out.println("Matcher starts:");
			dict.put("Test", 1);
			while(m.find()){
				String word = "";
				word=lines.substring(m.start(), m.end());
				//System.out.println("Splitting"+word);
				if(dict.containsKey(word)){
					int count1  = dict.get(word);
					count1++;
					dict.put(word, count1);
					//System.out.println("Inside"+word);
				}
				else if(!(h.contains(word)) && !word.matches("[0-9]+") && word.length()>2){
					dict.put(word, 0);
					//System.out.println(word);
				}
				 //System.out.println("out");
			}
			if(dict.size()<3){
				GetStopWords stop2= new GetStopWords(0);
		        HashSet h1 = stop2.getStopList();
				//dict.remove("Test");
				char chinese[]=lines.toCharArray();
				for(char c:chinese){
					String cword = ""+c;
					if(!cword.isEmpty()&&!cword.equals("")&&!cword.equals(" ")&&!h1.contains(cword)){
						if(!dict.containsKey(cword)){
							dict.put(cword, 1);
						}
						else{
							int count1  = dict.get(cword);
							count1++;
							dict.put(cword, count1);
						}
					}
						
				}
				//String s = new String();
			}
			sorted_map.putAll(dict);
			//System.out.println("results: "+sorted_map);
			//System.out.println(lines);
		
			count = Long.toString(document.getSize());

			results.add(new WordCloudResult(document, sorted_map, count));
		}
		
		//SessionMessages.add(request, "results", results);
		//System.out.println(SessionMessages.get(request, "results"));
		//System.out.println(results.size());
		for(int i=0; i< results.size();i++) {
			response.setRenderParameter("WordArray"+i, results.get(i).getWordArray());
			response.setRenderParameter("title"+i, results.get(i).getContent().getTitle());
			response.setRenderParameter("frequency_counts"+i, results.get(i).getFreqCounts());
			response.setRenderParameter("filesize"+i, results.get(i).getFileSize());
		}
		
		response.setRenderParameter("resultSize", Integer.toString(results.size()));
		response.setRenderParameter("jspPage", "/html/wordcloud/viewcloud.jsp");
	}

	
}
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
