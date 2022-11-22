package com.fullmugu.nanumeal.api.controller.history;

import com.fullmugu.nanumeal.api.dto.history.HistoryDTO;
import com.fullmugu.nanumeal.api.dto.history.MakeHistoryRequestDto;
import com.fullmugu.nanumeal.api.dto.history.MakeHistoryResponseDto;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.api.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
@Log4j2
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/use")
    public ResponseEntity<List<HistoryDTO>> getUsingList(@AuthenticationPrincipal User user) {
        List<HistoryDTO> historyDTOList = historyService.findAllHistory(user);

        return ResponseEntity.ok(historyDTOList);
    }

    @PostMapping("/make")
    public ResponseEntity<MakeHistoryResponseDto> makeHistory(@RequestBody MakeHistoryRequestDto makeHistoryRequestDto, @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                MakeHistoryResponseDto.from(historyService.makeHistory(makeHistoryRequestDto, user)
                )
        );
    }
}
