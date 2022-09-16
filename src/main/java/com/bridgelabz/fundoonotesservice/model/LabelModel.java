package com.bridgelabz.fundoonotesservice.model;

import com.bridgelabz.fundoonotesservice.DTO.LabelDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "label")
@Data
public class LabelModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String labelName;
    private Long userId;
    private Long noteId;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private List<NotesModel> notes;

    public LabelModel(LabelDTO labelDTO) {
        this.labelName = labelDTO.getLabelName();
        this.notes = labelDTO.getNotes();
    }

    public LabelModel() {

    }
}
