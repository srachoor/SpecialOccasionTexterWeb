package com.spoctexter.texts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spoctexter.friends.Friend;
import com.spoctexter.occasions.Occasion;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Text")
@Table
@Data
public class Text {

    @Id
    @SequenceGenerator(
            name = "text_sequence",
            sequenceName = "text_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "text_sequence"
    )
    private Long id;

    private LocalDateTime sentTime;

    private String smsMessage;

    private String sentPhone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(
            name = "occasion_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey (
                    name = "occasion_id_fk"
            )
    )
    Occasion occasion;

    public Text() {
    }

    public Text(LocalDateTime sentTime, String smsMessage, String sentPhone) {
        this.sentTime = sentTime;
        this.smsMessage = smsMessage;
        this.sentPhone = sentPhone;
    }
}
