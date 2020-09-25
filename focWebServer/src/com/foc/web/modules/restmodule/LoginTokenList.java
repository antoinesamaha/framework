package com.foc.web.modules.restmodule;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.foc.admin.FocUser;
import com.foc.db.SQLFilter;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocDesc;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocListOrder;
import com.foc.list.FocListWithFilter;
import com.foc.util.ASCII;

public class LoginTokenList extends FocListWithFilter{

	public LoginTokenList() {
		
		super(LoginTokenFilter.getFocDesc(), new FocLinkSimple(LoginToken.getFocDesc()));
		
		FocDesc desc = getFocDesc();
		int id = desc.getFieldIDByName(LoginToken.FIELD_DateTime);
		FocListOrder order = new FocListOrder(id);
		order.setReverted(true);
		setListOrder(order);
	}

	public LoginTokenList(String token) {
		this(token, false);
	}
	
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public LoginTokenList(String token, boolean recentUnconsumed) {
		super(LoginTokenFilter.getFocDesc(), new FocLinkSimple(LoginToken.getFocDesc()));
		
		if (recentUnconsumed) {
			long since = System.currentTimeMillis();
			since = since - 30000;
			Date dateSince30Sec = new Date(since);
			String datetime = df.format(dateSince30Sec);
			
			SQLFilter filter = getFilter();
			filter.putAdditionalWhere("TOKEN", "\""+LoginToken.FIELD_Token+"\"='"+token+"' AND \""+LoginToken.FIELD_Consumed+"\"=0 AND \""+LoginToken.FIELD_DateTime+"\">'"+datetime+"'");
		} else {
			SQLFilter filter = getFilter();
			filter.putAdditionalWhere("TOKEN", "\""+LoginToken.FIELD_Token+"\"='"+token+"'");
		}
	}

	public static synchronized LoginToken generateToken(FocUser user) {
		LoginToken token = null;
		if(user != null) {
			FocConstructor constr = new FocConstructor(LoginToken.getFocDesc());
			token = (LoginToken) constr.newItem();
			token.setCreated(true);
			token.setFocUser(user);		
			token.setDateTime(new Date(System.currentTimeMillis()));
			String tokenStirng = ASCII.generateRandomString(20, true);
			token.setToken(tokenStirng);
			token.validate(true);
		}
		return token;
	}

}
