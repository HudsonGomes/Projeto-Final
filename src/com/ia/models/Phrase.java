package com.ia.models;
import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ia.hibernatepersistent.AbstractHibernatePersistent;
import com.ia.usertype.CategoryUserType;


@Entity
@Table(name="phrase")
public class Phrase extends AbstractHibernatePersistent {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="phrase")
    String phrase;
    
    @Column(name="category")
    CategoryUserType category;
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="databaseword_id")
	DataBaseWord lastWord;
	
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public DataBaseWord getLastWord() {
		return lastWord;
	}
	public void setLastWord(DataBaseWord lastWord) {
		this.lastWord = lastWord;
	}
	public void setId(Serializable id) {
        this.id = (Long)id;
    }

    public Serializable getId() {
        return this.id;
    }
	public CategoryUserType getCategory() {
		return category;
	}
	public void setCategory(CategoryUserType category) {
		this.category = category;
	}
	
	
}
