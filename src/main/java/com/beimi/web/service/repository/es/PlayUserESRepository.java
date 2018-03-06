package com.beimi.web.service.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.beimi.web.model.PlayUser;

import java.util.List;

public abstract interface PlayUserESRepository
  extends ElasticsearchCrudRepository<PlayUser, String>
{
  public abstract PlayUser findById(String paramString);
  
  public abstract PlayUser findByUsername(String username);

  public abstract List<PlayUser> findByPeriods(String periods);
  
  public abstract int countByUsername(String username);
  
  public abstract PlayUser findByEmail(String email);
  
  public abstract PlayUser findByMobileAndPassword(String username, String password);
  
  public abstract Page<PlayUser> findByOrgi(String orgi, Pageable page);

  public abstract Page<PlayUser> findByUsername(String username, Pageable page);
  
  public abstract Page<PlayUser> findByOrgiAndOnline(String orgi ,boolean online, Pageable page);
}
