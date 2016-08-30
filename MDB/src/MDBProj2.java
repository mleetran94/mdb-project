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
import org.apache.commons.io.FilenameUtils;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@SuppressWarnings("serial")
public class MDBProj2 extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField txtDBLoad;
	private JLabel lblDBDir;
	private JLabel lblFolderSaveDir;
	private JTextField txtSaveDir;
	private JButton btnLoadTable;
	
	private String nameOfFile;
	//column name & type name arrays are initialized with nothing in them because combo boxes throw error if loaded with null items
	private String[] columnNames = new String[0];
	private String[] columnTypeNames = new String[0];
	
	private JTextField txtLabelNameCount;

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
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }


	/*
	//utilizes user's output file naming convention to 
	public String[] getColumnNames(String columnName, int numOfCol){
		String[] updatedName = new String[numOfCol];
		
		for(int index = 0; index < numOfCol; index++){
			int colNameHolder = 0;
			
			for(int a = 0; a < columnName.length(); a++){
				char ch = columnName.charAt(i);
				if(ch == '_'){
					updatedName[a] = updatedName + ", ";
					colNameHolder = a;
				}
				else
					updatedName[a] = updatedName + ch;
			}
			
		}
		
		return updatedName;
	}
	*/
	
	
	/**
	 * Create the frame.
	 */
	public MDBProj2() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1049, 888);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JButton btnExportFiles = new JButton("Export...");	
		btnExportFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				
				//connection belongs to java.sql.Connection class
				Connection myConn = null;
				Statement myStmnt = null;
				ResultSet rs = null;
				
				InputStream input = null;
				FileOutputStream output = null;
				
				String filePath = txtDBLoad.getText();
				//System.out.println("File Path Location:" + filePath);
				String folderPath = txtSaveDir.getText();
				//System.out.println("Folder Path Directory:" + folderPath);
				
				try {
					myConn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
					
					//query to execute
					myStmnt = myConn.createStatement();
					String query = "select * from " + nameOfFile;
					rs = myStmnt.executeQuery(query);
					//table.setModel(DbUtils.resultSetToTableModel(rs));
					
					//Setup a handle to the file
					while(rs.next()){
						if (rs.getBinaryStream("file") != null) {
							String imgName = "" + rs.getString("key") + "_" + rs.getString("f_name") + "_" + rs.getString("l_name");
							File theFile = new File("" + folderPath + "\\" + imgName + ".jpg");
							output = new FileOutputStream(theFile);
							input = rs.getBinaryStream("file");
							//System.out.println("Reading id from database...");
							//System.out.println(query);
							
							byte[] buffer = new byte[1024];
							while (input.read(buffer) > 0){
								output.write(buffer);
							}
							//System.out.println("\nSaved file: " + theFile.getAbsolutePath());
							//System.out.println("\nCompleted successfully at counter: " + rs.getRow() + " !");
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
		btnExportFiles.setBounds(825, 565, 150, 23);
		contentPane.add(btnExportFiles);
		
		JScrollPane SPaneModelTable = new JScrollPane();
		SPaneModelTable.setBounds(45, 259, 930, 290);
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
		
		JButton btnImportDb = new JButton("Import DB");
		btnImportDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(fcLoad.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fcLoad.getSelectedFile();
					String path = file.getPath();
					nameOfFile = FilenameUtils.removeExtension(file.getName());
					
					txtDBLoad.setText(path);
				} else {
					//nothing happens yet
					txtDBLoad.setText("No path");
				}
			}
		});
		btnImportDb.setBounds(45, 35, 115, 29);
		contentPane.add(btnImportDb);
		
		
		//Create a file save chooser
		final JFileChooser fcSave = new JFileChooser();
		
		JButton btnSaveLocation = new JButton("Save location");
		btnSaveLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e4) {
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
		
		btnLoadTable = new JButton("Load Table");
		btnLoadTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//connection belongs to java.sql.Connection class
				String[] colNames2;
				String[] colTypes2;
				Connection myConn = null;
				Statement myStmnt = null;
				ResultSet rs = null;
				ResultSetMetaData mrs = null;
				
				String filePath = txtDBLoad.getText();
				//System.out.println("File Path Location:" + filePath);
				
				try {
					myConn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
					
					//query to execute
					myStmnt = myConn.createStatement();
					String query = "select * from " + nameOfFile;
					rs = myStmnt.executeQuery(query);
					
					//obtains column information for user to specification later in program 
					mrs = rs.getMetaData();
					colNames2 = new String[mrs.getColumnCount()];
					colTypes2 = new String[mrs.getColumnCount()];
					for(int i = 1; i <= mrs.getColumnCount(); i++){
						colNames2[i-1] = mrs.getColumnName(i);
						colTypes2[i-1] = mrs.getColumnTypeName(i);
					}
					
					columnNames = colNames2;
					columnTypeNames = colTypes2;
					
					
					
					//models table
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
				} catch (Exception e2) {
					e2.printStackTrace();
				} 
			}
		});
		btnLoadTable.setBounds(45, 217, 115, 29);
		contentPane.add(btnLoadTable);
		
		/* removed generic jcombobox raw type error warning
		 * windowbuilder does not support
		
		
		
		JComboBox<String> cBoxColLbl1 = new JComboBox<>(columnNames);
		cBoxColLbl1.setEnabled(false);
		cBoxColLbl1.setToolTipText("First label in name");
		cBoxColLbl1.setBounds(45, 659, 300, 26);
		contentPane.add(cBoxColLbl1);
		
		JComboBox<String> cBoxColLbl2 = new JComboBox<>(columnNames);
		cBoxColLbl2.setEnabled(false);
		cBoxColLbl2.setToolTipText("Second label in name");
		cBoxColLbl2.setBounds(360, 659, 300, 26);
		contentPane.add(cBoxColLbl2);
		
		JComboBox<String> cBoxColLbl3 = new JComboBox<>(columnNames);
		cBoxColLbl3.setEnabled(false);
		cBoxColLbl3.setToolTipText("Third label in name");
		cBoxColLbl3.setBounds(675, 659, 300, 26);
		contentPane.add(cBoxColLbl3);
		*/
		
		//combo boxes for label names that specify naming convention of output files
		//*************************
		JComboBox cBoxColLbl1 = new JComboBox();
		cBoxColLbl1.setEnabled(false);
		cBoxColLbl1.setToolTipText("First label in name");
		cBoxColLbl1.setBounds(45, 659, 300, 26);
		contentPane.add(cBoxColLbl1);
		
		JComboBox cBoxColLbl2 = new JComboBox();
		cBoxColLbl2.setEnabled(false);
		cBoxColLbl2.setToolTipText("Second label in name");
		cBoxColLbl2.setBounds(360, 659, 300, 26);
		contentPane.add(cBoxColLbl2);
		
		JComboBox cBoxColLbl3 = new JComboBox();
		cBoxColLbl3.setEnabled(false);
		cBoxColLbl3.setToolTipText("Third label in name");
		cBoxColLbl3.setBounds(675, 659, 300, 26);
		contentPane.add(cBoxColLbl3);
		//*************************
		
		
		//Combo box for number of name label combo boxes to enable
		txtLabelNameCount = new JTextField();
		txtLabelNameCount.setText("Label name count:");
		txtLabelNameCount.setBounds(45, 605, 146, 26);
		contentPane.add(txtLabelNameCount);
		txtLabelNameCount.setColumns(10);
		String[] lblCount = {"1","2","3"};
		JComboBox cBoxLblCount = new JComboBox(lblCount);
		//updates label name jcomboboxes items upon clicking labelcount
		//columnNames array values are not initialized upon startup and need to be updated, which is what this action event does
		cBoxLblCount.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				for(String s:columnNames){
					cBoxColLbl1.addItem(s);
					cBoxColLbl2.addItem(s);
					cBoxColLbl3.addItem(s);
				}
			}
		});
		//enables or disables number column label combo boxes based on user input (enables left to right, disables right to left)
		cBoxLblCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String selectedCount = (String)cb.getSelectedItem();
				if(selectedCount == "1"){
					cBoxColLbl1.setEnabled(true);
					cBoxColLbl2.setEnabled(false);
					cBoxColLbl3.setEnabled(false);
				} else if (selectedCount == "2") {
					cBoxColLbl1.setEnabled(true);
					cBoxColLbl2.setEnabled(true);
					cBoxColLbl3.setEnabled(false);
				} else if (selectedCount == "3") {
					cBoxColLbl1.setEnabled(true);
					cBoxColLbl2.setEnabled(true);
					cBoxColLbl3.setEnabled(true);
				}
				
			}
		});
		cBoxLblCount.setBounds(195, 605, 102, 26);
		contentPane.add(cBoxLblCount);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnImportDb, btnSaveLocation, btnLoadTable, cBoxColLbl1, cBoxColLbl2, cBoxColLbl3, btnExportFiles, lblDBDir, SPaneModelTable, table, txtDBLoad, lblFolderSaveDir, txtSaveDir, txtLabelNameCount}));
	}
}
