package services;

import java.util.ArrayList;

import models.Word;

public class SeparatesSyllablesService {
	
	public SeparatesSyllablesService(){
		
	}
	
	public ArrayList<String> separatesSyllables(Word word){
		String text = word.getText().toLowerCase();
		
		if (text.equals("ao") || text.equals("aos")) {
			ArrayList<String> separate = new ArrayList<String>();
			separate.add(text);
			return separate;
		} else {
			ArrayList<Integer> positions = buildFirstPositions(text);
			positions = buildSecondPositions(positions, text);
			return fillSyllables(positions, text);
		}
	}
	
	private ArrayList<Integer> buildFirstPositions(String text) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		
		for (int i = 1; i < text.length(); i++) {
			if (Word.isBelong(text.charAt(i), Word.VOGAIS_ACENTOS)
					&& !Word.isBelong(text.charAt(i - 1), Word.VOGAIS_ACENTOS)
					&& i > 1) {
				if (Word.isBelong(text.charAt(i - 1), Word.H)
						&& Word.isBelong(text.charAt(i - 2), Word.CONJ_2)
						|| Word.isBelong(text.charAt(i - 1), Word.LATERAIS)
						&& Word.isBelong(text.charAt(i - 2), Word.CONJ_1)) {
					positions.add(Integer.valueOf(i - 2));
				} else {
					positions.add(Integer.valueOf(i - 1));
				}
			}
		}

		if (positions.size() > 0 && ((Integer) positions.get(0)).intValue() == 1
				&& !Word.isBelong(text.charAt(0), Word.VOGAIS_ACENTOS)) {
			positions.set(0, Integer.valueOf(0));
		}
		if (positions.size() == 0 || ((Integer) positions.get(0)).intValue() != 0) {
			positions.add(0, Integer.valueOf(0));
		}
		return positions;
	}

	private ArrayList<Integer> buildSecondPositions(ArrayList<Integer> positions, String text) {		
		for (int i = 1; i < text.length(); i++) {
			if (Word.isBelong(text.charAt(i), Word.VOGAIS_ACENTOS)
					&& Word.isBelong(text.charAt(i - 1), Word.VOGAIS_ACENTOS)
					&& (i <= 1 || text.charAt(i - 1) != 'u' || !Word.isBelong(
							text.charAt(i - 2), Word.CONJ_8))
					&& !Word.isBelong(text.charAt(i - 1), Word.TIL)
					&& (!Word.isBelong(text.charAt(i), Word.SEMI_VOGAIS)
							|| lastSyllable(i, positions)
							&& Word.isBelong2(Word.LATERAIS, text, i + 1) || Word.isBelong2(
							Word.NASAIS, text, i + 1)
							&& !Word.isBelong2(Word.VOGAIS_ACENTOS, text, i + 2))) {
				for (int j = 0; j < positions.size(); j++) {
					if (((Integer) positions.get(j)).intValue() > i) {
						positions.add(j, Integer.valueOf(i));
						break;
					}
					if (j != positions.size() - 1) {
						continue;
					}
					positions.add(Integer.valueOf(i));
					break;
				}

			}
		}

		return positions;
	}
	
	private boolean lastSyllable(int index, ArrayList<Integer> posicoes) {
		return index >= ((Integer) posicoes.get(posicoes.size() - 1)).intValue();
	}

	private ArrayList<String> fillSyllables(ArrayList<Integer> posicoes, String text) {
		ArrayList<String> ret = new ArrayList<String>();
		
		if (posicoes.size() > 0) {
			int i;
			for (i = 0; i < posicoes.size() - 1; i++) {
				ret.add(text.substring(
						((Integer) posicoes.get(i)).intValue(),
						((Integer) posicoes.get(i + 1)).intValue()));
			}

			ret.add(text.substring(((Integer) posicoes.get(i)).intValue(),
					text.length()));
		}
		return ret;
	}

}
