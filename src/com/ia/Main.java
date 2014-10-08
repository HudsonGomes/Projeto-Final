package com.ia;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.annotation.Resource;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.hibernate.Session;
import org.postgresql.ds.PGSimpleDataSource;

import resource.Resources;

import com.ia.models.DataBaseWord;
import com.ia.models.Evaluation;
import com.ia.models.Phrase;
import com.ia.models.User;
import com.ia.models.Word;
import com.ia.services.RhymeService;
import com.ia.services.SeparatesSyllablesService;
import com.ia.usertype.CategoryUserType;
import com.ia.DAO.AbstractDao;
import com.ia.DAO.DataBaseWordDAO;
import com.ia.datamodels.PostgreSQLJDBCDataModel;
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
		createRecommendations();
	}
	
	//Retorna a lista inteira das palavras do banco de dado. OBS: CUIDADO, esse método é custoso vide que temos + de 200k de palavras
	public List<DataBaseWord> execute(List<DataBaseWord> atributos) throws Exception {
        Session session = HibernateUtil.getSession();
        DataBaseWordDAO dao = new DataBaseWordDAO();
        List<DataBaseWord> list = new ArrayList<DataBaseWord>();
        List<DataBaseWord> todos = dao.findAll(DataBaseWord.class);
        
        return list;
    }
	
	// Método que ensina a utilizar o Apache Mahout para UserBased Collaborative Filtering
	public static void createRecommendations() throws TasteException{

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("127.0.0.1");
        dataSource.setUser("postgres");
        dataSource.setPassword("123");
        dataSource.setDatabaseName("poem");
        dataSource.setPortNumber(5432);

        DataModel model = new PostgreSQLJDBCDataModel(dataSource);
        //DataModel model = new FileDataModel(new File("C:/Users/Visagio/Dropbox/Projeto Final/postgresql/dataset.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(2, 3);
        for (RecommendedItem recommendation : recommendations) {
          System.out.println(recommendation);
        }
	}
	
	//Cria uma phrase, um user, uma evaluation, essa que estará associada a frase criada e então associada ao usuário
	public static void fillPhrase() throws Exception{
		
		Phrase phrase = new Phrase();
		phrase.setPhrase("Estamos chegando lá");
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
        
        User newUser = new User();
        newUser.setName("roberto");
        newUser.setPassword("admin");
		Evaluation ev = new Evaluation();
		ev.setPhrase(phrase);
		ev.setGrade(new BigDecimal(5.0));
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		evaluations.add(ev);
		newUser.setEvaluations(evaluations);
		AbstractDao<User> userDAO = new AbstractDao<User>(){};
		userDAO.saveOrUpdate(newUser);
	
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
