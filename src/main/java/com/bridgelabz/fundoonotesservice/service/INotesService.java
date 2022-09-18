package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.DTO.NotesDTO;
import com.bridgelabz.fundoonotesservice.model.NotesModel;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INotesService {
    Response addNotes(NotesDTO notesDTO, String token);

    Response getNotesById(long id);
    List<NotesModel> getAllNotes(String token);

    Response updateNotes(long id, NotesDTO notesDTO, String token);

    Response trash(Long id, String token);

    Response delete(Long id, String token);

    Response archiveNote(Long id, String token);


    List<NotesModel> getTrashNotes(String token);


    List<NotesModel> getArchiveNotes(String token);

    List<NotesModel> getPinnedNotes(String token);

    Response removeTrash(Long id, String token);

    Response addColour(String token, Long id, String colour);

    Response getColour(Long id);

    Response pinNotes(Long id, String token);

    Response unPinNotes(Long id, String token);

    Response addCollaborator(String token, String email, Long id, List<String> collaborator);

    NotesModel setRemainder(String remainderTime, String token, Long id);
}
