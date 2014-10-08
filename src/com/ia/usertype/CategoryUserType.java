package com.ia.usertype;

public class CategoryUserType extends StringUserType {

	public static CategoryUserType AMOR = new CategoryUserType("Amor");
	public static CategoryUserType MEDO = new CategoryUserType("Medo");
	public static CategoryUserType RELIGIAO = new CategoryUserType("Religião");
	
	public CategoryUserType(){
		super();
	}
	
	public CategoryUserType(String string) {
		super(string, string);
	}
	
}
