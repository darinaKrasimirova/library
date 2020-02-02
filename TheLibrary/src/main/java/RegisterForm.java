import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterForm {
    private JButton confirmButton;
    private JTextField textFieldName;
    private JTextField textFieldFamilyName;
    private JButton cancelButton;
    public JPanel panel;
    private JCheckBox adminCheckBox;

    public RegisterForm() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame hp=new JFrame("HomePage");
                hp.setContentPane(new HomePage().panel);
                hp.setVisible(true);
                hp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                hp.setSize(500,450);
                Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
                activeWindow.dispose();
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int id=LibraryService.newUser(textFieldName.getText(), textFieldFamilyName.getText(), adminCheckBox.isSelected());
                JOptionPane.showMessageDialog(panel,"Your id is "+id);
            }
        });
    }
}
