package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.DTO.NotesDTO;
import com.bridgelabz.fundoonotesservice.exception.NotesException;
import com.bridgelabz.fundoonotesservice.model.NotesModel;
import com.bridgelabz.fundoonotesservice.repository.INotesRepository;
import com.bridgelabz.fundoonotesservice.util.Response;
import com.bridgelabz.fundoonotesservice.util.TokenUtil;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotesService implements INotesService{
    @Autowired
    INotesRepository notesRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    MailService mailService;
    @Override
    public Response addNotes(NotesDTO notesDTO, String token) {
       Long userId = tokenUtil.decodeToken(token);
       if (userId != 0){
           NotesModel notesModel = new NotesModel(notesDTO);
           notesModel.setUserId(userId);
           notesModel.setRegisterDate(LocalDateTime.now());
           notesRepository.save(notesModel);
           String body = "Mentor added Successfully with id  :" + notesModel.getId();
           String subject = "Mentor added Successfully....";
           mailService.send(notesModel.getEmailId(), body, subject);
           return new Response("Mentor Added Successfully", 200, notesModel);
       }
        throw new NotesException(400, "Token is Wrong");
    }

    @Override
    public Response getNotesById(long id) {
        Optional<NotesModel> notesModel = notesRepository.findById(id);
        return new Response("Notes Found With id..", 200, notesModel.get());
    }

    @Override
    public List<NotesModel> getAllNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findById(userId);
        if (notesModel.isPresent()){
            List<NotesModel> getAllNotes = notesRepository.findAll();
            if (getAllNotes.size() > 0)
                return getAllNotes;
            else
                throw new NotesException(400, "No Data Found");
        }
        throw new NotesException(400, "Notes not found");
    }


    @Override
    public Response updateNotes(long id, NotesDTO notesDTO, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setTitle(notesDTO.getTitle());
            notesModel.get().setDescription(notesDTO.getDescription());
            notesModel.get().setRegisterDate(notesDTO.getRegisterDate());
            notesModel.get().setLabelId(notesDTO.getLabelId());
            notesModel.get().setEmailId(notesDTO.getEmailId());
            notesModel.get().setColor(notesDTO.getColor());
            notesModel.get().setReminderTime(notesDTO.getReminderTime());
            notesRepository.save(notesModel.get());
            return new Response("Notes Updated Successfully", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response trash(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            if (notesModel.get().isTrash()==true){
                notesModel.get().setTrash(false);
                notesRepository.save(notesModel.get());
                return new Response("Notes Restored from Trash", 200, null);
            } else {
                notesModel.get().setTrash(true);
                notesRepository.save(notesModel.get());
                return new Response("Notes Moved in Trash", 200, null);
            }
        }
        throw new NotesException(400, "Notes Cannot restored");
    }

    @Override
    public List<NotesModel> getTrashNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NotesModel> notesModels = notesRepository.findByUserId(userId);
        List<NotesModel> trashNotes = notesModels.stream().filter(notes -> notes.isTrash()==true)
                .collect(Collectors.toList());
        return trashNotes;
    }

    @Override
    public Response removeTrash(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setTrash(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Restored", 200, null);
        }
        throw new NotesException(400, "Trash is Empty");
    }

    @Override
    public Response delete(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesRepository.delete(notesModel.get());
            return new Response("Notes Deleted", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response archiveNote(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            if (notesModel.get().isArchive()==true){
                notesModel.get().setArchive(false);
                notesRepository.save(notesModel.get());
                return new Response("Notes not archived", 200, null);
            } else {
                notesModel.get().setArchive(true);
                notesRepository.save(notesModel.get());
                return new Response("Notes Archived", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found in Archive");
    }
    @Override
    public List<NotesModel> getArchiveNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NotesModel> notes = notesRepository.findByUserId(userId);
        List<NotesModel> archiveNotes = notes.stream().filter(note -> note.isArchive()==true)
                .collect(Collectors.toList());
        return archiveNotes;
    }

    @Override
    public List<NotesModel> pinned(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NotesModel> notes = notesRepository.findByUserId(userId);
        for (NotesModel note : notes) {
            if (note.isPin()){
                List<NotesModel> list = new ArrayList<>();
                list.add(note);
                return list;
            }
        }
        throw new NotesException(400, "Not Pinned");
    }
    @Override
    public Response addColour(String token, Long id, String colour) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setColor(colour);
            notesRepository.save(notesModel.get());
            return new Response("Notes colour set", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response getColour(Long id) {
        Optional<NotesModel> notesModel = notesRepository.findById(id);
        if (notesModel.isEmpty()){
            throw new NotesException(400, "Notes Not Found");
        } else {
            String colour = notesModel.get().getColor();
            return new Response("Note colour changed", 200, colour);
        }
    }


}
