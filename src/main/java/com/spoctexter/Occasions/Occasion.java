package com.spoctexter.Occasions;

import com.spoctexter.Friends.Friend;
import com.spoctexter.UserAccountLayer.UserAccount;

import javax.persistence.*;

import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity (name = "Occasion")
@Table
public class Occasion {

    @Id
    @SequenceGenerator(
            name = "occasion_sequence",
            sequenceName = "occasion_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "occasion_sequence"
    )
    private Long id;

    private String occasionName;

    private OffsetDateTime occasionDate;

    @ManyToOne
    @JoinColumn(
            name = "friend_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey (
                    name = "friend_id_fk"
            )
    )
    Friend friend;

    public Occasion(String occasionName, OffsetDateTime occasionDate) {
        this.occasionName = occasionName;
        this.occasionDate = occasionDate;
    }

    public Occasion(String occasionName, OffsetDateTime occasionDate, Friend friend) {
        this.occasionName = occasionName;
        this.occasionDate = occasionDate;
        this.friend = friend;
    }

    public Occasion() {
        this.id = id;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOccasionName() {
        return occasionName;
    }

    public void setOccasionName(String occasionName) {
        this.occasionName = occasionName;
    }

    public OffsetDateTime getOccasionDate() {
        return occasionDate;
    }

    public void setOccasionDate(OffsetDateTime occasionDate) {
        this.occasionDate = occasionDate;
    }

    @Override
    public String toString() {
        return "Occasion{" +
                "id=" + id +
                ", occasionName='" + occasionName + '\'' +
                ", occasionDate=" + occasionDate +
                ", friend=" + friend +
                '}';
    }
}
