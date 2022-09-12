package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.DTO.NotesDTO;
import com.bridgelabz.fundoonotesservice.exception.NotesException;
import com.bridgelabz.fundoonotesservice.model.NotesModel;
import com.bridgelabz.fundoonotesservice.repository.INotesRepository;
import com.bridgelabz.fundoonotesservice.util.Response;
import com.bridgelabz.fundoonotesservice.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    RestTemplate restTemplate;
    @Override
    public Response addNotes(NotesDTO notesDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            if (userId != 0) {
                NotesModel notesModel = new NotesModel(notesDTO);
                notesModel.setUserId(userId);
                notesModel.setRegisterDate(LocalDateTime.now());
                notesRepository.save(notesModel);
                String body = "Mentor added Successfully with id  :" + notesModel.getId();
                String subject = "Mentor added Successfully....";
                mailService.send(notesModel.getEmailId(), body, subject);
                return new Response("Mentor Added Successfully", 200, notesModel);
            }
        }
        throw new NotesException(400, "Token is Wrong");
    }

    @Override
    public Response getNotesById(long noteId) {
            Optional<NotesModel> notesModel = notesRepository.findById(noteId);
            return new Response("Notes Found With id..", 200, notesModel.get());
    }

    @Override
    public List<NotesModel> getAllNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findById(userId);
        if (notesModel.isPresent()) {
            List<NotesModel> getAllNotes = notesRepository.findAll();
            if (getAllNotes.size() > 0)
                return getAllNotes;
            else
                throw new NotesException(400, "No Data Found");
            }
        }
        throw new NotesException(400, "Notes not found");
    }


    @Override
    public Response updateNotes(long noteId, NotesDTO notesDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, noteId);
        if (notesModel.isPresent()) {
            notesModel.get().setTitle(notesDTO.getTitle());
            notesModel.get().setDescription(notesDTO.getDescription());
            notesModel.get().setLabelId(notesDTO.getLabelId());
            notesModel.get().setEmailId(notesDTO.getEmailId());
            notesModel.get().setColor(notesDTO.getColor());
            notesModel.get().setReminderTime(notesDTO.getReminderTime());
            notesRepository.save(notesModel.get());
            return new Response("Notes Updated Successfully", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response trash(Long noteId, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, noteId);
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
        }
        throw new NotesException(400, "Notes Cannot restored");
    }

    @Override
    public List<NotesModel> getTrashNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NotesModel> notesModels = notesRepository.findByUserId(userId);
            List<NotesModel> trashNotes = notesModels.stream().filter(notes -> notes.isTrash() == true)
                    .collect(Collectors.toList());
            return trashNotes;
        }
        throw new NotesException(400, "Token Wrong");
    }

    @Override
    public Response removeTrash(Long noteId, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, noteId);
        if (notesModel.isPresent()) {
            notesModel.get().setTrash(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Restored", 200, null);
            }
        }
        throw new NotesException(400, "Trash is Empty");
    }

    @Override
    public Response delete(Long noteId, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, noteId);
        if (notesModel.isPresent()) {
            notesRepository.delete(notesModel.get());
            return new Response("Notes Deleted", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response archiveNote(Long noteId, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, noteId);
        if (notesModel.isPresent()){
            notesModel.get().setArchive(!notesModel.get().isArchive());
            notesModel.get().setArchive(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Not Archived", 200, null);
        } else {
            return new Response("Notes Archived", 200, null);
            }
        }
        throw new NotesException(400, "Token is Wrong");
//            if (notesModel.get().isArchive()==true){
//                notesModel.get().setArchive(false);
//                notesRepository.save(notesModel.get());
//                return new Response("Notes not archived", 200, null);
//            } else {
//                notesModel.get().setArchive(true);
//                notesRepository.save(notesModel.get());
//                return new Response("Notes Archived", 200, null);
//            }
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
    public Response pinNotes(Long noteId, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> isIdPresent = notesRepository.findByUserIdAndId(userId, noteId);
        if (isIdPresent.isPresent()){
            isIdPresent.get().setPin(true);
            notesRepository.save(isIdPresent.get());
            return new Response("Notes pinned", 200, isIdPresent.get());
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response unPinNotes(Long noteId, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> isNotesPresent = notesRepository.findByUserIdAndId(userId, noteId);
        if (isNotesPresent.isPresent()){
            isNotesPresent.get().setPin(false);
            notesRepository.save(isNotesPresent.get());
            return new Response("Notes pinned", 200, isNotesPresent.get());
        }
        throw new NotesException(400, "Notes Not Found");
    }


    @Override
    public List<NotesModel> getPinnedNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NotesModel> notes = notesRepository.findByUserId(userId);
        List<NotesModel> pinNotes = notes.stream().filter(note -> note.isPin()==true)
                .collect(Collectors.toList());
        return pinNotes;
//        for (NotesModel note : notes) {
//            if (note.isPin()){
//                List<NotesModel> list = new ArrayList<>();
//                list.add(note);
//                return list;
//            }
//        }
//        throw new NotesException(400, "Not Pinned");
    }
    @Override
    public Response addColour(String token, Long noteId, String colour) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, noteId);
        if (notesModel.isPresent()){
            notesModel.get().setColor(colour);
            notesRepository.save(notesModel.get());
            return new Response("Notes colour set", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response getColour(Long noteId) {
        Optional<NotesModel> notesModel = notesRepository.findById(noteId);
        if (notesModel.isEmpty()){
            throw new NotesException(400, "Notes Not Found");
        } else {
            String colour = notesModel.get().getColor();
            return new Response("Note colour changed", 200, colour);
        }
    }
}
