package com.ia.util;

import java.util.Iterator;
import java.util.List;

public class StringUtil {
	
	public static final boolean contains(String st, String c){
		return st.indexOf(c)!=-1;
	}
	
	public static final boolean isEmpty (String text) {
		if (text == null) {
			return true;
		}
		final int length = text.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Cria um string com os valores passados na lista delimitados pelo separador informado.
	 * @param list
	 * @param separador
	 * @return Uma string com os valores delimitados pelo separador
	 */
	public static String concatWithSeparator(List<?> list, String separador) {
		StringBuffer texto = new StringBuffer();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			texto.append(iter.next());
			if(iter.hasNext()){
				texto.append(separador);
			}
		}
		return texto.toString();
	}
	
}
