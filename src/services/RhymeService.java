package services;

import java.util.ArrayList;

import models.Word;

public class RhymeService {
	
	public RhymeService(){
		
	}

	public ArrayList<Word> collectionRhyme(Word word, ArrayList<Word> words){
		ArrayList<Word> wordsRhyme = new ArrayList<Word>();
		for(int i = 0; i < words.size() - 1; i++){
			word = words.get(i);
			if (isRhyming(word, words.get(i))){
				wordsRhyme.add(word);
			}
		}
		return wordsRhyme;
	}
	
	public boolean isRhyming(Word word1, Word word2){
		
		boolean rhyming = true;
		
		//Pega numero da silaba tonica dentro do vetor
		int minimalSyllablesToRhyme1 = getNumberTonicSyllable(word1);
		int minimalSyllablesToRhyme2 = getNumberTonicSyllable(word2);
		
		//Pega vetor da palavra dividida por silaba
		String[] syllablesPhoneme = word1.getPhoneme().split("\\.");
		String[] syllablesPhoneme2 = word2.getPhoneme().split("\\.");
		
		//Considera o vetor a partir das palavras após a silaba tonica,exemplo oxítona antes, agora xítona
		String[] cuttedFromTonic1;
		String[] cuttedFromTonic2;
		if(syllablesPhoneme.length>1){
			cuttedFromTonic1 = new String[ syllablesPhoneme.length - minimalSyllablesToRhyme1 ];
			for (int i = 0; i < cuttedFromTonic1.length; i++) {
				cuttedFromTonic1[i] = syllablesPhoneme[minimalSyllablesToRhyme1+i];
				
			}
		}else{
			cuttedFromTonic1 = syllablesPhoneme;
		}
		if(syllablesPhoneme2.length>1){
			cuttedFromTonic2 = new String[ syllablesPhoneme2.length - minimalSyllablesToRhyme2 ];
			for (int i = 0; i < cuttedFromTonic2.length; i++) {
				cuttedFromTonic2[i] = syllablesPhoneme2[minimalSyllablesToRhyme2+i];
				
			}
		}else{
			cuttedFromTonic2 = syllablesPhoneme2;
		}
		
		if( cuttedFromTonic1.length == cuttedFromTonic2.length ){
			
			for(int i = 0; i < cuttedFromTonic1.length; i++){
					
					if (!compareVogalsSyllables(cuttedFromTonic1[i], cuttedFromTonic2[i])){
						rhyming = false;
						break;
					}
			}
			
			
		}else{
			rhyming = false;
		}/*else if( cuttedFromTonic1.length > cuttedFromTonic1.length  ){
			
			for(int i = minimalSyllablesToRhyme2; i < syllablesPhoneme2.length; i++){
				
				if (!compareVogalsSyllables(syllablesPhoneme[i], syllablesPhoneme2[i])){
					rhyming = false;
					break;
				}
			}
			
		}else{
			
			for(int i = minimalSyllablesToRhyme1+1; i < syllablesPhoneme.length; i++){
				
				if (!compareVogalsSyllables(syllablesPhoneme[i], syllablesPhoneme2[i])){
					rhyming = false;
					break;
				}
			}
		}*/
		
		return rhyming;
	}
	
	private int getMinimalSyllablesToRhyme(Word word){
		SeparatesSyllablesService separateService = new SeparatesSyllablesService();
		StressedSyllableService stressedService = new StressedSyllableService();
		ArrayList<String> syllables = separateService.separatesSyllables(word);
		int numberOfSyllables = syllables.size();
		int tonicaInt = stressedService.getTonicaInt(word, syllables);
		return numberOfSyllables - tonicaInt;
	}
	
	private int getNumberTonicSyllable(Word word){
		SeparatesSyllablesService separateService = new SeparatesSyllablesService();
		StressedSyllableService stressedService = new StressedSyllableService();
		ArrayList<String> syllables = separateService.separatesSyllables(word);
		int numberOfSyllables = syllables.size();
		int tonicaInt = stressedService.getTonicaInt(word, syllables);
		return tonicaInt;
	}
	
	public static boolean isVogal(String v) {
		String[] allVogals = {
				"a","e","i","o","u",
				"á","é","í","ó","ú",
				"à","è","ì","ò","ù",
				"â","ê","î","ô","û",
				"ä","ë","ï","ö","ü",
				"ã","õ",
				"A","E","I","O","U",
				"Á","É","Í","Ó","Ú",
				"À","È","Ì","Ò","Ù",
				"Â","Ê","Î","Ô","Û",
				"Ä","Ë","Ï","Ö","Ü",
				"Ã","Õ"
		};
		
		return inArray(v, allVogals);
	}
	
	public static boolean inArray(String s, String[] array) {
		for (String string : array) {
			if (string.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean compareVogalsSyllables(String s1, String s2) {
		char[] charArray = s1.toCharArray();
		char[] charArray2 = s2.toCharArray();
		String vogal1 = "";
		String vogal2 = "";
		for (char c : charArray) {
			String s = String.valueOf(c);
			if(isVogal(s)){
				vogal1 = s;
				break;
			}
		}
		for (char c2 : charArray2) {
			String s = String.valueOf(c2);
			if(isVogal(s)){
				vogal2 = s;
				break;
			}
		}
		return vogal1.equals(vogal2);
	}
	
}
