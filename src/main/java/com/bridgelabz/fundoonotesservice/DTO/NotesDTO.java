package com.bridgelabz.fundoonotesservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class NotesDTO {
    private String title;
    private String description;
}
