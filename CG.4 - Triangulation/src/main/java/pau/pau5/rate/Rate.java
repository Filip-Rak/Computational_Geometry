package pau.pau5.rate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import pau.pau5.classEmployee.ClassEmployee;

import java.time.LocalDateTime;

@Entity
@Table (name = "rate")
public class Rate
{
    // Constants
    public static final int LOWER_BOUND = 0;
    public static final int UPPER_BOUND = 6;

    // Attributes
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "rating", nullable = false)
    private int rating;

    @Column (name = "comment", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn (name = "group_id")
    @JsonBackReference
    private ClassEmployee classEmployee;

    @Column (name = "date", nullable = false)
    private LocalDateTime dateTime;

    // Constructor
    public Rate(int rating, ClassEmployee group, String comment)
    {
        validateRating(rating);

        this.rating = rating;
        this.classEmployee = group;
        this.comment = comment;

        dateTime = LocalDateTime.now();
    }

    protected Rate() {}

    // Methods
    private void validateRating(int rating)
    {
        if (rating < LOWER_BOUND || rating > UPPER_BOUND)
            throw new IllegalArgumentException("Rating must be between " + LOWER_BOUND + " and " + UPPER_BOUND);
    }

    // Getters
    public int getId() { return id; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getDate() { return dateTime; }
    public ClassEmployee getClassEmployee() { return this.classEmployee; }

    // Setters
    public void setId(int id) { this.id = id; }

    public void setRating(int rating)
    {
        validateRating(rating);

        this.rating = rating;
        this.dateTime = LocalDateTime.now();
    }

    public void setComment(String comment) { this. comment = comment; }
    protected void setGroup(ClassEmployee group) { this.classEmployee = group; }
}
