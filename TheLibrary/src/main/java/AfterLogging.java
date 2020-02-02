import Library.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AfterLogging {
    public JList<Book> listBooks;
    private JButton borrowNewButton;
    public JButton backButton;
    public JPanel panel;
    public JButton showAllBooksButton;
    private JButton returnBookButton;
    private JLabel warningLabel;

    int userId;

    public AfterLogging(int id) {

        userId=id;
        borrowNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame borrow=new JFrame("Borrow Book");
                LibraryManagement lm=new LibraryManagement(false,userId);
                borrow.setContentPane(lm.panel);
                borrow.setVisible(true);
                borrow.setSize(400,350);
                borrow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();

                DefaultListModel<Book>listModel=new DefaultListModel<>();
                lm.listFoundBooks.setModel(listModel);
                for(Book b :LibraryService.loadAvailableBooks()){
                    listModel.addElement(b);
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame hp=new JFrame("Home Page");
                hp.setContentPane(new HomePage().panel);
                hp.setVisible(true);
                hp.setSize(500,450);
                hp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();
            }
        });
        showAllBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame libraryManagement=new JFrame("Library Management");
                LibraryManagement lm=new LibraryManagement(true,userId);
                libraryManagement.setContentPane(lm.panel);
                libraryManagement.setVisible(true);
                libraryManagement.setSize(400,350);
                libraryManagement.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();

                DefaultListModel<Book>listModel=new DefaultListModel<>();
                lm.listFoundBooks.setModel(listModel);
                for(Book b :LibraryService.loadAllBooks()){
                    listModel.addElement(b);
                }

            }
        });
        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Book b=listBooks.getSelectedValue();
                LibraryService.returnBook(b);
                DefaultListModel model=(DefaultListModel) listBooks.getModel();
                model.removeElementAt(listBooks.getSelectedIndex());
            }
        });
    }

}
