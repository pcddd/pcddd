package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.GameBetType;
import com.beimi.web.model.SysMesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface SystemMesRepository extends JpaRepository<SysMesModel, String> {
}
