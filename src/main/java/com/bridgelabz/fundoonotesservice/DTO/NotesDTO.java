package com.bridgelabz.fundoonotesservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class NotesDTO {
    private String title;
    private String description;
    private long userId;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private boolean trash;
    private boolean isArchive;
    private boolean pin;
    private Long labelId;
    private String emailId;
    private String color;
    private LocalDateTime reminderTime;
    List<String> collaborator;

}
