package com.beimi.web.service.repository.es;

import com.beimi.web.model.BetGameDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * Created by fanling on 2018/2/9.
 */
public abstract interface BetGameDetailESRepository extends ElasticsearchCrudRepository<BetGameDetail, String> {
    public abstract List<BetGameDetail> findByTypeAndPeriods(int type,int periods);

    public abstract Page<BetGameDetail> findByOrgi(String orgi,Pageable page);

    public abstract Page<BetGameDetail> findByTokenIdAndPeriods(String tokenid,int periods,Pageable page);

}
