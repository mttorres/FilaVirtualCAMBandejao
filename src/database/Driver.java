package database;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mywebcam.Person;
public class Driver {
	String localhost = "8889";
	String user = "root";
	String password = "root";
	String query = "select * from fila"; // query 
	Connection myConn;
	public static void main(String[] args){
						
	}
	public Driver (){
		//get connection
		//submit sql query
		//process data
		try{
			//get connection NO WINDOWS
		//	this.myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bandejuff","root","root");
			
			this.myConn = DriverManager.getConnection("jdbc:mysql://localhost:"+localhost+"/bandejuff","root","root");
			//create a statement  
			Statement myStmt = myConn.createStatement(); // usado para "manipular o banco de dados" de uma dada conexao
						
			//submit sql query
			ResultSet myRs = myStmt.executeQuery(query);
			//process result sets
			/*
			while(myRs.next()){
				System.out.println(myRs.getString("id") + ", " + myRs.getString("foto"));
			}
			*/
			System.out.println("Database connection is opened!");
			
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
	}
	public boolean insertPerson(Person p) throws SQLException{
		
		
		String insertQuery = 
		"INSERT INTO `fila` (`id`, `foto`) VALUES ('"+
		(p.getId())+"', '"+p.getPic().getPath()+"');";
		System.out.println(insertQuery);
		System.out.println(p.getPic().getPath());
		Statement insertStmt = this.myConn.createStatement();
		/***************
		CHECAR COMO INSERIR JAVA MYSQL
		****************/
		boolean myRs = insertStmt.execute(insertQuery);
		System.out.println("Insert? "+!myRs);
		
		return !myRs;
		
	}
	public boolean deletePerson() throws SQLException{
		String selectQuery = "SELECT * FROM `fila` WHERE line_number IS NOT NULL ORDER BY line_number ASC LIMIT 1;";
		String deleteQuery = "DELETE FROM `fila` WHERE line_number IS NOT NULL ORDER BY line_number ASC LIMIT 1;";
		Statement selectStmt = this.myConn.createStatement();
		ResultSet myRs1 = selectStmt.executeQuery(selectQuery);		
		Statement deleteStmt = this.myConn.createStatement();
		boolean myRs = deleteStmt.execute(deleteQuery);
		//if(!myRs) System.out.println("Saiu da fila o id: "+myRs1.getString("line_number"));
		return myRs;
	}
	// modificado por Mateus
        
        public File getFirstImg() throws SQLException, FileNotFoundException, IOException
        {
        	Statement stat = this.myConn.createStatement();
            String select =  "SELECT * FROM `fila` WHERE line_number IS NOT NULL ORDER BY line_number ASC LIMIT 1;";
            ResultSet rs = stat.executeQuery(select);
            File image = null;
            String picname = null;
            if(rs.next())
            {
            //	picname = rs.getString("foto");
            	Integer pId = rs.getInt("id");
            	picname = pId.toString();            	
                
            	image = new File("html/imgs/"+picname+".jpg");
            	System.out.println(image.exists());
            }
            return image;
          }
        
        public ArrayList<Person> getAll() throws SQLException, FileNotFoundException, IOException
        {
        	ArrayList<Person> mylist = new ArrayList(); 
            Statement stat = this.myConn.createStatement();
            String select =  "SELECT * FROM bandejuff.fila";
            ResultSet rs = stat.executeQuery(select);
            while(rs.next())
            {
                File image = null;
                Integer pId = rs.getInt("id");
                String picname = rs.getString("foto");
          //      Blob pFoto =  rs.getBlob("foto");
          //      InputStream bin = pFoto.getBinaryStream();
          //      FileOutputStream bout = new FileOutputStream(pId.toString()+".jpg");
          //      while ((bytesRead = bin.read(bbuf)) != -1) {  
	//		     bout.write(bbuf, 0, bytesRead);  
	//		}
         //     image = new File(pId.toString()+".jpg");
                int id = Integer.parseInt(pId.toString());
                image = new File("html/imgs/"+pId.toString()+".jpg");
                System.out.println("CHECANDO ID: "+id);
                Person p = new Person(id, image);
                mylist.add(p);
            }
            return mylist;
        }
        
        
	
        public boolean repetido(int id) throws FileNotFoundException, SQLException, IOException
        {
        	ArrayList<Person> pessoas = this.getAll();
        	for(Person a: pessoas)
        	{
        		if(a.getId() == id)
        		{
        			return true;
        		}
        	}
        	return false;
        
        }
    	
}





