package mywebcam;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clientserver.Cliente;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;


@SuppressWarnings("serial")
public class TakeSnapshotFromVideo extends JFrame {

	private class SnapMeAction extends AbstractAction {

		public SnapMeAction() {
			super("Tirar foto");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				for (int i = 0; i < webcams.size(); i++) {
					Webcam webcam = webcams.get(i);
					File file = new File(String.format("test-"+picNumber+".jpg"));					
					ImageIO.write(webcam.getImage(), "JPG", file);
					System.out.format("Image for %s saved in %s \n", webcam.getName(), file);
					
					imageFile = file.getPath();
					photo = new JLabel(new ImageIcon(imageFile));
					photo.setVisible(true);					
										
					for (WebcamPanel panel : panels) {
						panel.setVisible(false);
						panel.stop();
												
						panel.add(photo);
						panel.setVisible(true);
												
					}	
					btSnapMe.setEnabled(false);
					btSnapMe.setVisible(false);
					picNumber++;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	private class ConfirmId extends AbstractAction{
		public ConfirmId(){
			super ("OK");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			id = studentId.getText();
			if(validateId(id)){							
				btConfirmId.setEnabled(false);
			}
			
		}
		boolean validateId(String id){
			if ((id.matches("[0-9]+") && (id.length() > 7) && (id.length() < 10))) {
				personId = Integer.parseInt(id);
				System.out.println("Recebi personid: "+personId);
				invalidId.setVisible(false);
				idTooShort.setVisible(false);
				studentId.setEnabled(false);
				return true;
				
			}
			if (id.matches("[0-9]+")){				
				invalidId.setVisible(false);
				idTooShort.setVisible(true);
				return false;
			}									
			studentIdPanel.add(separator);
			invalidId.setVisible(true);
			idTooShort.setVisible(false);
			return false;
			
		}
		
	}
	private class GetInLine extends AbstractAction {
		public GetInLine(){
			super("Entrar na fila");
			
			
		}
		@Override
		public void actionPerformed(ActionEvent e){
			if(btSnapMe.isEnabled()){
				takePic.setVisible(true);
			}
			else if(btConfirmId.isEnabled()){
				enterId.setVisible(true);
			}
			else {
				enterId.setVisible(false);
				takePic.setVisible(false);
			}
			File f = new File (imageFile);
			Person p = new Person(personId, f);
			//String args[] = { Integer.toString(p.getId()), p.getPic().getAbsolutePath()};
			String args[] = { studentId.getText(), imageFile};
			
			/*System.out.println("f e p: "+ f + ", "+ p);
			System.out.println("p.getId(): "+p.getId()+" p.getPic(): "+p.getPic().getAbsolutePath());
			System.out.println("criado args: "+args);*/
			
			try {
				Cliente.main(args);				
				resetGui();
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	

	
	JLabel photo = null;
	private Executor executor = Executors.newSingleThreadExecutor();

	private Dimension size = WebcamResolution.QQVGA.getSize();

	private List<Webcam> webcams = Webcam.getWebcams();
	private List<WebcamPanel> panels = new ArrayList<WebcamPanel>();
	
	private JTextField studentId = studentId = new JTextField(20);
	private JLabel invalidId = new JLabel("Sua matricula deve conter apenas numeros \n");
	private JLabel idTooShort = new JLabel("Sua matricula deve conter 8 ou 9 numeros \n");
	private JLabel enterId = new JLabel ("Insira uma matricula valida antes de entrar na fila");
	private JLabel takePic = new JLabel ("Tire uma foto sua antes de entrar na fila");
	private JLabel separator = new JLabel(new ImageIcon("imgs/separator.png"));
	

	private JButton btSnapMe = new JButton(new SnapMeAction());
	private JButton btGetIn = new JButton(new GetInLine());
	private JButton btConfirmId = new JButton (new ConfirmId());
	
	private JPanel logoPanel = new JPanel();
	private JPanel studentIdPanel = new JPanel();
	private JPanel camPanel = new JPanel();        
	private JPanel submittPanel = new JPanel();
	
	private String imageFile = "";
	private String id="";
	
	private int personId;
	int picNumber=0;
	

	public TakeSnapshotFromVideo() {

		super("BandejUFF");
					    
	    //top
	    JLabel logo = new JLabel(new ImageIcon("imgs/bandejuff-logo.png"));		
        //JLabel separator = new JLabel(new ImageIcon("imgs/separator.png"));
		separator.setVisible(true);
		logoPanel.add(logo);
		logoPanel.add(separator);
                
		//middle		
		JLabel studentIdLabel = new JLabel("Insira sua matricula UFF: ");		
                        
        
        studentIdPanel.add(studentIdLabel);				
		studentIdPanel.add(studentId);
		studentIdPanel.add(btConfirmId);		
		studentIdPanel.add(invalidId);
		studentIdPanel.add(idTooShort);
					
		invalidId.setVisible(false);
		idTooShort.setVisible(false);
		studentIdPanel.add(separator);
                

		for (Webcam webcam : webcams) {
			webcam.setViewSize(size);
			WebcamPanel panel = new WebcamPanel(webcam, size, true);
			panel.setFPSDisplayed(false);
			panel.setFillArea(true);
			panels.add(panel);
		}		
                
		btSnapMe.setEnabled(true);		
		
		for (WebcamPanel panel : panels) {
			studentIdPanel.add(panel);
		}

		
		studentIdPanel.add(btSnapMe);                
		submittPanel.add(enterId);
		submittPanel.add(takePic);		
		submittPanel.add(btGetIn, BorderLayout.SOUTH);
		
		enterId.setVisible(false);
		takePic.setVisible(false);
        
        
        add(logoPanel, BorderLayout.NORTH);
        add(studentIdPanel, BorderLayout.CENTER);
        add(camPanel,BorderLayout.SOUTH);
        add(submittPanel, BorderLayout.SOUTH);
                
		pack();
		setVisible(true);
		setSize(600,500);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void resetGui(){			    
	    String message = "Voce entrou na fila virtual.";
	    String title = "Confirmacao";
	    // display the JOptionPane showConfirmDialog
	    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.DEFAULT_OPTION);
		
		for (int i = 0; i < webcams.size(); i++) {
			Webcam webcam = webcams.get(i);			
						
			photo.setVisible(false);												
			
			for (WebcamPanel panel : panels) {
				
				panel.start();
										
				//panel.add(photo);
				panel.setVisible(true);
										
			}
			studentId.setEnabled(true);
			studentId.setText("");
			btConfirmId.setEnabled(true);
			btSnapMe.setEnabled(true);
			btSnapMe.setVisible(true);			
			
		}
	}

	public static void main(String[] args) {
		new TakeSnapshotFromVideo();
	}
}