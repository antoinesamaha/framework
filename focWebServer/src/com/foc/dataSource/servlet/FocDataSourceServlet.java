/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.dataSource.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.shared.dataStore.IDataStoreConst;

/**
 * Servlet implementation class FocDataSourceServlet
 */
@SuppressWarnings("serial")
//@WebServlet("/FocDataSourceServlet")
public class FocDataSourceServlet extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FocDataSourceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		treatRequest(request, response);
		/*
		String str = "doGet nbrOfRequests = "+nbrOfRequests;
		System.out.println(str);
		response.getOutputStream().println(str);
		nbrOfRequests++;
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		treatRequest(request, response);
		/*
		String str = "doPost nbrOfRequests = "+nbrOfRequests;
		System.out.println(str);
		response.getOutputStream().println(str);
		nbrOfRequests++;
		*/
	}
	
	private void treatRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jSonString = request.getParameter(IDataStoreConst.REQ_PARAM_JSON);
		
		Globals.logString("RECEIVED JSON : "+jSonString);
		
		FDataRequestList_Server dataReqList = new FDataRequestList_Server();
		dataReqList.parseJson(jSonString);
		
		//Treat the requests and fill the gaps
		for(int i=0; i<dataReqList.size(); i++){
			FDataRequest_Server req = dataReqList.get(i);
			req.execute();
		}
		
		response.getOutputStream().print(dataReqList.toJson());
		/*
		
		String dataKey       = request.getParameter(IDataStoreConst.JSON_KEY_DATA_KEY);
		int    loadMethod    = loadMethodStr != null ? Integer.valueOf(loadMethodStr) : 0;
		
		Object resultObject = (Object) DataStore_Server.getInstance().getData(dataKey);
		if(resultObject instanceof FocList){
			FocList focList = (FocList) resultObject;
			if(focList != null){
				focList.loadIfNotLoadedFromDB();
				if(focList.size() > 0){
					FocDesc focDesc = focList.getFocDesc();
					response.getOutputStream().print("[");
					for(int i=0; i<focList.size(); i++){
						FocObject obj = focList.getFocObject(i);
						if(i > 0) response.getOutputStream().print(",");
						response.getOutputStream().print("{");
						
						//Adding the reference field
						if(focDesc.getWithReference() && obj.hasRealReference()){
							response.getOutputStream().print("\""+IDataStoreConst.JSON_KEY_REF+"\":");
							response.getOutputStream().print(obj.getReference().getInteger());
						}
						
					  //Adding the reference field
						for(int p=0; p<obj.propertiesArray_Size(); p++){
							FProperty prop = obj.propertiesArray_Get(p);
							FField    fld  =  prop.getFocField();
							if(fld.getID() != FField.REF_FIELD_ID){
								response.getOutputStream().print(",\"");
								response.getOutputStream().print(fld.getName());
								response.getOutputStream().print("\":");
								response.getOutputStream().print("\"");
								response.getOutputStream().print(prop.getString());
								response.getOutputStream().print("\"");
							}
						}
						
						response.getOutputStream().print("}");
					}
					response.getOutputStream().print("]");
				}
			}
		}
		*/
		//Globals.logString("Requesting for table : "+table+" load mode:"+PRoxyData);
	}

}
