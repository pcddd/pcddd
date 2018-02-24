package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.GameBetFatherType;
import com.beimi.web.model.GameBetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GameBetFatherTypeRepository extends JpaRepository<GameBetFatherType, String>{

}
