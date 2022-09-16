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
import java.time.LocalDateTime;
import java.util.List;

    /*
     Purpose : NotesController to process Data API's
     @author : Himanshi Mohabe
     version : 1.0
    */
@RestController
@RequestMapping("/notes")
public class NotesController {
    @Autowired
    INotesService notesService;

        /*
         *@Purpose:to add Notes into the Notes Repository
         * @Param :NotesDTO
         */

    @PostMapping(value = "/addNotes")
    ResponseEntity<Response> addNotes(@Valid @RequestBody NotesDTO notesDTO, @RequestHeader String token) {
        Response response = notesService.addNotes(notesDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
        *@Purpose : to get list of Notes in the Notes Repository using id
         @Param  : id
        */

    @GetMapping("/getNotes/{id}")
    ResponseEntity<Response> getNotes(@PathVariable long id){
        Response response = notesService.getNotesById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

          /*
           *@Purpose : to get list of Notes in the Notes Repository using id
            @Param  : token
           */

    @GetMapping("/getAllNotes")
    public List<NotesModel> getAllNotes(@RequestHeader String token){

        return notesService.getAllNotes(token);
    }

        /*
         @Purpose : Able to update Notes in the notes Repository
         @Param :   NotesDTO,  noteId and token
         */

    @PutMapping("updateNotes/{id}")
    ResponseEntity<Response> updateNotes(@Valid @RequestBody NotesDTO notesDTO, @PathVariable long id, @RequestHeader String token ){
        Response response = notesService.updateNotes(id, notesDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
            @Purpose : Able to add deleted Notes in the trash in the notes Repository
            @Param :    noteId and token
            */
    @DeleteMapping("trashNotes/{id}")
    ResponseEntity<Response> trash(@PathVariable Long id, @RequestHeader String token){

        Response response = notesService.trash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
          @Purpose : Able to get Trashed Notes in the notes Repository
          @Param :   token
          */
    @GetMapping("/getTrashNotes")
   public List <NotesModel> getTrashNotes(@RequestHeader String token){

        return notesService.getTrashNotes(token);
    }

            /*
               @Purpose : Able to  deleted Notes from the notes Repository
               @Param :    noteId and token
               */
    @DeleteMapping("deleteNotes/{id}")
    ResponseEntity<Response> delete(@PathVariable Long id, @RequestHeader String token){

        Response response = notesService.delete(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

          /*
           @Purpose : Able to  Archive Notes in the notes Repository
           @Param :    noteId and token
            */
    @PutMapping("/archive")
    public ResponseEntity<Response> moveToArchive(@PathVariable Long id, @RequestHeader String token) {
        Response response = notesService.archiveNote(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

            /*
              @Purpose : Able to get Archive Notes in the notes Repository
              @Param :    noteId and token
               */
    @GetMapping("/getArchive")
    public List<NotesModel> getArchive(@RequestHeader String token){

        return notesService.getArchiveNotes(token);
    }

          /*
             @Purpose : Able to  pin Notes in the notes Repository
             @Param :   noteId and token
             */
    @PutMapping("/pinNotes")
    public ResponseEntity<Response> pinNotes(@PathVariable Long id, @RequestHeader String token){
        Response response = notesService.pinNotes(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
                /*
                 @Purpose : Able to  unpin Notes in the notes Repository
                 @Param :   noteId and token
                  */
        @PutMapping("/unPinNotes")
        public ResponseEntity<Response> unPinNotes(@PathVariable Long id, @RequestHeader String token){
            Response response = notesService.unPinNotes(id, token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        /*
         @Purpose : Able to get pin Notes in the notes Repository
         @Param :    noteId and token
          */
    @GetMapping("/getPin")
    public List<NotesModel> getPin(@RequestHeader String token){

        return notesService.getPinnedNotes(token);
    }

        /*
             @Purpose : Able to remove Trashed Notes in the notes Repository
             @Param :    noteId and token
             */
    @DeleteMapping("/removeTrash/{id}")
    ResponseEntity<Response> removeTrash(@PathVariable Long id, @RequestHeader String token){

        Response response = notesService.removeTrash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
          @Purpose : Able to add colour to the Notes in the notes Repository
          @Param :    noteId, token and colour
          */

    @PutMapping("/addColour")
    public ResponseEntity<Response> addColour(@RequestHeader String token, @PathVariable Long id, @RequestHeader String colour) {
        Response response = notesService.addColour(token, id, colour);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
             @Purpose : Able to get colour  Notes from the notes Repository by id
             @Param :    noteId
             */
    @GetMapping("/getColour")
    ResponseEntity<Response> getColour(@PathVariable Long id) {
        Response response = notesService.getColour(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
        @PostMapping(value = "/addCollaborator")
        ResponseEntity<Response> addCollaborator(@RequestParam String email, @PathVariable Long id, @RequestParam List<String> collaborator ) {
            Response response = notesService.addCollaborator(email, id, collaborator);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        @PostMapping(value = "/setRemainder/{id}")
        ResponseEntity<Response> setRemainder(@RequestHeader String token, @PathVariable Long id, @RequestParam String remainderTime) {
        NotesModel notesModel = notesService.setRemainder(remainderTime, token, id);
            Response response = new Response("Remainder set Successfully", 400, notesModel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
}
