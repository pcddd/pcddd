package com.beimi.web.handler.api.rest.user;


import com.beimi.web.model.Lottery;
import com.beimi.web.model.PcData;
import com.beimi.web.service.repository.es.LotteryResESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Iterator;

@RestController
@RequestMapping("/api/updateLottery")
public class updateLotteryController {

    @Autowired
    private LotteryResESRepository lotteryResESRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PcData> updateLottery(@Valid int curNo,@Valid int preNo,@Valid String preRes,@Valid String preStartTime,
                                                          @Valid String preEndTime,@Valid String nextTime){
        Iterator<Lottery> iterator = lotteryResESRepository.findAll().iterator();
        Lottery newLottery = null;
        if (iterator.hasNext()){
            Lottery lottery = lotteryResESRepository.findAll().iterator().next();
            int no = lottery.getNo();
            if (curNo > no){
                newLottery = new Lottery(curNo,preNo,preRes,nextTime,preStartTime,preEndTime);
                System.out.println(newLottery.toString());
                lotteryResESRepository.deleteAll();
                lotteryResESRepository.save(newLottery);
                return new ResponseEntity<>(new PcData("更新成功","200"), HttpStatus.OK);
            }else if(curNo == no && !preRes.equals(lottery.getPrenum())){
                newLottery = new Lottery(curNo,preNo,preRes,nextTime,preStartTime,preEndTime);
                lotteryResESRepository.deleteAll();
                lotteryResESRepository.save(newLottery);
                System.out.println(newLottery.toString());
                return new ResponseEntity<>(new PcData("更新成功","200"), HttpStatus.OK);
            }

        }else{
            newLottery = new Lottery(curNo,preNo,preRes,nextTime,preStartTime,preEndTime);
            lotteryResESRepository.save(newLottery);
            System.out.println(newLottery.toString());
            return new ResponseEntity<>(new PcData("更新成功","200"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new PcData("更新失败","201"), HttpStatus.OK);
    }
}
