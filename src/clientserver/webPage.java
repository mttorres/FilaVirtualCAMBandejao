package clientserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mywebcam.Person;

public class webPage {
	String baseTop;
	
	String baseDown;
	
	String table;
	
	File indexPage;
	
	int lineNumber;
	
	webPage() throws IOException{
		this.lineNumber = 0;
		this.baseTop = "<html> <head></head> <body> </br>"
				+ "<h1 align='center'> <img align='center' src='/imgs/bandejuff-logo.png' alt='bandejuff logo' class='icon' > <br><br>Ninguem na fila </h1>";
		
		this.baseDown = "</body> </html>";
		
		this.indexPage = new File("html/index.html");
		
		this.table="";
				
		writePage(this.baseTop, this.baseDown,"");
		
	}
	public void insertPerson(Person p) throws IOException{
		this.lineNumber++;
		//MODIFICADO POR MATEUS:
		String path = p.getPic().getPath();
		System.out.println("PATH RECEBIDO: "+path);
		
		this.table = this.table + "<h2 align='center'><table><td><tr>"+lineNumber+"--- </tr><tr>"+p.getId()+"</tr>"
				+ "<tr> <img align='center' src= " + "\""+path+"\"" + "alt='foto da pessoa' width= 144 height= 176 /> </tr></td></table></h2>";
		this.baseTop="<html> <head></head> <body> </br>"
				+ "<h1 align='center'> <img align='center' src='/imgs/bandejuff-logo.png' "
				+ "alt='bandejuff logo' class='icon' > <br><br>"+lineNumber+" na fila </h1>";
		writePage(this.baseTop, this.baseDown, this.table);
	}
	void writePage(String top, String down, String table) throws IOException{
		FileWriter fw = new FileWriter(indexPage);
		fw.write(top);
		fw.write(table);
		fw.write(down);
		fw.close();
	}
	
}
