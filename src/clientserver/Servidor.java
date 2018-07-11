package clientserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import mywebcam.Person;
import database.Driver;
public class Servidor extends javax.swing.JFrame
{
	
//	public static Queue<Aluno> fila;
	public static void main(String args[])  throws IOException, ClassNotFoundException, SQLException
	{
		ServerSocket servidor = new ServerSocket(12345); 
		System.out.println("Porta 12345 ABERTA!\n");
		
		Queue<Person> fila = new LinkedList<Person>(); 
		Driver driver = new Driver();
		webPage page = new webPage();
		String picName = "test";
		int i = 0;
		while (true)
			try {
			
				/***** MODIFICADO POR BRUNO *****/
				 Socket cliente = servidor.accept(); 
				 
				 System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());				 				
				 
				 InputStream inputStream = cliente.getInputStream();
				 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				 String personId = Integer.toString(objectInputStream.readInt());				 
				 long size =objectInputStream.readLong();
				 
				 FileOutputStream fos = new FileOutputStream("html/imgs/"+personId+".jpg");
				 byte[] buf = new byte[4096];
	                while(true){
	                    int len = objectInputStream.read(buf);
	                    if(len == -1) break;
	                    
	                    fos.write(buf, 0, len);
	                }
                 fos.flush();
                 fos.close();
                 System.out.println("Transferência concluída com sucesso! Imagem em: "+personId);
				 System.out.println("Matricula: "+personId+" entrou na fila.");
				 
				 if(Integer.parseInt(personId)==987654321)
				 {
					 break;
				 }
				 
				 //note que nesse caso de teste estamos registrando alunos inves de 
				 //buscar as informa��es existentes pela matricula/nome digitada
				 
				 Person a1 = new Person(Integer.parseInt(personId), new File(personId)); // instanciamos um aluno com os dados coletados

				 /********************TEMPORARIO*******************/
				 if(a1.getId()<200000000){
					 driver.deletePerson();
				 }
				 /********************TEMPORARIO*******************/
				 
				 fila.add(a1); // salvamos na fila
				 System.out.println("Aluno adicionado, existem:  " + (fila.size()-1) + " aluno(s) na sua frente");
				 
				 driver.insertPerson(a1);
				 page.insertPerson(a1);
				
				 System.out.println();
				 System.out.println("F I L A ");
				 System.out.println();
				 for( Person a: fila)
				 {
					 System.out.println(a);
				 }
				 inputStream.close();
				 objectInputStream.close();				 				 
				 cliente.close(); // fechamamos nosso cliente para ir para o proximo
			
			} catch (Exception e){
				e.printStackTrace();
			}
		
		fila.clear();
		System.out.println("S E R V I D O R     F E C H A D O ");
		servidor.close();
		
	}
}
