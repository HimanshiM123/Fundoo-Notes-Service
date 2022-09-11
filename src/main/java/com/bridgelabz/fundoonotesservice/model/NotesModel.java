package com.bridgelabz.fundoonotesservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notes")
@Data
public class NotesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="Title")
    private String title;
    @Column(name="NoteDescription")
    private String description;
    @Column(name="UserId")
    private long userId;
    @Column(name = "registeredDate")
    private LocalDateTime registerDate;
    @Column(name = "UpdatedDate")
    private LocalDateTime updateDate;
    @Column(name="trash")
    private boolean trash;
    @Column(name="archieve")
    private boolean isArchive;
    @Column(name="pin")
    private boolean pin;
    @Column(name="labelid")
    private Long labelId;
    @Column(name="emailid")
    private String emailId;
    @Column(name="color")
    private String color;
    @Column(name="reminder")
    private LocalDateTime reminderTime;
    List<String> collaborator;
}
