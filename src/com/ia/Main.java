package com.ia;
import java.io.File;
import java.util.ArrayList;

import br.furb.api.furbspeech.FurbSpeech;

import models.Word;
import services.RhymeService;
import services.SeparatesSyllablesService;

//testando commit
public class Main {

	public static void main(String args[]){
		
		//File speech = new FurbSpeech().text("isso � muito t�xico e tem paralelep�pedo").to().speech();
		
		Word word = new Word("pneum�tico", "pneu.ma.ti.co");
		//Word word = new Word("t�dio", "te.di.o");
		//Word word2 = new Word("traum�tico", "trau.ma.ti.co");
		//Word word2 = new Word("col�gio", "co.le.gi.o");
		Word word2 = new Word("enf�tico", "en.fa.ti.co");
		SeparatesSyllablesService service = new SeparatesSyllablesService();
		ArrayList<String> syllables = service.separatesSyllables(word);
		RhymeService rhymeService = new RhymeService();
		boolean isR = rhymeService.isRhyming(word, word2);
		
		System.out.println((new StringBuilder("Syllables: ")).append(syllables).toString());
		if(isR){
			System.out.println(word.getText()+" rima com "+word2.getText());
			
		}else{
			System.out.println(word.getText()+" n�o rima com "+word2.getText());
		}
	}
}
