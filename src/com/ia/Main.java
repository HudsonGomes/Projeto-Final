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
		
		//File speech = new FurbSpeech().text("isso é muito tóxico e tem paralelepípedo").to().speech();
		
		Word word = new Word("pneumático", "pneu.ma.ti.co");
		//Word word = new Word("tédio", "te.di.o");
		//Word word2 = new Word("traumático", "trau.ma.ti.co");
		//Word word2 = new Word("colégio", "co.le.gi.o");
		Word word2 = new Word("enfático", "en.fa.ti.co");
		SeparatesSyllablesService service = new SeparatesSyllablesService();
		ArrayList<String> syllables = service.separatesSyllables(word);
		RhymeService rhymeService = new RhymeService();
		boolean isR = rhymeService.isRhyming(word, word2);
		
		System.out.println((new StringBuilder("Syllables: ")).append(syllables).toString());
		if(isR){
			System.out.println(word.getText()+" rima com "+word2.getText());
			
		}else{
			System.out.println(word.getText()+" não rima com "+word2.getText());
		}
	}
}
