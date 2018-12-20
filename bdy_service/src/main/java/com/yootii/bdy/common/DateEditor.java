package com.yootii.bdy.common;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (text!=null && text.length()==16) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (text!=null && text.length()==10) {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}
		Date date = null;
		try {
			
			if (text!=null && text!="") {
				date = format.parse(text);
			}
		} catch (ParseException e1) {
				//e1.printStackTrace();
		}
		setValue(date);
	}
}
