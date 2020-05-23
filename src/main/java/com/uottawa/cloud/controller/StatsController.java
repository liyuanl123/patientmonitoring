package com.uottawa.cloud.controller;


import com.uottawa.cloud.exception.ResourceNotFoundException;
import com.uottawa.cloud.model.ActivityState;
import com.uottawa.cloud.model.StatisticalHistoryData;
import com.uottawa.cloud.util.ConnectToRedshiftCluster;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {

    @GetMapping("/daily/{id}/{s}/{e}")
    public ResponseEntity<List<StatisticalHistoryData>> getDailyStatsByDeviceId(@PathVariable(value = "id") String deviceID,
                                                                    @PathVariable(value = "s") String s,
                                                                    @PathVariable(value = "e") String e)
            throws ResourceNotFoundException {
        List<StatisticalHistoryData> list = ConnectToRedshiftCluster.execQueryDaily(deviceID, s, e);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/hourly/{id}/{s}/{e}")
    public ResponseEntity<List<StatisticalHistoryData>> getHourlyStatsByDeviceId(@PathVariable(value = "id") String deviceID,
                                                                                @PathVariable(value = "s") String s,
                                                                                @PathVariable(value = "e") String e)
            throws ResourceNotFoundException {
        List<StatisticalHistoryData> list = ConnectToRedshiftCluster.execQueryHourly(deviceID, s, e);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/detected/{id}/{s}/{e}")
    public ResponseEntity<ActivityState> getdetectedStatsByDeviceId(@PathVariable(value = "id") String deviceID,
                                                                        @PathVariable(value = "s") String s,
                                                                        @PathVariable(value = "e") String e)
            throws ResourceNotFoundException {
        ActivityState activitystat = ConnectToRedshiftCluster.execQueryActivityState(deviceID, s, e);

        return ResponseEntity.ok().body(activitystat);
    }
}
