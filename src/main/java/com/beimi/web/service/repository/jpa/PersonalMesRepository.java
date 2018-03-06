package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.PersonalMesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public abstract interface PersonalMesRepository extends JpaRepository<PersonalMesModel, String> {
    public abstract List<PersonalMesModel> findByUserid(String userid);
}
