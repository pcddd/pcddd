package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PcRoomInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface PcRoomRepository extends JpaRepository<PcRoomInfo, String>{

  public abstract PcRoomInfo findByRoomid(String roomid);

  public abstract PcRoomInfo findByIdAndOrgi(String id, String orgi);

  public abstract Page<PcRoomInfo> findByOrgi(String orgi, Pageable page);

  public abstract List<PcRoomInfo> findByRoomidAndOrgi(String roomid, String orgi);

  public abstract List<PcRoomInfo> findByRoomtypeAndOrgi(String roomtype, String orgi);
}
