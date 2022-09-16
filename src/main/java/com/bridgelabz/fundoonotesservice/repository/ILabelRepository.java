package com.bridgelabz.fundoonotesservice.repository;

import com.bridgelabz.fundoonotesservice.model.LabelModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILabelRepository extends JpaRepository<LabelModel, Long> {
}
