//import java.io.FileInputStream;
//import net.proteanit.sql.DbUtils;

import java.awt.EventQueue;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Scanner;
import javax.swing.*;



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
	
	
	
	/**
	 * Create the frame.
	 */
	public MDBProj2() {
		setAlwaysOnTop(true);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 335);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadTable = new JButton("Load Data");
		btnLoadTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				
				//connection belongs to java.sql.Connection class
				Connection myConn = null;
				Statement myStmnt = null;
				ResultSet rs = null;
				//PreparedStatement pst = null;
				
				InputStream input = null;
				FileOutputStream output = null;
				
				
				//copy pasta blank try catch
				try {
					//conneccts employeeInfo to the database using .dbConnector
					//for testing uses
					System.out.println("C:\\\\Users\\\\1\\\\mdb-project\\\\SQLite\\\\TestTable.sqlite");
					
					//user input path to .sqlite file
					System.out.println("******\tInsert Path\t******");
					System.out.println("Note: use double \\\\ for pathing");
					System.out.println("Example path:C:\\\\Users\\\\PCName\\\\Databases\\\\filename.sqlite");
					System.out.print("Path to database.sqlite:");
					String inputDB =  sc.next();
					txtDBLoad.setText(inputDB);
					myConn = DriverManager.getConnection("jdbc:sqlite:" + inputDB);
					
					
					
					//statement to execute
					myStmnt = myConn.createStatement();
					String query = "select key, f_name, l_name, file from testTable ";
					rs = myStmnt.executeQuery(query);
					
					
					//Setup a handle to the file
					System.out.print("\nPath to save files to:");
					String inputSave = sc.next();
					txtSaveDir.setText(inputSave);
					
					
					while(rs.next()){
						if (rs.getBinaryStream("file") != null) {
							String imgName = "" + rs.getString("key") + "_" + rs.getString("f_name") + "_" + rs.getString("l_name");
							File theFile = new File("" + inputSave + "\\" + imgName + ".jpg");
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		btnLoadTable.setBounds(10, 11, 89, 23);
		contentPane.add(btnLoadTable);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(118, 14, 230, 85);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		txtDBLoad = new JTextField();
		txtDBLoad.setBounds(45, 162, 597, 20);
		contentPane.add(txtDBLoad);
		txtDBLoad.setColumns(10);
		
		lblDBDir = new JLabel("Database loaded directory:");
		lblDBDir.setBounds(10, 137, 164, 14);
		contentPane.add(lblDBDir);
		
		lblFolderSaveDir = new JLabel("Folder saved directory:");
		lblFolderSaveDir.setBounds(10, 193, 144, 14);
		contentPane.add(lblFolderSaveDir);
		
		txtSaveDir = new JTextField();
		txtSaveDir.setBounds(45, 218, 597, 20);
		contentPane.add(txtSaveDir);
		txtSaveDir.setColumns(10);
	}
	
	
}
