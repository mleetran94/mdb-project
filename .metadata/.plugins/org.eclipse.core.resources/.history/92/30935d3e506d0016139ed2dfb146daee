import java.sql.*;
import javax.swing.*;
import java.nio.*;
import net.proteanit.sql.DbUtils;

public class SQLiteProj {

	Connection conn = null;
	PreparedStatement pst = null;
	Statement st = null;
	ResultSet rs = null;
	
	public void binders() {
		
		try{
			
			Class.forName("org.sqlite.JDBC");
			
			conn = DriverManager.getConnection("dbc:sqlite:C:\\Users\\1\\workspace\\SQLite\\EmployeeData.sqlite");
			if(!conn.isClosed()){
				JOptionPane.showMessageDialog(null, "Connection Success");
			}else{
				JOptionPane.showMessageDialog(null, "Error: Connection Unsuccessful");
			}
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
		}
		
	}
	
	public void DisplayTable(JTable jtb){
		
		try{
			
			String query = "select * from files";
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			jtb.setModel(DbUtils.resultSetToTableModel(rs));
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
}
