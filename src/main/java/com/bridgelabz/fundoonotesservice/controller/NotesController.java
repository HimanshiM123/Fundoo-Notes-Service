package com.bridgelabz.fundoonotesservice.controller;

import com.bridgelabz.fundoonotesservice.DTO.NotesDTO;
import com.bridgelabz.fundoonotesservice.model.NotesModel;
import com.bridgelabz.fundoonotesservice.service.INotesService;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {
    @Autowired
    INotesService notesService;
    @PostMapping(value = "/addNotes")
    ResponseEntity<Response> addNotes(@Valid @RequestBody NotesDTO notesDTO, @RequestHeader String token) {
        Response response = notesService.addNotes(notesDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getNotes/{id}")
    ResponseEntity<Response> getNotes(@PathVariable long id){
        Response response = notesService.getNotesById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getAllNotes")
    public List<NotesModel> getAllNotes(@RequestHeader String token){
        return notesService.getAllNotes(token);
    }
    @PutMapping("updateNotes/{id}")
    ResponseEntity<Response> updateNotes(@Valid @RequestBody NotesDTO notesDTO, @PathVariable long id, @RequestHeader String token ){
        Response response = notesService.updateNotes(id, notesDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("trashNotes/{id}")
    ResponseEntity<Response> trash(@PathVariable Long id, @RequestHeader String token){

        Response response = notesService.trash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getTrashNotes")
   public List <NotesModel> getTrashNotes(@RequestHeader String token){

        return notesService.getTrashNotes(token);
    }

    @DeleteMapping("deleteNotes/{id}")
    ResponseEntity<Response> delete(@PathVariable Long id, @RequestHeader String token){

        Response response = notesService.delete(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/archive")
    public ResponseEntity<Response> moveToArchive(@PathVariable Long id, @RequestHeader String token) {
        Response response = notesService.archiveNote(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getArchive")
    public List<NotesModel> getArchive(@RequestHeader String token){

        return notesService.getArchiveNotes(token);
    }

    @GetMapping("/getPin")
    public List<NotesModel> pin(@RequestHeader String token){
        return notesService.pinned(token);
    }

    @DeleteMapping("removeTrash/{id}")
    ResponseEntity<Response> removeTrash(@PathVariable Long id, @RequestHeader String token){

        Response response = notesService.removeTrash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/addColour")
    public ResponseEntity<Response> addColour(@RequestHeader String token, @PathVariable Long id, @RequestHeader String colour) {
        Response response = notesService.addColour(token, id, colour);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getColour")
    ResponseEntity<Response> getColour(@PathVariable Long id) {
        Response response = notesService.getColour(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
