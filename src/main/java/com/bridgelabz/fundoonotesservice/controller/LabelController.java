package com.bridgelabz.fundoonotesservice.controller;

import com.bridgelabz.fundoonotesservice.DTO.LabelDTO;
import com.bridgelabz.fundoonotesservice.service.ILabelService;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/labels")
public class LabelController {
    @Autowired
    ILabelService labelService;

    @PostMapping(value = "/addLabel")
    ResponseEntity<Response> addLabel(@RequestBody LabelDTO labelDTO, @RequestHeader String token,  @PathVariable Long noteId, @RequestParam List<Long> labelId) {
        Response response = labelService.addLabel(labelDTO, token, labelId, noteId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getLabel")
    ResponseEntity<Response> getAllLabel(@RequestHeader String token){
        Response response = labelService.getAllLabel(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /*
         @Purpose : Able to update Candidate details into the Candidate Repository
         @Param : CandidateDTO, id and token
         */
    @PutMapping("updateLabel/{id}")
    ResponseEntity<Response> updateLabel(@Valid @RequestBody LabelDTO labelDTO, @PathVariable long id, @RequestHeader String token ){
        Response response = labelService.updateLabel(id, labelDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
      @Purpose : Able to delete  Candidate details by id in the Candidate Repository
      @Param : token and id
     */
    @DeleteMapping("deleteLabel/{id}")
    ResponseEntity<Response> deleteLabel(@PathVariable Long id, @RequestHeader String token){
        Response response = labelService.deleteLabel(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
