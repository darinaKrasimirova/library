import javax.swing.*;

public class Main {
    public static void main(String []args){
        JFrame fr=new JFrame ("Home Page");
        fr.setContentPane(new HomePage().panel);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.setVisible(true);
        fr.setSize(500,450);

    }
}
