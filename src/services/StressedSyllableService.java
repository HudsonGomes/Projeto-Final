package services;

import java.util.ArrayList;

import models.Word;

public class StressedSyllableService {
	
	public StressedSyllableService(){
		
	}

	public String getTonicaString(Word word) {
		SeparatesSyllablesService service = new SeparatesSyllablesService();
		ArrayList<String> silabas = service.separatesSyllables(word);
		int i = this.getTonicaInt(word, silabas);
		return i < 0 ? "-" : (String) silabas.get(i);
	}
	
	public int getTonicaInt(Word word, ArrayList<String> syllables) {
		String text = word.getText();
		if (estaEm(Word.MONOSSILABOS_ATONOS, text) || text.length() == 0) {
			return -1;
		}
		for (int i = syllables.size() - 1; i >= 0; i--) {
			String syllable = (String) syllables.get(i);
			for (int j = 0; j < syllable.length(); j++) {
				if ((Word.isBelong(syllable.charAt(j), Word.ACENTOS_GA) || Word.isBelong(
						syllable.charAt(j), Word.CIRCUNFLEXO))
						&& i > syllables.size() - 4) {
					return i;
				}
				if (Word.isBelong(syllable.charAt(j), Word.TIL) && i > syllables.size() - 3) {
					return i;
				}
			}

		}

		char theLast1 = text.charAt(text.length() - 1);
		if (syllables.size() == 1 || Word.isBelong(theLast1, Word.LATERAIS)) {
			return syllables.size() - 1;
		}
		String theLast2 = (String) syllables.get(syllables.size() - 1);
		for (int i = 0; i < theLast2.length(); i++) {
			if (Word.isBelong(theLast2.charAt(i), Word.SEMI_VOGAIS)
					&& (theLast2.charAt(i) != 'u' || !Word.isBelong2(Word.CONJ_8, theLast2,
							i - 1))) {
				return syllables.size() - 1;
			}
		}

		return syllables.size() - 2;
	}

	private boolean estaEm(String list[], String text) {
		for (int i = 0; i < list.length; i++) {
			if (text.equals(list[i])) {
				return true;
			}
		}

		return false;
	}
	
}
