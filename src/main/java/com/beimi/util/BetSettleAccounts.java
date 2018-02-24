package com.beimi.util;

import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.BetTypeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BetSettleAccounts {

    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private BetTypeGroupRepository typeGroupRes;

    @Autowired
    private BetGameDetailESRepository betGameDetailESRepository;


}
