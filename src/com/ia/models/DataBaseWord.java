package com.ia.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ia.hibernatepersistent.AbstractHibernatePersistent;
import com.ia.hibernatepersistent.NaturalKey;

@Entity
@Table(name="databaseword")
public class DataBaseWord extends AbstractHibernatePersistent {
    
    private static final long serialVersionUID = 2657786123456778L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="nome")
    private String nome;
    
    public DataBaseWord() throws Exception{
        
    }
    
    public DataBaseWord(DataBaseWord dbWord) throws Exception{
        this.nome = dbWord.nome;
                
    }
    
    public void setId(Serializable id) {
        this.id = (Long)id;
    }

    public Serializable getId() {
        return this.id;
    }
    
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString(){
        return " Nome: "+this.nome+"\n";
    }

}
