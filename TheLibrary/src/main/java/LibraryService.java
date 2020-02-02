import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import Library.*;
import java.util.List;


public class LibraryService {
     static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static int newUser(String name, String familyName, boolean admin){
        User u=new User();
        u.setName(name);
        u.setFamily_name(familyName);
        u.setIs_admin(admin);
        int id;

        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        s.save(u);
        List getId=s.createNativeQuery("SELECT id FROM user WHERE name='"+name+"' AND family_name ='"+familyName+"'").list();
        Object o=getId.get(0);
        id =(int)o;
        t.commit();
        s.close();
        return id;
    }
    public static User log(int id){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Query<User> query=s.createNativeQuery("SELECT * FROM user WHERE id ="+id,User.class);
        List<User> log=query.getResultList();
        t.commit();
        s.close();
        if(log.isEmpty()){
            return null;
        }
        else {
            return log.get(0);
        }

    }
    public static void borrowBook(Book b, int userId){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Query<User> query=s.createNativeQuery("SELECT * FROM user WHERE id="+userId,User.class);
        User u=query.getResultList().get(0);
        b.setUser(u);
        s.update(b);
        t.commit();;
        s.close();
    }
    public static void addBook(Book b){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Author a=b.getAuthor();
        Query<Author> checkAuthor=s.createNativeQuery("SELECT * FROM author WHERE name =\""+a.getName()+"\"AND family_name=\""+a.getFamily_name()+"\"", Author.class);;
        List<Author> result=checkAuthor.getResultList();
        if(result.isEmpty())s.save(b);
        else {
            int id=result.get(0).getId();
            Query save=s.createNativeQuery("INSERT INTO book (title, author_id) VALUES (\""+b.getTitle()+"\", "+id+")");
            save.executeUpdate();
        }
        t.commit();;
        s.close();
    }
    public static void deleteBook(Book b){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();

        Query deleteBook=s.createNativeQuery("DELETE FROM book WHERE id="+b.getId());
        deleteBook.executeUpdate();
        Query<Book> books=s.createNativeQuery("SELECT * FROM book WHERE author_id="+b.getAuthor().getId(), Book.class);
        if(books.getResultList().isEmpty()){
            Query deleteAuthor=s.createNativeQuery("DELETE FROM author WHERE id="+b.getAuthor().getId());
            deleteAuthor.executeUpdate();
        }
        t.commit();;
        s.close();
    }
    public static void editBook(Book b,Author oldAuthor){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        s.update(b);
        int id=(int)(s.createNativeQuery("SELECT id FROM author WHERE name=\""+oldAuthor.getName()+"\" AND family_name=\""+oldAuthor.getFamily_name()+"\"").getResultList().get(0));
        s.flush();
        if(s.createNativeQuery("SELECT * FROM book WHERE author_id="+id).getResultList().isEmpty()) {
            Query delete=s.createNativeQuery("DELETE FROM author WHERE id="+id);
            delete.executeUpdate();
        }
        t.commit();;
        s.close();
    }
    public static List<Book> loadUserBooks(int id){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Query<Book> query=s.createNativeQuery("SELECT * FROM book WHERE user_id="+id,Book.class);
        List<Book> books=query.getResultList();
        t.commit();
        s.close();
        return books;
    }
    public static List<Book> loadAllBooks(){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Query<Book> query=s.createNativeQuery("SELECT * FROM book",Book.class);
        List<Book> books=query.getResultList();
        t.commit();
        s.close();
        return books;
    }
    public static List<Book> loadAvailableBooks(){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Query<Book> query=s.createNativeQuery("SELECT * FROM book WhERE user_id IS NULL",Book.class);
        List<Book> books=query.getResultList();
        t.commit();
        s.close();
        return books;
    }
    public static void returnBook(Book b){
        Session s=sessionFactory.openSession();
        Transaction t=s.beginTransaction();
        Query query=s.createNativeQuery("UPDATE book SET user_id=NULL WHERE id="+b.getId());
        query.executeUpdate();
        t.commit();
        s.close();
    }
}
