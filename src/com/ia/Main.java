package com.ia;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.ia.models.DataBaseWord;
import com.ia.models.Word;
import com.ia.services.RhymeService;
import com.ia.services.SeparatesSyllablesService;

import com.ia.DAO.DataBaseWordDAO;
import com.ia.hibernate.HibernateUtil;
import br.furb.api.furbspeech.FurbSpeech;

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
	
	public List<DataBaseWord> execute(List<DataBaseWord> atributos) throws Exception {
        Session session = HibernateUtil.getSession();
        DataBaseWordDAO dao = new DataBaseWordDAO();
        List<DataBaseWord> list = new ArrayList<DataBaseWord>();
        List<DataBaseWord> todos = dao.findAll(DataBaseWord.class);
        
        return list;
    }    
}
