package com.spoctexter.occasions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.friends.Friend;
import com.spoctexter.texts.Text;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDate occasionDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(
            name = "friend_id",
            nullable = false,
            referencedColumnName = "friendId",
            foreignKey = @ForeignKey (
                    name = "friend_id_fk"
            )
    )
    Friend friend;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "occasion",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Text> texts = new ArrayList<>();

    public Occasion(String occasionName, LocalDate occasionDate) {
        this.occasionName = occasionName;
        this.occasionDate = occasionDate;
    }

    public Occasion(String occasionName, LocalDate occasionDate, Friend friend) {
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

    public LocalDate getOccasionDate() {
        return occasionDate;
    }

    public void setOccasionDate(LocalDate occasionDate) {
        this.occasionDate = occasionDate;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void addText(Text text) {
        if (!this.texts.contains(text)) {
            this.texts.add(text);
            text.setOccasion(this);
        }
    }

    public void removeText(Text text) {
        if (this.texts.contains(text)) {
            this.texts.remove(text);
            text.setOccasion(null);
        }
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
