package mywebcam;

import java.io.File;
import java.io.Serializable;

public class Person implements Serializable{
	File picture;
	int id;
	
	public Person(int id, File picture){
		this.id = id;
		this.picture = picture;
			
	}
	public int getId(){
		return this.id;
	}
	public File getPic(){
		return this.picture;
	}
	
	public String toString(){
		return "("+Integer.toString(this.getId())+","+getPic().getAbsolutePath()+")";
	}
}
