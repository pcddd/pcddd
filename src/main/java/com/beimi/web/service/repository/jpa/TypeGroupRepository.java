package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.AccountConfig;
import com.beimi.web.model.TypeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fanling on 2018/2/8.
 */
public abstract interface TypeGroupRepository extends JpaRepository<TypeGroup, String> {
    public abstract TypeGroup findByName (String name);
}
