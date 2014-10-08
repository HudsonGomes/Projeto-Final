package com.ia.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ia.hibernatepersistent.AbstractHibernatePersistent;

@Entity
@Table(name="evaluation")
public class Evaluation extends AbstractHibernatePersistent {
    
    private static final long serialVersionUID = 2657786123456778L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="phrase_id")
    private Phrase phrase;
    
    @Column(name="grade")
    private BigDecimal grade;
    
    public void setId(Serializable id) {
        this.id = (Long)id;
    }

    public Serializable getId() {
        return this.id;
    }

	public Phrase getPhrase() {
		return phrase;
	}

	public void setPhrase(Phrase phrase) {
		this.phrase = phrase;
	}

	public BigDecimal getGrade() {
		return grade;
	}

	public void setGrade(BigDecimal grade) {
		this.grade = grade;
	}

}
