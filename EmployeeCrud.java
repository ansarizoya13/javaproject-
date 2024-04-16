import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class EmployeeCRUDApp extends JFrame{

    private JTextField nameField;
    private JTextField ageField;
    private JCheckBox isWorkingCheckBox;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeCRUDApp() {
        setTitle("Employee CRUD Application");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JFormattedTextField();
        JLabel isWorkingLabel = new JLabel("Is Working:");
        isWorkingCheckBox = new JCheckBox();

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(ageLabel);
        inputPanel.add(ageField);
        inputPanel.add(isWorkingLabel);
        inputPanel.add(isWorkingCheckBox);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Table to show list of employees
        tableModel = new DefaultTableModel();
        employeeTable = new JTable(tableModel);
        tableModel.addColumn("Name");
        tableModel.addColumn("Age");
        tableModel.addColumn("Is Working");
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // Adding components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        // Calculate center coordinates of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        // Set the location of the frame to the center of the screen
        setLocation(x, y);

        // Make the frame non-resizable
        setResizable(false);

        setVisible(true);
    }

    public void addEmployee() {
        String name = nameField.getText();
        String age = ageField.getText();
        boolean isWorking = isWorkingCheckBox.isSelected();
        tableModel.addRow(new Object[]{name, age, isWorking});

		try
		    {
			String url = "jdbc:mysql://localhost:3306/java";
			Connection conn = DriverManager.getConnection(url,"root","siws");
			PreparedStatement st = conn.prepareStatement("insert into Employeee values(?,?)");
			//String query = "insert into Employee values(?,?)";
			st.setString(2,age);
			st.setString(1,name);

			st.executeUpdate();
			System.out.println("Query Executed");
			st.close();
			conn.close();
		    }
	       catch(SQLException se)
		    {
			se.printStackTrace();
		    }
        clearFields();
    }

    public void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            String name = nameField.getText();
            String age = ageField.getText();
	String selected = employeeTable.getValueAt(selectedRow, 0).toString();
		
            boolean isWorking = isWorkingCheckBox.isSelected();


            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.setValueAt(age, selectedRow, 1);
            tableModel.setValueAt(isWorking, selectedRow, 2);

		try
		    {
			String url = "jdbc:mysql://localhost:3306/java";
			Connection conn = DriverManager.getConnection(url,"root","siws");
			PreparedStatement st = conn.prepareStatement("update Employeee set name = ?,age = ? where name = '" + selected + "'");
			//String query = "insert into Employee values(?,?)";
			st.setString(2,age);
			st.setString(1,name);

			st.executeUpdate();
			System.out.println("Query Executed");
			st.close();
			conn.close();
		    }
	       catch(SQLException se)
		    {
			se.printStackTrace();
		    }
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
        }
    }

    public void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
	 String name = nameField.getText();
	String selected = employeeTable.getValueAt(selectedRow, 0).toString();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
	try
		    {
			String url = "jdbc:mysql://localhost:3306/java";
			Connection conn = DriverManager.getConnection(url,"root","siws");
			PreparedStatement st = conn.prepareStatement("DELETE FROM Employeee WHERE name='" + selected + "'");

			//st.setString(2,age);
			//st.setString(1,name);

			st.executeUpdate();
			System.out.println("Query Executed");
			st.close();
			conn.close();
		    }
	       catch(SQLException se)
		    {
			se.printStackTrace();
		    }
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        isWorkingCheckBox.setSelected(false);
    }
}
class EmployeeCrud{
    public static void main(String args[]) {
		
           try 
             {
 		Class.forName("com.mysql.cj.jdbc.Driver");
	        System.out.println("Connected Successfully");
	     }
	  catch(Exception e)
	     {	       
		 System.out.println("NOT Connected Successfully");
	     }
        new EmployeeCRUDApp();
    
}
}
