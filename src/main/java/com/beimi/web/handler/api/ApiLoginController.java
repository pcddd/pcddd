package com.beimi.web.handler.api;

import javax.servlet.http.HttpServletRequest;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.PcData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beimi.web.handler.Handler;

@RestController
@RequestMapping("/api/token")
public class ApiLoginController extends Handler{
	
//	@Autowired
//	private PlayUserESRepository playUserESRes;
//
//	@Autowired
//	private PlayUserRepository playUserRes ;

//    @SuppressWarnings("rawtypes")
//    public ResponseEntity login(HttpServletRequest request) {
//    	ResponseEntity entity = null ;
//        return entity;
//    }

    @RequestMapping
    public ResponseEntity error(HttpServletRequest request) {
        return new ResponseEntity<>(new PcData("201", MessageEnum.USER_TOKEN, null), HttpStatus.OK);
    }

//    @SuppressWarnings("rawtypes")
//	@RequestMapping(method = RequestMethod.DELETE)
//    public ResponseEntity logout(HttpServletRequest request , @RequestHeader(value="authorization") String authorization) {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}