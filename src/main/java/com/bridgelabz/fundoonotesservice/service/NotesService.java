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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /*
    Purpose : To Add Notes
     */
    @Override
    public Response addNotes(NotesDTO notesDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
                    NotesModel notesModel = new NotesModel(notesDTO);
                    notesModel.setUserId(userId);
                    notesModel.setRegisterDate(LocalDateTime.now());
                    notesRepository.save(notesModel);
                    String body = "Notes added Successfully with id  :" + notesModel.getId();
                    String subject = "Notes added Successfully....";
                    mailService.send(notesModel.getEmailId(), body, subject);
                    return new Response("Notes Added Successfully", 200, notesModel);
        }
        throw new NotesException(400, "Token is Wrong");
    }

    /*
    Purpose : To get Notes by id
     */
    @Override
    public Response getNotesById(long id) {
            Optional<NotesModel> notesModel = notesRepository.findById(id);
            return new Response("Notes Found With id..", 200, notesModel.get());
    }

    /*
    Purpose : To get Notes by Token
     */
    @Override
    public List<NotesModel> getAllNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
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

    /*
        Purpose : To update Notes
         */
    @Override
    public Response updateNotes(long id, NotesDTO notesDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesModel.get().setTitle(notesDTO.getTitle());
            notesModel.get().setDescription(notesDTO.getDescription());
            notesRepository.save(notesModel.get());
            return new Response("Notes Updated Successfully", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }

    /*
    Purpose : To Add Notes in trash
     */

    @Override
    public Response trash(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
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
        }
        throw new NotesException(400, "Notes Cannot restored");
    }

    /*
    Purpose : To get Trash Notes by token
     */

    @Override
    public List<NotesModel> getTrashNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NotesModel> notesModels = notesRepository.findByUserId(userId);
            List<NotesModel> trashNotes = notesModels.stream().filter(notes -> notes.isTrash() == true)
                    .collect(Collectors.toList());
            return trashNotes;
        }
        throw new NotesException(400, "Token Wrong");
    }

    /*
    Purpose : To remove Notes from trash or to restore
     */

    @Override
    public Response removeTrash(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesModel.get().setTrash(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Restored", 200, null);
            }
        }
        throw new NotesException(400, "Trash is Empty");
    }

    /*
    Purpose : To delete Notes
     */
    @Override
    public Response delete(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesRepository.delete(notesModel.get());
            return new Response("Notes Deleted", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }

    /*
    Purpose : To archive Notes
     */
    @Override
    public Response archiveNote(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
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

    /*
    Purpose : To get Archive Notes List
     */

    @Override
    public List<NotesModel> getArchiveNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NotesModel> notes = notesRepository.findByUserId(userId);
            List<NotesModel> archiveNotes = notes.stream().filter(note -> note.isArchive() == true)
                    .collect(Collectors.toList());
            return archiveNotes;
        }
        throw new NotesException(400, "Invalid Token");
    }

    /*
    Purpose : To pin Notes
     */

    @Override
    public Response pinNotes(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> isIdPresent = notesRepository.findByUserIdAndId(userId, id);
        if (isIdPresent.isPresent()) {
            isIdPresent.get().setPin(true);
            notesRepository.save(isIdPresent.get());
            return new Response("Notes pinned", 200, isIdPresent.get());
             }
        }
        throw new NotesException(400, "Notes Not Found");
    }

       /*
        Purpose : To umPin Notes
         */
    @Override
    public Response unPinNotes(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> isNotesPresent = notesRepository.findByUserIdAndId(userId, id);
        if (isNotesPresent.isPresent()) {
            isNotesPresent.get().setPin(false);
            notesRepository.save(isNotesPresent.get());
            return new Response("Notes pinned", 200, isNotesPresent.get());
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }


    /*
    Purpose : To get Pinned Notes
     */

    @Override
    public List<NotesModel> getPinnedNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NotesModel> notes = notesRepository.findByUserId(userId);
            List<NotesModel> pinNotes = notes.stream().filter(note -> note.isPin() == true)
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
        throw new NotesException(400, "Not Pinned");
    }

    /*
    Purpose : To Add colour
     */
    @Override
    public Response addColour(String token, Long id, String colour) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NotesModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesModel.get().setColor(colour);
            notesRepository.save(notesModel.get());
            return new Response("Notes colour set", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }

    /*
    Purpose : To get colour Notes
     */

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

    /*
    Purpose : To Add collaborator to Notes
     */
    @Override
    public Response addCollaborator(String token, String email, Long id, List<String> collaborator) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validateEmail/" + email, boolean.class);
       if (isUserPresent){
           Long userId = tokenUtil.decodeToken(token);
            Optional<NotesModel> isNotesPresent = notesRepository.findById(userId);
            if (isNotesPresent.isPresent()){
                List<String> collabList = new ArrayList<>();
                collaborator.stream().forEach(collab ->{
                    boolean isEmailPresent = restTemplate.getForObject("http://USER-SERVICE/user/validateEmail/" + email, boolean.class);
                    if (isEmailPresent){
                        collabList.add(collab);
                    } else {
                        throw new NotesException(400, "Email Not Present");
                    }
                });
                Optional<NotesModel> notes = notesRepository.findById(id);
                if (collabList.size()>0){
                    notes.get().setCollaborator(collabList);
                    notesRepository.save(notes.get());
                    NotesModel notes1= new NotesModel();
                    notes1.setTitle(notes.get().getTitle());
                    notes1.setUserId(notes.get().getUserId());
                    notesRepository.save(notes1);
                    return new Response("Added", 200, notes.get());
                }
            }
        }
        throw new NotesException(400, "Not Found");
    }

    /*
    Purpose : To set Remainder
     */
    @Override
    public NotesModel setRemainder(String remainderTime, String token, Long id) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Optional<NotesModel> isNotesPresent = notesRepository.findById(id);
            if (isNotesPresent.isPresent()) {
                LocalDate today = LocalDate.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm:ss");
                LocalDate remainder = LocalDate.parse(remainderTime, dateTimeFormatter);
                if (remainder.isBefore(today)) {
                    throw new NotesException(400, "Invalid Remainder time");
                }
                isNotesPresent.get().setReminderTime(remainderTime);
                notesRepository.save(isNotesPresent.get());
            }
            throw new NotesException(400, "Notes Not Present");
        }
        throw new NotesException(400, "Invalid Token");
    }
}

