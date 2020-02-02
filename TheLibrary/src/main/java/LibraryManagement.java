import Library.Author;
import Library.Book;
import Library.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibraryManagement {
    private JTextField textFieldTitle;
    private JTextField textFieldAuthor;
    public JList listFoundBooks;
    private JButton cancelButton;
    private JButton borrowOrAddButton;
    public JPanel panel;
    private JButton editButton;
    private JButton deleteButton;

    int id;
    boolean lmode;
    List<Book> booksToShow;
    boolean searchMode=true;

    public LibraryManagement(boolean mode, final int userId) {
        //mode=true-admin editing library list; mode=false-user borrowing book
        id=userId;
        lmode=mode;
        if(mode){
           borrowOrAddButton.setText("Add");
           cancelButton.setText("Back");
           booksToShow=LibraryService.loadAllBooks();
        }
        else{
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            booksToShow=LibraryService.loadAvailableBooks();
        }
       // listFoundBooks.setModel(new DefaultListModel<Book>());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                User u=LibraryService.log(id);
                JFrame al=new JFrame("Logged in as "+u.getName()+", id- "+u.getId());
                AfterLogging afterLogging = new AfterLogging(id);
                al.setContentPane(afterLogging.panel);
                if (u.getIs_admin()){
                    afterLogging.showAllBooksButton.setVisible(true);}
                else{ afterLogging.showAllBooksButton.setVisible(false);}
                al.setVisible(true);
                al.setSize(500,450);
                al.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();

                DefaultListModel<Book> listModel=new DefaultListModel<>();
                afterLogging.listBooks.setModel(listModel);
                for(Book b: LibraryService.loadUserBooks(id)) {
                    listModel.addElement(b);
                }
            }
        });
        borrowOrAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(lmode){//if in editor mode
                    Book b=new Book();
                    b.setTitle(textFieldTitle.getText());
                    String[] name=textFieldAuthor.getText().split(" ");
                    Author a=new Author();
                    a.setName(name[0]);
                    a.setFamily_name(name[1]);
                    b.setAuthor(a);
                    LibraryService.addBook(b);
                    DefaultListModel model=(DefaultListModel) listFoundBooks.getModel();
                    for(Book book: LibraryService.loadAllBooks()) {
                        model.addElement(book);
                    }
                }
                else{//if in borrowing mode
                    Book b= (Book) listFoundBooks.getSelectedValue();
                    LibraryService.borrowBook(b, id);
                    DefaultListModel model=(DefaultListModel) listFoundBooks.getModel();
                    model.removeElementAt(listFoundBooks.getSelectedIndex());
                    textFieldTitle.setText("");
                    textFieldAuthor.setText("");
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean changed=false;
                Book b=(Book)listFoundBooks.getSelectedValue();
                Author a=b.getAuthor();
                if(b.getTitle()!=textFieldTitle.getText()){
                    b.setTitle(textFieldTitle.getText());
                    changed=true;
                }
                else b.setTitle(b.getTitle());
                String[] name=textFieldAuthor.getText().split(" ");
                if(b.getAuthor().getName()!=name[0]||b.getAuthor().getFamily_name()!=name[1]) {
                    Author au = new Author();
                    au.setName(name[0]);
                    au.setFamily_name(name[1]);
                    b.setAuthor(au);
                    changed=true;
                }
                else b.setAuthor(b.getAuthor());
                if(changed) {
                    LibraryService.editBook(b, a);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Book b=(Book)listFoundBooks.getSelectedValue();
                if(b.getUser()==null) {
                    LibraryService.deleteBook(b);
                    DefaultListModel model = (DefaultListModel) listFoundBooks.getModel();
                    model.removeElementAt(listFoundBooks.getSelectedIndex());
                }
                else JOptionPane.showMessageDialog(panel,"The book is borrowed, cannot be deleted ");
            }
        });
        listFoundBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(listFoundBooks.getSelectedValue()!=null){
                    Book b=(Book) listFoundBooks.getSelectedValue();
                    textFieldTitle.setText(b.getTitle());
                    textFieldAuthor.setText((b.getAuthor().getName()+" "+b.getAuthor().getFamily_name()));
                }
            }
        });
        textFieldTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(searchMode) {
                    super.keyTyped(e);
                    String text = textFieldTitle.getText();
                    DefaultListModel<Book> model = (DefaultListModel<Book>) listFoundBooks.getModel();
                    model.removeAllElements();
                    for (Book b : booksToShow) {
                        if (b.getTitle().startsWith(text)) model.addElement(b);
                    }
                }
            }
        });
        textFieldAuthor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(searchMode) {
                    super.keyTyped(e);
                    String text = textFieldAuthor.getText();
                    DefaultListModel<Book> model = (DefaultListModel<Book>) listFoundBooks.getModel();
                    model.removeAllElements();
                    for (Book b : booksToShow) {
                        if (b.getAuthor().getName().startsWith(text) || b.getAuthor().getFamily_name().startsWith(text))
                            model.addElement(b);
                    }
                }
            }
        });
        listFoundBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(listFoundBooks.getSelectedValue()!=null)searchMode=false;
            }
        });
    }
}
