import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

class Student {
    private int id;
    private String name;
    private String gender;
    private int age;
    private String year;
    private String section;
    private String course;

    public Student(int id, String name, String gender, int age, String year, String section, String course) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.year = year;
        this.section = section;
        this.course = course;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getYear() { return year; }
    public String getSection() { return section; }
    public String getCourse() { return course; }

    @Override
    public String toString() {
        return "ID            : " + id + "\nName     : " + name + "\nGender  : " + gender + "\nAge         : " + age + "\nYear        : " + year + "\nSection  : " + section + "\nCourse   : " + course ;
    }
}

class StudentManagementGUI3 {
    private JFrame frame;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private ArrayList<Student> studentList;

    private JTextField idField, nameField, ageField, searchIdField, removeIdField, filterAgeField;
    private JComboBox<String> genderComboBox, sectionComboBox, courseComboBox, yearComboBox, 
                              filterYearComboBox, filterGenderComboBox, filterSectionComboBox, filterCourseComboBox;
    
    private Connection connection;

    public StudentManagementGUI3() {
        studentList = new ArrayList<>();
        initializeGUI();
        connectToDatabase();
        loadStudentsFromDatabase();  // Load students into the table at startup
    }

    public void initializeGUI() {
        frame = new JFrame("Advanced Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Table for displaying students
        String[] columnNames = {"ID", "Name", "Gender", "Age", "Year", "Section", "Course"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        idField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);
        
        // Age
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Age:"), gbc);
        ageField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(ageField, gbc);

        
        // Gender (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Gender:"), gbc);
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        gbc.gridx = 1;
        inputPanel.add(genderComboBox, gbc);

        
        // Year (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Year:"), gbc);
        String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
        yearComboBox = new JComboBox<>(years);
        gbc.gridx = 1;
        inputPanel.add(yearComboBox, gbc);

        // Section (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Section:"), gbc);
        String[] sections = {"Section A", "Section B", "Section C", "Section D"};
        sectionComboBox = new JComboBox<>(sections);
        gbc.gridx = 1;
        inputPanel.add(sectionComboBox, gbc);

        // Course (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Course:"), gbc);
        String[] courses = {"IT", "CSE", "ECE", "EEE", "MECH", "AIDS", "CIVIL"};
        courseComboBox = new JComboBox<>(courses);
        gbc.gridx = 1;
        inputPanel.add(courseComboBox, gbc);
        
         
        // Add Student button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Span the button across both columns
        JButton addButton = new JButton("Add Student");
        inputPanel.add(addButton, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 5, 5));

        // Panel for Search and Remove student actions
        JPanel searchRemovePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Search student by ID
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        searchRemovePanel.add(new JLabel("Enter ID to Get Student:"), gbc2);
        searchIdField = new JTextField(10);
        gbc2.gridx = 1;
        searchRemovePanel.add(searchIdField, gbc2);

        JButton getStudentButton = new JButton("Get Student");
        gbc2.gridx = 2;
        searchRemovePanel.add(getStudentButton, gbc2);

        // Remove student by ID
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        searchRemovePanel.add(new JLabel("Enter ID to Remove Student:"), gbc2);
        removeIdField = new JTextField(10);
        gbc2.gridx = 1;
        searchRemovePanel.add(removeIdField, gbc2);

        JButton removeButton = new JButton("Remove Student");
        gbc2.gridx = 2;
        searchRemovePanel.add(removeButton, gbc2);

        buttonPanel.add(searchRemovePanel);
        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Filter Panel (Moved to the top)
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Students"));
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(5, 5, 5, 5);
        gbc3.fill = GridBagConstraints.HORIZONTAL;

        // Year filter
        gbc3.gridx = 0;
        gbc3.gridy = 0;
        filterPanel.add(new JLabel("Year:"), gbc3);
        filterYearComboBox = new JComboBox<>(new String[]{"All", "1st Year", "2nd Year", "3rd Year", "4th Year"});
        gbc3.gridx = 1;
        filterPanel.add(filterYearComboBox, gbc3);

        // Gender filter
        gbc3.gridx = 2;
        filterPanel.add(new JLabel("Gender:"), gbc3);
        filterGenderComboBox = new JComboBox<>(new String[]{"All", "Male", "Female", "Other"});
        gbc3.gridx = 3;
        filterPanel.add(filterGenderComboBox, gbc3);

        // Max Age filter
        gbc3.gridx = 4;
        filterPanel.add(new JLabel("Max Age:"), gbc3);
        filterAgeField = new JTextField(5);
        gbc3.gridx = 5;
        filterPanel.add(filterAgeField, gbc3);

        // Course filter
        gbc3.gridx = 6;
        filterPanel.add(new JLabel("Course:"), gbc3);
        filterCourseComboBox = new JComboBox<>(new String[]{"All", "IT", "CSE", "ECE", "EEE", "MECH", "AIDS", "CIVIL"});
        gbc3.gridx = 7;
        filterPanel.add(filterCourseComboBox, gbc3);

        // Section filter
        gbc3.gridx = 8;
        filterPanel.add(new JLabel("Section:"), gbc3);
        filterSectionComboBox = new JComboBox<>(new String[]{"All", "Section A", "Section B", "Section C", "Section D"});
        gbc3.gridx = 9;
        filterPanel.add(filterSectionComboBox, gbc3);

        // Filter button
        JButton filterButton = new JButton("Filter");
        gbc3.gridx = 10;
        filterPanel.add(filterButton, gbc3);

        frame.add(filterPanel, BorderLayout.NORTH);

        // Add Student Action Listener
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudentToDatabase();
            }
        });

        // Get Student Action Listener
        getStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int searchId = Integer.parseInt(searchIdField.getText());
                getStudentFromDatabase(searchId);
            }
        });

        // Remove Student Action Listener
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int removeId = Integer.parseInt(removeIdField.getText());
                removeStudentFromDatabase(removeId);
            }
        });

        // Filter Action Listener
        filterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterStudentsInDatabase();
            }
        });

        frame.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            // Database connection details
            String url = "jdbc:mariadb://localhost:3306/studentsdb";
            String username = "root";
            String password = "Abinesh1010";  // Change it to your MariaDB password

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudentsFromDatabase() {
        try {
            String query = "SELECT * FROM students";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                String year = rs.getString("year");
                String section = rs.getString("section");
                String course = rs.getString("course");

                Student student = new Student(id, name, gender, age, year, section, course);
                studentList.add(student);
                tableModel.addRow(new Object[]{id, name, gender, age, year, section, course});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStudentToDatabase() {
        try {
            int id = Integer.parseInt(idField.getText());
            
            if (isIdUnique(id)) {
            
                String name = nameField.getText();
                String gender = genderComboBox.getSelectedItem().toString();
                int age = Integer.parseInt(ageField.getText());
                String year = yearComboBox.getSelectedItem().toString();
                String section = sectionComboBox.getSelectedItem().toString();
                String course = courseComboBox.getSelectedItem().toString();

                // Check for age limit
                if (age < 18 || age > 30) {
                     JOptionPane.showMessageDialog(null, "Age must be between 18 and 30.");
                     return;
                }

                // Insert into database
                String query = "INSERT INTO students (id, name, gender, age, year, section, course) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                statement.setString(2, name);
                statement.setString(3, gender);
                statement.setInt(4, age);
                statement.setString(5, year);
                statement.setString(6, section);
                statement.setString(7, course);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "Student with ID : "+ id +" is added successfully.");
                    tableModel.addRow(new Object[]{id, name, gender, age, year, section, course});
                    clearInputFields();
                }
                
            } else {
                JOptionPane.showMessageDialog(frame,"ID " + id + " already exists. Please enter a unique ID.","Error",JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DataBase Error adding student!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isIdUnique(int id) {
        for (Student student : studentList) {
            if (student.getId() == id) {
                return false; // ID is not unique
            }
        }
        return true; // ID is unique
    }

    private void getStudentFromDatabase(int studentId) {
        try {
            String query = "SELECT * FROM students WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                String year = rs.getString("year");
                String section = rs.getString("section");
                String course = rs.getString("course");

                Student student = new Student(id, name, gender, age, year, section, course);
                JOptionPane.showMessageDialog(null, student.toString(), "Student Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Student with ID : "+ studentId +" is not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " DataBase Error retrieving student!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeStudentFromDatabase(int studentId) {
        try {
            String query = "DELETE FROM students WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Student with ID : "+ studentId +" is removed successfully.");
                // Remove from the JTable
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if ((int) tableModel.getValueAt(i, 0) == studentId) {
                        tableModel.removeRow(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Student with ID : "+ studentId +" is not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DataBase Error removing student!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
     private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        sectionComboBox.setSelectedIndex(0);
        courseComboBox.setSelectedIndex(0);
        genderComboBox.setSelectedIndex(0);
        yearComboBox.setSelectedIndex(0);
    }

    private void filterStudentsInDatabase() {
        try {
            String year = filterYearComboBox.getSelectedItem().toString();
            String gender = filterGenderComboBox.getSelectedItem().toString();
            String ageText = filterAgeField.getText();
            String course = filterCourseComboBox.getSelectedItem().toString();
            String section = filterSectionComboBox.getSelectedItem().toString();

            String query = "SELECT * FROM students WHERE 1=1";
            ArrayList<String> conditions = new ArrayList<>();

            if (!year.equals("All")) {
                conditions.add("year = '" + year + "'");
            }
            if (!gender.equals("All")) {
                conditions.add("gender = '" + gender + "'");
            }
            if (!course.equals("All")) {
                conditions.add("course = '" + course + "'");
            }
            if (!section.equals("All")) {
                conditions.add("section = '" + section + "'");
            }
            if (!ageText.isEmpty()) {
                int age = Integer.parseInt(ageText);
                conditions.add("age <= " + age);
            }

            // If we have any conditions, append them to the query
            if (!conditions.isEmpty()) {
                query += " AND " + String.join(" AND ", conditions);
            }

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            // Clear the table before displaying filtered results
            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String genderRes = rs.getString("gender");
                int age = rs.getInt("age");
                String yearRes = rs.getString("year");
                String sectionRes = rs.getString("section");
                String courseRes = rs.getString("course");

                tableModel.addRow(new Object[]{id, name, genderRes, age, yearRes, sectionRes, courseRes});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DataBase Error filtering students!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new StudentManagementGUI3();
    }
}

