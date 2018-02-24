package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.GameBetType;
import com.beimi.web.model.GameRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GameBetTypeRepository extends JpaRepository<GameBetType, String>{

  public abstract GameBetType findByIdAndOrgi(String id, String orgi);

  public abstract Page<GameBetType> findByOrgi(String orgi, Pageable page);

  public abstract List<GameBetType> findByTypeAndRoomtypeAndOrgi(String type,String roomtype, String orgi);

  public abstract List<GameBetType> findByType(String typr);
}
