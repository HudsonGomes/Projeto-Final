package com.ia.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ia.hibernatepersistent.AbstractHibernatePersistent;

@Entity
@Table(name="user")
public class User extends AbstractHibernatePersistent {
    
    private static final long serialVersionUID = 2657786123456778L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="password")
    private String password;
    
    @OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@Cascade(value={org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	@JoinColumn(name="user_id")
	private List<Evaluation> evaluations = new ArrayList<Evaluation>();
	
    
    public void setId(Serializable id) {
        this.id = (Long)id;
    }

    public Serializable getId() {
        return this.id;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	@Override
    public String toString(){
        return " Nome: "+this.name+"\n";
    }

}
