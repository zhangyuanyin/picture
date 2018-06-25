package com.zyy.pic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zyy.pic.common.Result;
import com.zyy.pic.entity.vo.VoGomeNetLoan;
import com.zyy.pic.entity.vo.VoSimplePic;
import com.zyy.pic.service.TmAppMainService;

/**
 * @author  Administrator
 * @date  2018/5/16.
 */
@RestController
@RequestMapping("/pic-api")
public class PicController {
	
	@Autowired
	private TmAppMainService tmAppMainService;

    @RequestMapping("/getPicUrlByAppNo")
    Result<List<VoSimplePic>> getPicUrlByAppNo(@RequestParam(name="appNo") String appNo) {
        Result<List<VoSimplePic>> result = new Result();
        //todo 定义service接口、实现
        List<VoSimplePic> list = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            VoSimplePic voSimplePic = new VoSimplePic();
            voSimplePic.setType("type" + 1);
            voSimplePic.setUrl("http://localhost:6666/123/of" + i + appNo);
            list.add(voSimplePic);
        }
        result.setData(list);
        return result;
    }

    @RequestMapping("/queryNetLoan")
    Result<VoGomeNetLoan> queryNetLoan(@RequestParam(name="appNo") String appNo) {
        Result<VoGomeNetLoan> result = new Result();
        VoGomeNetLoan voGomeNetLoan = tmAppMainService.getAppMainByAppNo(appNo);
        result.setData(voGomeNetLoan);
        if("404".equals(voGomeNetLoan.getCode())){
        	result.setCode(404);
        	result.setMsg("未查询到进件信息");
        }
        return result;
    }
}
