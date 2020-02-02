package Library;
import javax.persistence.*;

@Entity
@Table(name="book")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    public int getId(){
        return  id;
    }
    public void setId(int id){
        this.id=id;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;
    public Author getAuthor() { return author;}
    public void setAuthor(Author author) { this.author = author; }

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    public User getUser() { return user;}
    public void setUser(User user) { this.user = user; }

    @Column(name="title")
    private String title;
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }

    @Override
    public String toString() {
        return title+" - "+author.getName()+" "+author.getFamily_name();
    }
}

