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

This is the <b>First Lines</b> portlet in View mode.-Testn2

<h1>My Library</h1>

<% 
final int fileCount = DLFileEntryLocalServiceUtil.getDLFileEntriesCount();
List<DLFileEntry> documents = DLFileEntryLocalServiceUtil.getDLFileEntries(0, fileCount);
List<DLFileEntry> textDocuments = new ArrayList<DLFileEntry>();
for(int i=0;i<documents.size();i++) {
	if(documents.get(i).getExtension().equals("txt")) {
		textDocuments.add(documents.get(i));
		//System.out.println("Found a text doc");
	}
}
%>


<portlet:actionURL name="loadFirstLines" var="loadFirstLinesURL">
</portlet:actionURL>

<portlet:actionURL name="tokenize_file" var="wordCloudGeneratorURL">
</portlet:actionURL>

<aui:form action="<%= wordCloudGeneratorURL %>" method="post">
	<% for ( int i = 0; i < textDocuments.size(); i++ ) {
		%>
	<aui:input type="checkbox"  
				name="<%= \"document-\" + Long.toString(textDocuments.get(i).getFileEntryId()) %>" label="<%=textDocuments.get(i).getTitle() %>">
	</aui:input>
	<% 
	}
	%>
	<aui:input type="hidden" name="doumentListSize" value="<%= documents.size() %>"></aui:input>
	<aui:input type="submit" name="" value="Get First Lines" />
</aui:form>
<aui:button-row cssClass="liferay-buttons">
	<portlet:renderURL var="cloudURL">
			<portlet:param name="mvcPath" value="/html/wordcloud/wordcloudg.jsp"></portlet:param>
	</portlet:renderURL>
	
	<aui:button name="Cloud Generate" onClick="<%= cloudURL.toString() %>" value="Cloud Generate"></aui:button>
</aui:button-row>
