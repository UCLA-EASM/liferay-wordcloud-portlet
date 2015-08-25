<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import = "com.liferay.portlet.documentlibrary.model.DLFileEntry" %>
<%@ page import = "com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %>

<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<portlet:defineObjects />

<h3>Please select the document(s) to view in a word cloud:</h3>

<% 
final int fileCount = DLFileEntryLocalServiceUtil.getDLFileEntriesCount();
List<DLFileEntry> documents = DLFileEntryLocalServiceUtil.getDLFileEntries(0, fileCount);
List<DLFileEntry> textDocuments = new ArrayList<DLFileEntry>();
for(int i=0;i<documents.size();i++) {
	if(documents.get(i).getExtension().equals("txt")) {
		textDocuments.add(documents.get(i));
	}
}
%>

<portlet:actionURL name="tokenize_file" var="wordCloudGeneratorURL">
</portlet:actionURL>

<aui:form action="<%= wordCloudGeneratorURL %>" method="post" inlineLabel="true">
	<% for ( int i = 0; i < textDocuments.size(); i++ ) {
		%>
	<aui:input type="checkbox" inlineLabel="left"  
				name="<%= \"document-\" + Long.toString(textDocuments.get(i).getFileEntryId()) %>" label="<%=textDocuments.get(i).getTitle() %>">
	</aui:input>
	<% 
	}
	%>
	<aui:input type="hidden" name="doumentListSize" value="<%= documents.size() %>"></aui:input>
    <aui:input type="submit" name="" value="Generate wordcloud" />
</aui:form>
