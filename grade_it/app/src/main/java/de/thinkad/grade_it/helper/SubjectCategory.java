package de.thinkad.grade_it.helper;

import java.util.ArrayList;

public class SubjectCategory {
	
	String cateID;
	int weight;
	ArrayList<Integer> grades;
	public String getCateID() {
		return cateID;
	}
	public void setCateID(String cateID) {
		this.cateID = cateID;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public ArrayList<Integer> getGrades() {
		return grades;
	}
	public void setGrades(ArrayList<Integer> grades) {
		this.grades = grades;
	}

	
	
	@Override
	public String toString(){
		StringBuilder ret = new StringBuilder();
		
		ret.append(cateID+"   ");
		ret.append(weight+"    ");
		for(int i =0; i<grades.size();i++){
		ret.append(grades.get(i)+"; ");
		}
		return ret.toString();
	}

}
