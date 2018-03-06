package com.beimi.web.service.repository.es;

import com.beimi.web.model.BetGameDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * Created by fanling on 2018/2/9.
 */
public abstract interface BetGameDetailESRepository extends ElasticsearchCrudRepository<BetGameDetail, String> {


    public abstract List<BetGameDetail> findByStatus(int status);

    public abstract BetGameDetail findByUsername(String username);

    public abstract BetGameDetail findByUserIdAndPeriodsAndLevel(String userId,int periods,int level);

    public abstract List<BetGameDetail> findByTypeAndUserId(int type,String userId,Pageable page);

    public abstract List<BetGameDetail> findByTypeAndPeriods(int type,int periods);

    public abstract Page<BetGameDetail> findByOrgiAndType(String orgi,int type,Pageable page);

    public abstract Page<BetGameDetail> findByUsernameAndPeriodsAndType(String tokenid,int periods,int type,Pageable page);

    public abstract Page<BetGameDetail> findByPeriodsAndType(int periods,int type,Pageable page);

    public abstract Page<BetGameDetail> findByUsernameAndType(String username,int type,Pageable page);

    public abstract Page<BetGameDetail> findByUserIdAndPeriods(String userId,int periods,Pageable page);

}
