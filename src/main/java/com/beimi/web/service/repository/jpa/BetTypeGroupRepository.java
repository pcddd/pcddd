package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.GameBetType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fanling on 2018/2/8.
 */
public  abstract interface BetTypeGroupRepository extends JpaRepository<GameBetType, String> {
    public abstract GameBetType findById (String id);
}
