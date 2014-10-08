package com.ia;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import resource.Resources;

import com.ia.models.DataBaseWord;
import com.ia.models.Phrase;
import com.ia.models.Word;
import com.ia.services.RhymeService;
import com.ia.services.SeparatesSyllablesService;
import com.ia.usertype.CategoryUserType;
import com.ia.DAO.AbstractDao;
import com.ia.DAO.DataBaseWordDAO;
import com.ia.hibernate.HibernateUtil;

//testando commit
public class Main {

	public static void main(String args[]) throws Exception{
		
		
		Word word = new Word("pneumático", "pneu.ma.ti.co");
		//Word word = new Word("tédio", "te.di.o");
		//Word word2 = new Word("traumático", "trau.ma.ti.co");
		//Word word2 = new Word("colégio", "co.le.gi.o");
		Word word2 = new Word("enfático", "en.fa.ti.co");
		SeparatesSyllablesService service = new SeparatesSyllablesService();
		Word wordT = new Word("paralelepípedo");
		ArrayList<String> syllables = service.separatesSyllables(wordT);
		RhymeService rhymeService = new RhymeService();
		boolean isR = rhymeService.isRhyming(word, word2);
		
		System.out.println((new StringBuilder("Syllables: ")).append(syllables).toString());
		if(isR){
			System.out.println(word.getText()+" rima com "+word2.getText());
			
		}else{
			System.out.println(word.getText()+" não rima com "+word2.getText());
		}
		fillPhrase();
	}
	
	public List<DataBaseWord> execute(List<DataBaseWord> atributos) throws Exception {
        Session session = HibernateUtil.getSession();
        DataBaseWordDAO dao = new DataBaseWordDAO();
        List<DataBaseWord> list = new ArrayList<DataBaseWord>();
        List<DataBaseWord> todos = dao.findAll(DataBaseWord.class);
        
        return list;
    }
	
	//popula Banco de dados a partir do txt portuguese.txt
		public static void fillPhrase() throws Exception{
			
			Phrase phrase = new Phrase();
			phrase.setPhrase("Estamos nos aprimorando");
			phrase.setCategory(CategoryUserType.AMOR);
			String[] parts =phrase.getPhrase().split(" ");
			Session session = HibernateUtil.getSession();
	        DataBaseWordDAO dao = new DataBaseWordDAO();
	        DataBaseWord dbw = dao.getSameString(parts[parts.length-1]);
	        if(dbw != null){
	        	phrase.setLastWord(dbw);
	            AbstractDao<Phrase> phraseDAO = new AbstractDao<Phrase>() {};
	            phraseDAO.saveOrUpdate(phrase);
	        }
			    
		}
	
	//popula Banco de dados a partir do txt portuguese.txt
	public static void createBdFromTextFile() throws Exception{
		
		Session session = HibernateUtil.getSession();
		 BufferedReader br = new BufferedReader(new InputStreamReader(Resources.class.getResourceAsStream("portuguese.txt"), "UTF-8"));
		    try {
		        String line = br.readLine();

		        while (line != null) {
		            DataBaseWord dataBaseWord = new DataBaseWord();
		            dataBaseWord.setNome(line);
		            DataBaseWordDAO dbwDAO = new DataBaseWordDAO();
		            dbwDAO.saveOrUpdate(dataBaseWord);
		            line = br.readLine();
		        }
		    } finally {
		        br.close();
		    }
	}
}
