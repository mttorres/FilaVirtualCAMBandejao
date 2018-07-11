package clientserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


import mywebcam.Person;

public class Cliente {

	public static void main(String[] args) throws UnknownHostException, IOException
	{
	    Socket clienteSocket = new Socket("127.0.0.1", 12345);
														// nesse caso o proprio computador
		System.out.println("O cliente se conectou ao servidor!");
		
		int id = Integer.parseInt(args[0]);
		File f = new File (args[1]);
		
		Person person = new Person(id, f);		
	    
	    OutputStream outputStream = clienteSocket.getOutputStream();
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	    //Person p = new Person(person.getId(),person.getPic());
	    //objectOutputStream.writeObject(person);
	    
	    //objectOutputStream.writeObject(new String("another object from the client"));
	    objectOutputStream.writeInt(person.getId());
	    objectOutputStream.writeLong(person.getPic().length());
	    FileInputStream in = new FileInputStream(person.getPic());
	    byte[] buf = new byte[4096];
        
        while(true){
            int len = in.read(buf);
            if(len == -1) break;
            objectOutputStream.write(buf, 0, len);
        }
        objectOutputStream.close();
        in.close();
	    outputStream.close();
	    clienteSocket.close();
	    
	    System.out.println("Enviando: "+person);
	    //f.delete();

	}
	
	

}
