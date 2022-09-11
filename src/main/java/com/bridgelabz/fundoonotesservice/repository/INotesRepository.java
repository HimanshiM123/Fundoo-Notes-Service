package com.bridgelabz.fundoonotesservice.repository;

import com.bridgelabz.fundoonotesservice.model.NotesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface INotesRepository extends JpaRepository<NotesModel, Long> {
    Optional<NotesModel> findByUserIdAndId(Long userId, long id);

    List<NotesModel> findByUserId(Long userId);
}
