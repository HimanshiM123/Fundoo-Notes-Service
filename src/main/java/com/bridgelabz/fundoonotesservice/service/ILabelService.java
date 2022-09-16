package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.DTO.LabelDTO;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.stereotype.Service;

@Service
public interface ILabelService {
    Response addLabel(LabelDTO labelDTO, String token);

    Response getAllLabel(String token);

    Response updateLabel(long id, LabelDTO labelDTO, String token);

    Response deleteLabel(Long id, String token);
}
