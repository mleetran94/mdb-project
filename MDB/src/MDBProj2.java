//import java.io.FileInputStream;
//import net.proteanit.sql.DbUtils;

import java.awt.EventQueue;
import javax.swing.border.EmptyBorder;
import net.proteanit.sql.DbUtils;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.JFileChooser;



@SuppressWarnings("serial")
public class MDBProj2 extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField txtDBLoad;
	private JLabel lblDBDir;
	private JLabel lblFolderSaveDir;
	private JTextField txtSaveDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MDBProj2 frame = new MDBProj2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//converts single backslashes in path string to double backslashes
	//necessary to be properly read in connection path
	public String getPath(String inputPath){
		String outputPath = inputPath;
		
		for(int i = 0; i < inputPath.length(); i++)
		{
			char comparingCharacter = inputPath.charAt(i);
			if(comparingCharacter == '\\'){//single quotes to compare char
				outputPath = outputPath + "\\\\";
				
				//accounts for extra character ( '\' to '\\') as to no
				i++;//prevents being stuck in loop since next character will be another backslash if not iterated an extra time
			} else {
				outputPath = outputPath + comparingCharacter;
			}
		}
		
		
		return outputPath;
	}
	
	/**
	 * Create the frame.
	 */
	public MDBProj2() {
		setAlwaysOnTop(true);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 636);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadTable = new JButton("Export...");	
		btnLoadTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				
				//connection belongs to java.sql.Connection class
				Connection myConn = null;
				Statement myStmnt = null;
				ResultSet rs = null;
				
				InputStream input = null;
				FileOutputStream output = null;
				
				String filePath = txtDBLoad.getText();
				System.out.println("File Path Location:" + filePath);
				String folderPath = txtSaveDir.getText();
				System.out.println("Folder Path Directory:" + folderPath);
				
				try {
					myConn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
					
					//statement to execute
					myStmnt = myConn.createStatement();
					String query = "select key, f_name, l_name, file from testTable ";
					rs = myStmnt.executeQuery(query);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					//Setup a handle to the file
					while(rs.next()){
						if (rs.getBinaryStream("file") != null) {
							String imgName = "" + rs.getString("key") + "_" + rs.getString("f_name") + "_" + rs.getString("l_name");
							File theFile = new File("" + folderPath + "\\" + imgName + ".jpg");
							output = new FileOutputStream(theFile);
							
							input = rs.getBinaryStream("file");
							System.out.println("Reading id from database...");
							System.out.println(query);
							
							byte[] buffer = new byte[1024];
							while (input.read(buffer) > 0){
								output.write(buffer);
							}
							System.out.println("\nSaved file: " + theFile.getAbsolutePath());
							System.out.println("\nCompleted successfully at counter: " + rs.getRow() + " !");
						}
							
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		btnLoadTable.setBounds(45, 220, 150, 23);
		contentPane.add(btnLoadTable);
		
		JScrollPane SPaneModelTable = new JScrollPane();
		SPaneModelTable.setBounds(45, 259, 792, 289);
		contentPane.add(SPaneModelTable);
		
		table = new JTable();
		SPaneModelTable.setViewportView(table);
		
		txtDBLoad = new JTextField();
		txtDBLoad.setBounds(45, 80, 597, 20);
		contentPane.add(txtDBLoad);
		txtDBLoad.setColumns(10);
		
		lblDBDir = new JLabel("Database loaded directory:");
		lblDBDir.setBounds(171, 42, 219, 14);
		contentPane.add(lblDBDir);
		
		lblFolderSaveDir = new JLabel("Folder saved directory:");
		lblFolderSaveDir.setBounds(210, 129, 219, 14);
		contentPane.add(lblFolderSaveDir);
		
		txtSaveDir = new JTextField();
		txtSaveDir.setBounds(45, 165, 597, 20);
		contentPane.add(txtSaveDir);
		txtSaveDir.setColumns(10);
		
		
		//Create a file load chooser
		final JFileChooser fcLoad = new JFileChooser();
		
		JButton btnLoadDb = new JButton("Load DB");
		btnLoadDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(fcLoad.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fcLoad.getSelectedFile();
					String path = file.getPath();
					
					txtDBLoad.setText(path);
				} else {
					//nothing happens yet
					txtDBLoad.setText("No path");
				}
			}
		});
		btnLoadDb.setBounds(45, 35, 115, 29);
		contentPane.add(btnLoadDb);
		
		
		//Create a file save chooser
		final JFileChooser fcSave = new JFileChooser();
		
		JButton btnSaveLocation = new JButton("Save location");
		btnSaveLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fcSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if(fcSave.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String path = fcSave.getSelectedFile().toString();
					
					txtSaveDir.setText(path);
				} else {
					//nothing happens yet
					txtSaveDir.setText("No path");
				}
			}
		});
		btnSaveLocation.setBounds(45, 122, 150, 29);
		contentPane.add(btnSaveLocation);
	}
}
