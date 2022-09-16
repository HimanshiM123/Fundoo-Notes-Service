package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.DTO.LabelDTO;
import com.bridgelabz.fundoonotesservice.exception.NotesException;
import com.bridgelabz.fundoonotesservice.model.LabelModel;
import com.bridgelabz.fundoonotesservice.model.NotesModel;
import com.bridgelabz.fundoonotesservice.repository.ILabelRepository;
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

@Service
public class LabelService implements ILabelService{
    @Autowired
    ILabelRepository labelRepository;
    @Autowired
    INotesRepository notesRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    MailService mailService;

    @Autowired
    RestTemplate restTemplate;
    @Override
    public Response addLabel(LabelDTO labelDTO, String token, List<Long> labelId, Long noteId) {
            boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
            if (isUserPresent) {
                List<LabelModel> isLabelListPresent = new ArrayList<>();
                labelId.stream().forEach(label -> {
                    Optional<LabelModel> isLabelPresent = labelRepository.findById(label);
                    if (isLabelPresent.isPresent()){
                        isLabelListPresent.add(isLabelPresent.get());
                    }
                });
                Optional<NotesModel> notes = notesRepository.findById(noteId);
                if (notes.isPresent()){
                    notes.get().setLabelList(isLabelListPresent);
                    notesRepository.save(notes.get());
                    return new Response("Label Added", 200, notes.get());
                }
            }
            throw new NotesException(400, "Notes Not Found");
        }

    @Override
    public Response getAllLabel(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long labelId = tokenUtil.decodeToken(token);
            Optional<LabelModel> isLabelPresent = labelRepository.findById(labelId);
            if (isLabelPresent.isPresent()) {
                List<LabelModel> getAllCandidate = labelRepository.findAll();
                if (getAllCandidate.size() > 0)
                    return new Response("Label Added Successfully", 200, getAllCandidate);
                else
                    throw new NotesException(400, "No Data Found");
            }
        }
        throw new NotesException(400, "Token is Wrong");
    }

    @Override
    public Response updateLabel(long id, LabelDTO labelDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long labelId = tokenUtil.decodeToken(token);
            Optional<LabelModel> isLabelPresent = labelRepository.findById(labelId);
            if (isLabelPresent.isPresent()){
                isLabelPresent.get().setLabelName(labelDTO.getLabelName());
               isLabelPresent.get().setNotes(labelDTO.getNotes());
               labelRepository.save(isLabelPresent.get());
                return new Response("Label Updated Successfully", 200, isLabelPresent);
            } else {
                throw new NotesException(400, "Notes Cannot be updated");
            }
        }
        throw new NotesException(400, "No Data Found");
    }

    @Override
    public Response deleteLabel(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://localhost:8083/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long labelId = tokenUtil.decodeToken(token);
            Optional<LabelModel> isLabelPresent = labelRepository.findById(labelId);
            if (isLabelPresent.isPresent()) {
                labelRepository.delete(isLabelPresent.get());
                return new Response("Labels Deleted successfully", 200, isLabelPresent);
            } else {
                throw new NotesException(400, "Can't Delete Label");
            }
        }
        throw new NotesException(400, "No Data Found");
    }
}
