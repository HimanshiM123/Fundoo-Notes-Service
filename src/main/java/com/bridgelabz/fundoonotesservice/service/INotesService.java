package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.DTO.NotesDTO;
import com.bridgelabz.fundoonotesservice.model.NotesModel;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INotesService {
    Response addNotes(NotesDTO notesDTO, String token);

    Response getNotesById(long noteId);
    List<NotesModel> getAllNotes(String token);

    Response updateNotes(long noteId, NotesDTO notesDTO, String token);

    Response trash(Long noteId, String token);

    Response delete(Long noteId, String token);

    Response archiveNote(Long noteId, String token);


    List<NotesModel> getTrashNotes(String token);


    List<NotesModel> getArchiveNotes(String token);

    List<NotesModel> getPinnedNotes(String token);

    Response removeTrash(Long noteId, String token);

    Response addColour(String token, Long noteId, String colour);

    Response getColour(Long noteId);

    Response pinNotes(Long noteId, String token);

    Response unPinNotes(Long noteId, String token);
}
