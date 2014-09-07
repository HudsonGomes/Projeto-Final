package models;

public class Word extends Letters{

	String text;
	String phoneme;
	
	public Word(String text, String phoneme){
		this.text = text;
		this.phoneme = phoneme;
	}
	
	//TO DO!! THE CORRECT PLACE IS NOT HERE!!
	public static boolean isBelong(char l, char conjunto[]) {
		for (int i = 0; i < conjunto.length; i++) {
			if (l == conjunto[i]) {
				return true;
			}
		}

		return false;
	}

	//TO DO!! THE CORRECT PLACE IS NOT HERE!!
	public static boolean isBelong2(char conjunto[], String palavra, int index) {
		if (index >= palavra.length() || index < 0) {
			return false;
		} else {
			return isBelong(palavra.charAt(index), conjunto);
		}
	}
	
	public String getText(){
		return text;
	}
	
	public String getPhoneme(){
		return phoneme;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setPhoneme(String phoneme) {
		this.phoneme = phoneme;
	}

}
