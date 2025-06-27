import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; // Import for custom cell renderer

class Book {
    private String bookId;
    private String supplier;
    private String supplierId;
    private String bookTitle;
    private String author;
    private int quantity;
    private String dateAdded;
    private String dateUpdated;

    public Book(String bookId, String supplier, String supplierId, String bookTitle, String author, int quantity, String dateAdded, String dateUpdated) {
        this.bookId = bookId;
        this.supplier = supplier;
        this.supplierId = supplierId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
        this.dateUpdated = dateUpdated;
    }

    // Getters
    public String getBookId() { return bookId; }
    public String getSupplier() { return supplier; }
    public String getSupplierId() { return supplierId; }
    public String getBookTitle() { return bookTitle; }
    public String getAuthor() { return author; }
    public int getQuantity() { return quantity; }
    public String getDateAdded() { return dateAdded; }
    public String getDateUpdated() { return dateUpdated; }

    // Setters
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setAuthor(String author) { this.author = author; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }
    public void setDateUpdated(String dateUpdated) { this.dateUpdated = dateUpdated; }

    @Override
    public String toString() {
        return bookId + "," + supplier + "," + supplierId + "," + bookTitle + "," + author + "," + quantity + "," + dateAdded + "," + dateUpdated;
    }
}

class Borrower {
    private String borrowerId;
    private String name;
    private String course;
    private String borrowedBookTitle; // To link to the book
    private String borrowedBookAuthor; // To display in the borrowed books table
    private String borrowDate;
    private String returnDate;

    public Borrower(String borrowerId, String name, String course, String borrowedBookTitle, String borrowedBookAuthor, String borrowDate, String returnDate) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.course = course;
        this.borrowedBookTitle = borrowedBookTitle;
        this.borrowedBookAuthor = borrowedBookAuthor;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters
    public String getBorrowerId() { return borrowerId; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public String getBorrowedBookTitle() { return borrowedBookTitle; }
    public String getBorrowedBookAuthor() { return borrowedBookAuthor; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }

    @Override
    public String toString() {
        return borrowerId + "," + name + "," + course + "," + borrowedBookTitle + "," + borrowedBookAuthor + "," + borrowDate + "," + returnDate;
    }
}

public class BookInventoryApp extends JFrame {
    private List<Book> books = new ArrayList<>();
    private List<Borrower> borrowers = new ArrayList<>(); 

    private final String SUPPLY_DETAIL_FILE = "supplydetail.txt";
    private final String BORROWERS_FILE = "borrowers.txt"; 
    private final String ADMIN_CREDENTIALS_FILE = "admin_credentials.txt";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private JTabbedPane tabbedPane; 

    private JTable inventoryTable; 
    private DefaultTableModel inventoryTableModel;
    private JButton btnAddInventory, btnUpdateInventory, btnBorrow, btnAddQuantity; 

    private JTable borrowedBooksTable;
    private DefaultTableModel borrowedBooksTableModel;
    private JButton btnReturnBorrowed; 

    private JTable overdueBooksTable; // New table for overdue books
    private DefaultTableModel overdueBooksTableModel; // New model for overdue books

    public BookInventoryApp() {
        setTitle("Book Inventory and Borrowing System");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.foreground", Color.BLACK);
        UIManager.put("Button.background", Color.LIGHT_GRAY);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("TextField.caretForeground", Color.BLACK);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", Color.BLACK);
        UIManager.put("PasswordField.caretForeground", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("TableHeader.background", Color.LIGHT_GRAY);
        UIManager.put("TableHeader.foreground", Color.BLACK);
        UIManager.put("TabbedPane.background", Color.WHITE);
        UIManager.put("TabbedPane.foreground", Color.BLACK);
        UIManager.put("TitledBorder.titleColor", Color.BLACK);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.BLACK);
        UIManager.put("List.background", Color.WHITE); 
        UIManager.put("List.foreground", Color.BLACK);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.BLACK);
        UIManager.put("OptionPane.buttonForeground", Color.BLACK);
        UIManager.put("OptionPane.buttonBackground", Color.LIGHT_GRAY);
        UIManager.put("JScrollPane.background", Color.WHITE);
        UIManager.put("Viewport.background", Color.WHITE);
        UIManager.put("JFrame.activeCaptionBorderColor", Color.BLACK);
        UIManager.put("JFrame.inactiveCaptionBorderColor", Color.GRAY);
        
    }

    private void initComponents() {
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        titlePanel.setBackground(Color.WHITE);


        JLabel titleLabel = new JLabel("SuperCali");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24)); 
        titleLabel.setForeground(Color.BLACK); 

        titlePanel.add(titleLabel);
        mainContentPanel.add(titlePanel, BorderLayout.NORTH);


        tabbedPane = new JTabbedPane(); 

        JPanel inventoryPanel = new JPanel(new BorderLayout());
        setupInventoryPanel(inventoryPanel);
        tabbedPane.addTab("Inventory", inventoryPanel);

        JPanel borrowedBooksPanel = new JPanel(new BorderLayout());
        setupBorrowedBooksPanel(borrowedBooksPanel);
        tabbedPane.addTab("Borrowed Books", borrowedBooksPanel);

        // New tab for Overdue Books
        JPanel overdueBooksPanel = new JPanel(new BorderLayout());
        setupOverdueBooksPanel(overdueBooksPanel);
        tabbedPane.addTab("Overdue Books", overdueBooksPanel);

        mainContentPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainContentPanel); 
    }

    private void setupInventoryPanel(JPanel panel) {

        String[] columnNames = {"Book ID", "Supplier", "Supplier ID", "Book Title", "Author", "Quantity", "Date Added", "La Updated"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for Inventory
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAddInventory = new JButton("Add Inventory");
        btnUpdateInventory = new JButton("Update");
        btnBorrow = new JButton("Borrow"); 
        btnAddQuantity = new JButton("Add Quantity"); 

        btnAddInventory.addActionListener(e -> showAddUpdateInventoryDialog(null)); 
        btnUpdateInventory.addActionListener(e -> showAddUpdateInventoryDialog(getCurrentlySelectedBook())); 
        btnBorrow.addActionListener(this::showBorrowBookDialog);
        btnAddQuantity.addActionListener(this::addQuantityToBook); // Action listener for Add Quantity

        buttonPanel.add(btnAddInventory);
        buttonPanel.add(btnUpdateInventory);
        buttonPanel.add(btnBorrow);
        buttonPanel.add(btnAddQuantity);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupBorrowedBooksPanel(JPanel panel) {
        String[] columnNames = {"Borrower ID", "Borrower Name", "Book Title", "Book Author", "Date Borrowed", "Return Date"};
        borrowedBooksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        borrowedBooksTable = new JTable(borrowedBooksTableModel);
        borrowedBooksTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRefreshBorrowed = new JButton("Refresh Borrowed List");
        btnRefreshBorrowed.addActionListener(e -> populateBorrowedBooksTable());
        
        btnReturnBorrowed = new JButton("Return Book"); 
        btnReturnBorrowed.addActionListener(this::returnBorrowedBook); 

        buttonPanel.add(btnRefreshBorrowed);
        buttonPanel.add(btnReturnBorrowed); 
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // New method to setup the Overdue Books tab
    private void setupOverdueBooksPanel(JPanel panel) {
        // Columns for overdue books: borrower's id, name, course, book title, book author, return date
        String[] columnNames = {"Borrower ID", "Borrower Name", "Course", "Book Title", "Book Author", "Return Date"};
        overdueBooksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        overdueBooksTable = new JTable(overdueBooksTableModel);
        overdueBooksTable.setAutoCreateRowSorter(true);

        // Custom cell renderer to color overdue dates red
        overdueBooksTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 5) { // Assuming "Return Date" is the 6th column (index 5)
                    try {
                        Date returnDate = dateFormat.parse(value.toString());
                        Date currentDate = new Date(); // Current date
                        if (returnDate.before(currentDate)) {
                            c.setForeground(Color.RED); // Set text color to red if overdue
                        } else {
                            c.setForeground(Color.BLACK); // Default color for not overdue
                        }
                    } catch (ParseException e) {
                        c.setForeground(Color.BLACK); // Default color if date parsing fails
                    }
                } else {
                    c.setForeground(Color.BLACK); // Default color for other columns
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(overdueBooksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRefreshOverdue = new JButton("Refresh Overdue List");
        btnRefreshOverdue.addActionListener(e -> populateOverdueBooksTable());
        
        buttonPanel.add(btnRefreshOverdue);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        loadBooksFromFile();
        loadBorrowersFromFile();
    }

    private void loadBooksFromFile() {
        books.clear();
        File file = new File(SUPPLY_DETAIL_FILE);
        if (!file.exists()) {
            System.out.println("supplydetail.txt not found. Creating a new one.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error creating file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLY_DETAIL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 8);
                if (parts.length == 8) {
                    try {
                        String bookId = parts[0];
                        String supplier = parts[1];
                        String supplierId = parts[2];
                        String bookTitle = parts[3];
                        String author = parts[4];
                        int quantity = Integer.parseInt(parts[5]);
                        String dateAdded = parts[6];
                        String dateUpdated = parts[7];
                        books.add(new Book(bookId, supplier, supplierId, bookTitle, author, quantity, dateAdded, dateUpdated));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line in supplydetail.txt: " + line + " - " + e.getMessage());
                        JOptionPane.showMessageDialog(this, "Error parsing line in supplydetail.txt: " + line + "\nEnsure file matches current expected format (BookId,Supplier,SupplierId,BookTitle,Author,Quantity,DateAdded,DateUpdated). Consider deleting 'supplydetail.txt' to create a new, compatible one if errors persist.", "File Format Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.err.println("Skipping malformed line in supplydetail.txt due to incorrect number of parts: " + line);
                    JOptionPane.showMessageDialog(this, "Error parsing line in supplydetail.txt: " + line + "\nExpected 8 parts, but found " + parts.length + ". Consider deleting 'supplydetail.txt' to create a new, compatible one if errors persist.", "File Format Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUPPLY_DETAIL_FILE))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving books: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBorrowersFromFile() {
        borrowers.clear();
        File file = new File(BORROWERS_FILE);
        if (!file.exists()) {
            System.out.println("borrowers.txt not found. Creating a new one.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error creating borrowers file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 7); 
                if (parts.length == 7) {
                    borrowers.add(new Borrower(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]));
                } else {
                    System.err.println("Skipping malformed line in borrowers.txt: " + line);
                    JOptionPane.showMessageDialog(this, "Error parsing line in borrowers.txt: " + line + "\nExpected 7 parts, but found " + parts.length + ". Consider deleting 'borrowers.txt' to create a new, compatible one if errors persist.", "File Format Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading borrowers: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBorrowersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWERS_FILE))) {
            for (Borrower borrower : borrowers) {
                writer.write(borrower.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving borrowers: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateInventoryTable() {
        inventoryTableModel.setRowCount(0);
        for (Book book : books) {
            inventoryTableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getSupplier(),
                    book.getSupplierId(),
                    book.getBookTitle(),
                    book.getAuthor(),
                    book.getQuantity(),
                    book.getDateAdded(),
                    book.getDateUpdated()
            });
        }
    }

    private void populateBorrowedBooksTable() {
        borrowedBooksTableModel.setRowCount(0); 
        for (Borrower borrower : borrowers) {
            borrowedBooksTableModel.addRow(new Object[]{
                    borrower.getBorrowerId(),
                    borrower.getName(),
                    borrower.getBorrowedBookTitle(),
                    borrower.getBorrowedBookAuthor(),
                    borrower.getBorrowDate(),
                    borrower.getReturnDate()
            });
        }
    }

    // New method to populate the Overdue Books table
    private void populateOverdueBooksTable() {
        overdueBooksTableModel.setRowCount(0); // Clear existing data
        Date currentDate = new Date(); // Get current date for comparison

        for (Borrower borrower : borrowers) {
            try {
                Date returnDate = dateFormat.parse(borrower.getReturnDate());
                // Check if the return date is before the current date
                if (returnDate.before(currentDate)) {
                    overdueBooksTableModel.addRow(new Object[]{
                        borrower.getBorrowerId(),
                        borrower.getName(),
                        borrower.getCourse(),
                        borrower.getBorrowedBookTitle(),
                        borrower.getBorrowedBookAuthor(),
                        borrower.getReturnDate() // This date will be colored red by the renderer
                    });
                }
            } catch (ParseException e) {
                System.err.println("Error parsing return date for borrower " + borrower.getBorrowerId() + ": " + borrower.getReturnDate());
                // Optionally, add a row indicating a parsing error or skip it
            }
        }
    }

    private Book getCurrentlySelectedBook() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book from the inventory table.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String selectedBookId = inventoryTableModel.getValueAt(selectedRow, 0).toString(); // Book ID is at index 0
        return books.stream()
                .filter(b -> b.getBookId().equals(selectedBookId))
                .findFirst()
                .orElse(null);
    }
    private void showAddUpdateInventoryDialog(Book bookToEdit) {
        JDialog dialog = new JDialog(this, (bookToEdit == null ? "Add New Book" : "Update Book"), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300); 
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField txtDialogSupplier = new JTextField(15);
        JTextField txtDialogSupplierId = new JTextField(15);
        JTextField txtDialogBookId = new JTextField(15);
        JTextField txtDialogBookTitle = new JTextField(15);
        JTextField txtDialogAuthor = new JTextField(15);
        JTextField txtDialogQuantity = new JTextField(15);
        JTextField txtDialogDate = new JTextField(15); 

        txtDialogDate.setEditable(false);

        if (bookToEdit != null) {
            txtDialogSupplier.setText(bookToEdit.getSupplier());
            txtDialogSupplierId.setText(bookToEdit.getSupplierId());
            txtDialogBookId.setText(bookToEdit.getBookId());
            txtDialogBookId.setEditable(false); 
            txtDialogBookTitle.setText(bookToEdit.getBookTitle());
            txtDialogAuthor.setText(bookToEdit.getAuthor());
            txtDialogQuantity.setText(String.valueOf(bookToEdit.getQuantity()));
            txtDialogDate.setText(dateFormat.format(new Date())); 
        } else {
            String currentDate = dateFormat.format(new Date());
            txtDialogDate.setText(currentDate);
        }

        formPanel.add(new JLabel("Supplier:"));         formPanel.add(txtDialogSupplier);
        formPanel.add(new JLabel("Supplier ID:"));      formPanel.add(txtDialogSupplierId);
        formPanel.add(new JLabel("Book ID:"));          formPanel.add(txtDialogBookId);
        formPanel.add(new JLabel("Book Title:"));       formPanel.add(txtDialogBookTitle);
        formPanel.add(new JLabel("Book Author:"));      formPanel.add(txtDialogAuthor);
        formPanel.add(new JLabel("Quantity:"));         formPanel.add(txtDialogQuantity);
        formPanel.add(new JLabel("Date:"));             formPanel.add(txtDialogDate);

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            try {
                String bookId = txtDialogBookId.getText().trim();
                String supplier = txtDialogSupplier.getText().trim();
                String supplierId = txtDialogSupplierId.getText().trim();
                String bookTitle = txtDialogBookTitle.getText().trim();
                String author = txtDialogAuthor.getText().trim();
                int quantity = Integer.parseInt(txtDialogQuantity.getText().trim());

                String currentDate = dateFormat.format(new Date());

                if (bookId.isEmpty() || supplier.isEmpty() || supplierId.isEmpty() || bookTitle.isEmpty() || author.isEmpty() || quantity <= 0) {
                    JOptionPane.showMessageDialog(dialog, "All fields (Supplier, Supplier ID, Book ID, Book Title, Book Author, Quantity) must be filled, and Quantity must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bookToEdit == null) { 
                    boolean idExists = books.stream().anyMatch(b -> b.getBookId().equals(bookId));
                    if (idExists) {
                        JOptionPane.showMessageDialog(dialog, "Book with this ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    books.add(new Book(bookId, supplier, supplierId, bookTitle, author, quantity, currentDate, currentDate));
                    JOptionPane.showMessageDialog(dialog, "Book added successfully!");
                } else { 
                    Optional<Book> existingBookOpt = books.stream().filter(b -> b.getBookId().equals(bookId)).findFirst();
                    if(existingBookOpt.isPresent()) {
                        Book existingBook = existingBookOpt.get();
                        existingBook.setSupplier(supplier);
                        existingBook.setSupplierId(supplierId);
                        existingBook.setBookTitle(bookTitle);
                        existingBook.setAuthor(author);
                        existingBook.setQuantity(quantity);
                        existingBook.setDateUpdated(currentDate); 
                        JOptionPane.showMessageDialog(dialog, "Book updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Error: Book to update not found in data.", "Internal Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                saveBooksToFile();
                populateInventoryTable();
                dialog.dispose(); 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for Quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // --- Dialog for Borrowing a Book ---
    private void showBorrowBookDialog(ActionEvent e) {
        Book selectedBook = getCurrentlySelectedBook();
        if (selectedBook == null) {
            return;
        }

        if (selectedBook.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(this, "Book '" + selectedBook.getBookTitle() + "' is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Borrow Book: " + selectedBook.getBookTitle(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // 5 rows for borrower details
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField txtBorrowerId = new JTextField(15);
        JTextField txtBorrowerName = new JTextField(15);
        JTextField txtBorrowerCourse = new JTextField(15);
        JTextField txtReturnDate = new JTextField(15); // For user input

        JLabel lblBookTitle = new JLabel("Book Title: " + selectedBook.getBookTitle());
        JLabel lblBookAuthor = new JLabel("Book Author: " + selectedBook.getAuthor());

        formPanel.add(new JLabel("Borrower's ID:"));     formPanel.add(txtBorrowerId);
        formPanel.add(new JLabel("Borrower's Name:"));   formPanel.add(txtBorrowerName);
        formPanel.add(new JLabel("Borrower's Course:")); formPanel.add(txtBorrowerCourse);
        formPanel.add(new JLabel("Return Date (YYYY-MM-DD):")); formPanel.add(txtReturnDate);


        dialog.add(formPanel, BorderLayout.CENTER);

        JButton btnConfirmBorrow = new JButton("Confirm Borrow");
        btnConfirmBorrow.addActionListener(event -> {
            try {
                String borrowerId = txtBorrowerId.getText().trim();
                String borrowerName = txtBorrowerName.getText().trim();
                String borrowerCourse = txtBorrowerCourse.getText().trim();
                String returnDateStr = txtReturnDate.getText().trim();
                String borrowDate = dateFormat.format(new Date()); // Current date for borrowing

                if (borrowerId.isEmpty() || borrowerName.isEmpty() || borrowerCourse.isEmpty() || returnDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All borrower fields and return date must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dateFormat.parse(returnDateStr); 

                // Create new Borrower record
                borrowers.add(new Borrower(borrowerId, borrowerName, borrowerCourse, selectedBook.getBookTitle(), selectedBook.getAuthor(), borrowDate, returnDateStr));

                // Decrement book quantity
                selectedBook.setQuantity(selectedBook.getQuantity() - 1);

                saveBooksToFile();
                saveBorrowersToFile();
                populateInventoryTable(); 
                populateBorrowedBooksTable(); 
                populateOverdueBooksTable(); // Refresh overdue books table after borrow
                dialog.dispose(); 
                JOptionPane.showMessageDialog(this, "Book '" + selectedBook.getBookTitle() + "' borrowed successfully by " + borrowerName + "!", "Borrow Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid return date format. Please use `yyyy-MM-dd`.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "An error occurred during borrowing: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelBorrow = new JButton("Cancel");
        btnCancelBorrow.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnConfirmBorrow);
        buttonPanel.add(btnCancelBorrow);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // --- Return Borrowed Book Action ---
    private void returnBorrowedBook(ActionEvent e) {
        int selectedRow = borrowedBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrowed book from the 'Borrowed Books' tab to return.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String borrowerId = borrowedBooksTableModel.getValueAt(selectedRow, 0).toString();
        String borrowedBookTitle = borrowedBooksTableModel.getValueAt(selectedRow, 2).toString();
        String borrowedBookAuthor = borrowedBooksTableModel.getValueAt(selectedRow, 3).toString(); 

        // Find the specific borrower record to remove
        Optional<Borrower> returnedBorrowerRecordOpt = borrowers.stream()
            .filter(b -> b.getBorrowerId().equals(borrowerId) &&
                         b.getBorrowedBookTitle().equals(borrowedBookTitle) &&
                         b.getBorrowedBookAuthor().equals(borrowedBookAuthor)) 
            .findFirst();

        if (returnedBorrowerRecordOpt.isPresent()) {
            Borrower returnedBorrowerRecord = returnedBorrowerRecordOpt.get();
            borrowers.remove(returnedBorrowerRecord); 

            Optional<Book> bookToReturnOpt = books.stream()
                    .filter(book -> book.getBookTitle().equals(borrowedBookTitle) &&
                                     book.getAuthor().equals(borrowedBookAuthor)) 
                    .findFirst();

            if (bookToReturnOpt.isPresent()) {
                Book bookToReturn = bookToReturnOpt.get();
                bookToReturn.setQuantity(bookToReturn.getQuantity() + 1); 
                JOptionPane.showMessageDialog(this, "Book '" + borrowedBookTitle + "' returned successfully by " + returnedBorrowerRecord.getName() + "!", "Return Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println("Warning: Returned book '" + borrowedBookTitle + "' (author: " + borrowedBookAuthor + ") not found in inventory. Quantity not updated.");
                JOptionPane.showMessageDialog(this, "Book returned, but internal inventory record for '" + borrowedBookTitle + "' was not found. Please check inventory manually.", "Return Info", JOptionPane.WARNING_MESSAGE);
            }

            saveBooksToFile();
            saveBorrowersToFile();
            populateInventoryTable();
            populateBorrowedBooksTable(); 
            populateOverdueBooksTable(); // Refresh overdue books table after return
        } else {
            JOptionPane.showMessageDialog(this, "No matching borrowed record found for '" + borrowedBookTitle + "' by borrower ID '" + borrowerId + "'.", "Record Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    // --- Add Quantity to Book Action ---
    private void addQuantityToBook(ActionEvent e) {
        Book selectedBook = getCurrentlySelectedBook();
        if (selectedBook == null) {
            return;
        }

        String quantityToAddStr = JOptionPane.showInputDialog(this, "Enter quantity to add to '" + selectedBook.getBookTitle() + "':", "Add Quantity", JOptionPane.PLAIN_MESSAGE);

        if (quantityToAddStr != null && !quantityToAddStr.trim().isEmpty()) {
            try {
                int quantityToAdd = Integer.parseInt(quantityToAddStr.trim());
                if (quantityToAdd <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a positive number for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                selectedBook.setQuantity(selectedBook.getQuantity() + quantityToAdd);
                selectedBook.setDateUpdated(dateFormat.format(new Date())); 
                saveBooksToFile();
                populateInventoryTable();
                JOptionPane.showMessageDialog(this, quantityToAdd + " quantity added to '" + selectedBook.getBookTitle() + "' successfully!", "Quantity Added", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- Admin Login Dialog ---
    private boolean showAdminLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Admin Login", true);
        loginDialog.setLayout(new BorderLayout()); 
        loginDialog.setSize(350, 180);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); 
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20)); 

        JTextField txtAdminId = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);
        
        formPanel.add(new JLabel("Admin ID")); 
        formPanel.add(txtAdminId);
        formPanel.add(new JLabel("Password"));
        formPanel.add(txtPassword);

        // Continue with the rest of the showAdminLoginDialog method from the original code
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogin = new JButton("Login");
        JButton btnCreateAdmin = new JButton("Create Admin");
        JButton btnExit = new JButton("Exit");

        // Flag to indicate if login was successful
        final boolean[] loginSuccessful = {false};

        btnLogin.addActionListener(e -> {
            String adminId = txtAdminId.getText();
            String password = new String(txtPassword.getPassword());
            if (verifyAdminCredentials(adminId, password)) {
                loginSuccessful[0] = true;
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid Admin ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCreateAdmin.addActionListener(e -> {
            showCreateAdminDialog(loginDialog);
        });

        btnExit.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCreateAdmin);
        buttonPanel.add(btnExit);

        loginDialog.add(formPanel, BorderLayout.CENTER);
        loginDialog.add(buttonPanel, BorderLayout.SOUTH);
        loginDialog.setVisible(true);

        return loginSuccessful[0];
    }

    private boolean verifyAdminCredentials(String adminId, String password) {
        File file = new File(ADMIN_CREDENTIALS_FILE);
        if (!file.exists()) {
            return false; // No admin credentials file means no valid admin
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_CREDENTIALS_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(adminId) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showCreateAdminDialog(JDialog parentDialog) {
        JDialog createAdminDialog = new JDialog(parentDialog, "Create Admin Account", true);
        createAdminDialog.setLayout(new BorderLayout());
        createAdminDialog.setSize(350, 200);
        createAdminDialog.setLocationRelativeTo(parentDialog);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField txtNewAdminId = new JTextField(15);
        JPasswordField txtNewPassword = new JPasswordField(15);
        JPasswordField txtConfirmPassword = new JPasswordField(15);

        formPanel.add(new JLabel("New Admin ID:"));
        formPanel.add(txtNewAdminId);
        formPanel.add(new JLabel("New Password:"));
        formPanel.add(txtNewPassword);
        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(txtConfirmPassword);

        createAdminDialog.add(formPanel, BorderLayout.CENTER);

        JButton btnCreate = new JButton("Create");
        btnCreate.addActionListener(e -> {
            String newAdminId = txtNewAdminId.getText();
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            if (newAdminId.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(createAdminDialog, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(createAdminDialog, "Passwords do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File file = new File(ADMIN_CREDENTIALS_FILE);
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(createAdminDialog, 
                    "Admin account already exists. Do you want to overwrite it?", 
                    "Overwrite Admin", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_CREDENTIALS_FILE))) {
                writer.write(newAdminId + ":" + newPassword);
                JOptionPane.showMessageDialog(createAdminDialog, "Admin account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                createAdminDialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(createAdminDialog, "Error creating admin account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> createAdminDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnCancel);
        createAdminDialog.add(buttonPanel, BorderLayout.SOUTH);

        createAdminDialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookInventoryApp app = new BookInventoryApp();
            if (app.showAdminLoginDialog()) {
                app.initComponents();
                app.loadData();
                app.populateInventoryTable();
                app.populateBorrowedBooksTable();
                app.populateOverdueBooksTable(); // Populate the new overdue books table on startup
                app.setVisible(true);
            } else {
                System.exit(0); // Exit if login fails
            }
        });
    }
}
