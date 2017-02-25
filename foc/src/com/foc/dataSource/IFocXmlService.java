package com.foc.dataSource;

public interface IFocXmlService {
	String  iFocSvrRequest_getModule();
	String  iFocSvrRequest_getName();
	String  iFocSvrRequest_xmlFormat();
	boolean iFocSvrRequest_xmlParse(String xml);
}
