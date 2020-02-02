import Library.Book;
import Library.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JFrame{
    private JButton registerButton;
    private JButton logInButton;
    private JTextField textFieldEnterId;
    private JButton logButton;
    public JPanel panel;


    public HomePage(){

        textFieldEnterId.setVisible(false);
        logButton.setVisible(false);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textFieldEnterId.setVisible(true);
                logButton.setVisible(true);
            }
        });

        textFieldEnterId.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                textFieldEnterId.setText(null);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JFrame rf = new JFrame("Register form");
                rf.setContentPane(new RegisterForm().panel);
                rf.setVisible(true);
                rf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                rf.setSize(500, 450);
                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();
              }
            });


        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int id=Integer.parseInt(textFieldEnterId.getText());
                if (LibraryService.log(id)!=null) {
                    User u=LibraryService.log(id);
                    JFrame al = new JFrame("Logged in as "+u.getName()+", id- "+u.getId());
                    AfterLogging afterLogging = new AfterLogging(id);
                    al.setContentPane(afterLogging.panel);
                    if (!u.getIs_admin()){
                        afterLogging.showAllBooksButton.setVisible(false);}
                    al.setVisible(true);
                    al.setSize(500, 450);
                    al.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                    activeWindow.dispose();

                    DefaultListModel<Book> listModel=new DefaultListModel<>();
                    afterLogging.listBooks.setModel(listModel);
                    for(Book b: LibraryService.loadUserBooks(id)) {
                        listModel.addElement(b);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(panel,"Invalid id");
                }
            }
        });
    }

}
