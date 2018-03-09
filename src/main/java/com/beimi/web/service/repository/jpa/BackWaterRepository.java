package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.AccountRecordModel;
import com.beimi.web.model.BackWaterMondel;
import javafx.scene.control.Pagination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface BackWaterRepository extends JpaRepository<BackWaterMondel, String> {

  public abstract BackWaterMondel findByGametypeAndRoomtypeAndUseridAndCreatetime(int gametype, int roomtype, String userid, long createtime);
  public abstract BackWaterMondel findByRoomtypeAndUseridAndCreatetime(int roomtype, String userid, long createtime);

  public abstract List<BackWaterMondel> findByUseridAndRoomtype(String userid, int roomtype, Pageable pageable);
  public abstract List<BackWaterMondel> findByUseridAndRoomtypeAndWatergoldGreaterThan(String userid, int roomtype,long min, Pageable pageable);
}
