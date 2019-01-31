package com.foc.htmldoc;

import java.io.PrintStream;

import com.foc.Globals;
import com.foc.util.Utils;

public class HTMLDocumentation {
	
	private PrintStream unitDoc = null;
	
	public HTMLDocumentation(String dir, String fileName) {
		open(dir, fileName);
	}
	
	public HTMLDocumentation() {
	}
	
	public void dispose() {
		close();
	}
	
	public void close() {
		if(unitDoc != null) {
			closeBody();
			unitDoc.flush();
			unitDoc.close();
			unitDoc = null;
		}
	}
	
	public void open(String dir, String fileName) {
		try{
//			unitDoc = new PrintStream(ConfigInfo.getLogDir()+"/fileName.html", "UTF-8");
			unitDoc = new PrintStream(dir+"/"+fileName+".html", "UTF-8");
		}catch (Exception e){
			Globals.logException(e);
		}
		openBody();
	}
	
	public void openBody() {
		if(unitDoc != null) {
			unitDoc.println("<!DOCTYPE html>");
			unitDoc.println("  <html>");
			unitDoc.println("    <head>");
			unitDoc.println("      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
			unitDoc.println("      <style>");
			unitDoc.println("        .collapsible {");
			unitDoc.println("          background-color: #777;");
			unitDoc.println("          color: white;");
			unitDoc.println("          cursor: pointer;");
			unitDoc.println("          padding: 18px;");
			unitDoc.println("          width: 100%;");
			unitDoc.println("          border: none;");
			unitDoc.println("          text-align: left;");
			unitDoc.println("          outline: none;");
			unitDoc.println("          font-size: 15px;");
			unitDoc.println("        }");
			unitDoc.println();
			unitDoc.println("        .active, .collapsible:hover {");
			unitDoc.println("          background-color: #555;");
			unitDoc.println("        }");
			unitDoc.println();
			unitDoc.println("        .collapsible:after {");
			unitDoc.println("          content: \"\\002B\";");
			unitDoc.println("          color: white;");
			unitDoc.println("          font-weight: bold;");
			unitDoc.println("          float: right;");
			unitDoc.println("          margin-left: 5px;");
			unitDoc.println("        }");
			unitDoc.println();
			unitDoc.println("        .active:after {");
			unitDoc.println("          content: \"\\2212\";");
			unitDoc.println("        }");
			unitDoc.println();
			unitDoc.println("        .content {");
			unitDoc.println("           padding: 0 18px;");
			unitDoc.println("           max-height: 0;");
			unitDoc.println("           overflow: hidden;");
			unitDoc.println("           transition: max-height 0.2s ease-out;");
			unitDoc.println("           background-color: #f1f1f1;");
			unitDoc.println("        }");
			unitDoc.println();
			unitDoc.println("      </style>");
			unitDoc.println("    </head>");
			unitDoc.println("  <body>");
		}
	}
	
	public void closeBody() {
		if(unitDoc != null) {
			unitDoc.println("    <script>");
			unitDoc.println("      var coll = document.getElementsByClassName(\"collapsible\");");
			unitDoc.println("      var i;");
			unitDoc.println("      for (i = 0; i < coll.length; i++) {");
			unitDoc.println("        coll[i].addEventListener(\"click\", function() {");
			unitDoc.println("          this.classList.toggle(\"active\");");
			unitDoc.println("          var content = this.nextElementSibling;");
			unitDoc.println("          if (content.style.maxHeight){");
			unitDoc.println("            content.style.maxHeight = null;");
			unitDoc.println("          } else {");
			unitDoc.println("            content.style.maxHeight = content.scrollHeight + \"px\";");
			unitDoc.println("          }"); 
			unitDoc.println("        });");
			unitDoc.println("      }");
			unitDoc.println("    </script>");
			unitDoc.println("  </body>");
			unitDoc.println("</html>");
		}
	}
	
	public void addSection(String suiteName, String description) {
		if(unitDoc != null && !Utils.isStringEmpty(suiteName)) {
			unitDoc.println("<H1>"+suiteName+"</H1>");
			if(!Utils.isStringEmpty(description)) {
				unitDoc.println("<P>"+description+"</P>");
			}
		}
	}
	
	public void addCollapsible(String title, String description) {
		if(unitDoc != null && !Utils.isStringEmpty(title)) {
			unitDoc.println("<button class=\"collapsible\">"+title+"</button>");
			unitDoc.println("<div class=\"content\">");
			unitDoc.println("   <p>"+description+"</p>");
			unitDoc.println("</div>");
		}
	}

}
