package com.fullmugu.nanumeal.api.controller.history;

import com.fullmugu.nanumeal.api.dto.history.HistoryDTO;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Log4j2
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/use")
    public ResponseEntity<List<HistoryDTO>> getUsingList(@AuthenticationPrincipal User user){
        List<HistoryDTO> historyDTOList = historyService.findAllHistory(user);

        return ResponseEntity.ok(historyDTOList);
    }
}
