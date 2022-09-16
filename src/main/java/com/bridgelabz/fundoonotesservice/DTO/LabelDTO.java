package com.bridgelabz.fundoonotesservice.DTO;

import com.bridgelabz.fundoonotesservice.model.NotesModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class LabelDTO {
    private String labelName;
    private List<NotesModel> notes;
}
