package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.HxConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface HxConfigRepository extends JpaRepository<HxConfig, String>{

  public abstract HxConfig findByOrgname(String orgname);

  public abstract HxConfig findByAppname(String appname);
}
