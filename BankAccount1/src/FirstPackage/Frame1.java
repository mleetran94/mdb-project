package FirstPackage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame1 {

	private JFrame frame;
	private JTextField textFieldNum1;
	private JTextField textFieldNum2;
	private JTextField textFieldResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 847, 519);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textFieldNum1 = new JTextField();
		textFieldNum1.setBounds(93, 44, 285, 54);
		frame.getContentPane().add(textFieldNum1);
		textFieldNum1.setColumns(10);
		
		textFieldNum2 = new JTextField();
		textFieldNum2.setBounds(388, 44, 285, 54);
		frame.getContentPane().add(textFieldNum2);
		textFieldNum2.setColumns(10);
		
		JButton buttonAdd = new JButton("+");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int num1, num2, result;
				try{
					num1 = Integer.parseInt(textFieldNum1.getText() );
					num2 = Integer.parseInt(textFieldNum2.getText() );
					result= num1 + num2;
					textFieldResult.setText(Integer.toString(result));
				}catch(Exception e1){
					
					JOptionPane.showMessageDialog(null, "Stupid Cunt");
				}
			}
		});
		buttonAdd.setBounds(93, 150, 41, 23);
		frame.getContentPane().add(buttonAdd);
		
		JButton buttonSub = new JButton("-");
		buttonSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int num1, num2, result;
				try{
					num1 = Integer.parseInt(textFieldNum1.getText() );
					num2 = Integer.parseInt(textFieldNum2.getText() );
					result= num1 - num2;
					textFieldResult.setText(Integer.toString(result));
				}catch(Exception e1){
					
					JOptionPane.showMessageDialog(null, "Stupid Cunt");
				}
			}
		});
		buttonSub.setBounds(93, 184, 41, 23);
		frame.getContentPane().add(buttonSub);
		
		JLabel lblResult = new JLabel("Result");
		lblResult.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblResult.setBounds(298, 333, 78, 46);
		frame.getContentPane().add(lblResult);
		
		textFieldResult = new JTextField();
		textFieldResult.setBounds(388, 332, 285, 54);
		frame.getContentPane().add(textFieldResult);
		textFieldResult.setColumns(10);
	}
}
