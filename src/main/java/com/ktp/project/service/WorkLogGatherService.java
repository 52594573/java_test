package com.ktp.project.service;

import com.ktp.project.dao.WorkLogGatherDao;
import com.ktp.project.dto.WorkLogGatherDto;
import com.ktp.project.entity.WorkLogGather;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkLogGatherService {

    @Autowired
    private WorkLogGatherDao workLogGatherDao;
    @Autowired
    private KaoQinService kaoQinService;

    public List<WorkLogGather> listProjectIsAuth() {
       return  workLogGatherDao.listProjectIsAuth();
    }

    public WorkLogGather SetParameters(WorkLogGather bo) {
        Double turnOutScore = kaoQinService.countByUserIdAndProjectId(bo);
        Double scoreByWorkLog = workLogGatherDao.scoreByWorkLog(bo);
        return createJobRate(bo, turnOutScore, scoreByWorkLog);
    }

    public void save(WorkLogGather saveEntry) {
        workLogGatherDao.save(saveEntry);
    }

    public Map<String, Object> listByCondition(WorkLogGather vo) {
        Map<String, Object> result = new HashMap<>();
        List<WorkLogGatherDto> workLogGathers = workLogGatherDao.listByCondition(vo);
        if (vo.getProjectId() != null && vo.getProjectId() > 0) {
            Integer totalAction = 0, totalSafe = 0, totalQuality = 0;
            for (WorkLogGatherDto bo : workLogGathers) {
                totalAction += bo.getTotalActionLogNum();
                totalSafe += bo.getTotalSafeLogNum();
                totalQuality += bo.getTotalQualityLogNum();
            }
            result.put("totalAction", totalAction);
            result.put("totalSafe", totalSafe);
            result.put("totalQuality", totalQuality);
        }
        result.put("data", workLogGathers);
        return result;
    }

    public List<WorkLogGather> listByMore() {
        return workLogGatherDao.listByMore();
    }

    public WorkLogGather setPropertyForObj(WorkLogGather bo) {
        Double turnOutScore = kaoQinService.countByUserIdAndProjectId(bo);
        Double scoreByWorkLog = workLogGatherDao.createActionScoreByJobType(bo);
        return createJobRate(bo, turnOutScore, scoreByWorkLog);
    }

    private WorkLogGather createJobRate(WorkLogGather bo, Double turnOutScore, Double scoreByWorkLog) {
        double flag = NumberUtil.keepIntDecimalDou(turnOutScore + scoreByWorkLog);
        bo.setRateScore(flag);
        if (flag >= 90) {
            bo.setJobRate(1);//优秀
        } else if (flag < 90 && flag >= 80) {
            bo.setJobRate(2);//称职
        } else if (flag < 80 && flag >= 70) {
            bo.setJobRate(3);//合格
        } else {
            bo.setJobRate(4);//不合格
        }
        return bo;
    }


}
