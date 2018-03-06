package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.AiConfig;
import com.beimi.web.model.AttachmentFile;
import com.beimi.web.model.BetLevelTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface BetLevelRepository extends JpaRepository<BetLevelTypeInfo, String> {
    public abstract List<BetLevelTypeInfo> findByTypeAndOrgiAndRoomtype(String type,String orgi , String roomtype);
}
