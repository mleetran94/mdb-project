import java.awt.EventQueue;
import java.awt.Image;
import java.sql.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;
	private JTextField textFieldUN;
	private JPasswordField passwordField;
	
	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
		connection = sqliteConnection.dbConnector();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 465, 166);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUN = new JLabel("Username:");
		lblUN.setBounds(95, 24, 81, 14);
		frame.getContentPane().add(lblUN);
		
		JLabel lblPW = new JLabel("Password:");
		lblPW.setBounds(95, 49, 68, 14);
		frame.getContentPane().add(lblPW);
		
		textFieldUN = new JTextField();
		textFieldUN.setBounds(186, 21, 237, 20);
		frame.getContentPane().add(textFieldUN);
		textFieldUN.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		Image imgLogin = new ImageIcon(this.getClass().getResource("/checkMark.png")).getImage();
		btnLogin.setIcon(new ImageIcon(imgLogin));
		btnLogin.setIconTextGap(15);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String query = "select * from EmployeeInfo where username=? and password=? ";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, textFieldUN.getText() );
					pst.setString(2, passwordField.getText() );
					
					ResultSet rs = pst.executeQuery();
					
					int count = 0;
					while(rs.next()){
						count++;
						
					}
					
					if(count == 1)
					{
						JOptionPane.showMessageDialog(null, "Username and password is correct");
						//closes frame and opens second window
						frame.dispose();
						EmployeeInfo emplInfor = new EmployeeInfo();
						emplInfor.setVisible(true);
					}
					else if(count > 1)
					{
						JOptionPane.showMessageDialog(null, "Duplicate Username and password");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Username or password are incorrect. Re-enter account details");
					}
					
					rs.close();
					pst.close();
				}catch(Exception e){
					
					JOptionPane.showMessageDialog(null, e);
					
				}
				
			}
		});
		btnLogin.setBounds(186, 78, 106, 33);
		frame.getContentPane().add(btnLogin);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(186, 46, 237, 20);
		frame.getContentPane().add(passwordField);
		
		JLabel lblLogin = new JLabel("");
		//these 2 lines target login.png and insert it into JLabel
		Image img = new ImageIcon(this.getClass().getResource("/login.png")).getImage();
		lblLogin.setIcon(new ImageIcon(img));
		
		lblLogin.setBounds(10, 10, 75, 75);
		frame.getContentPane().add(lblLogin);
	}
}
