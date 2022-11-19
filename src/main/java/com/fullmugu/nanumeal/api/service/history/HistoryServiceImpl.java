package com.fullmugu.nanumeal.api.service.history;

import com.fullmugu.nanumeal.api.dto.history.HistoryDTO;
import com.fullmugu.nanumeal.api.entity.history.History;
import com.fullmugu.nanumeal.api.entity.history.HistoryRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{

    private final HistoryRepository historyRepository;

    @Override
    public List<HistoryDTO> findAllHistory(User user) {

        List<History> historyList = historyRepository.findAllByUserId(user);

        List<HistoryDTO> historyDTOList = historyList.stream().map(history -> entityToDTO(history)).collect(Collectors.toList());

        return historyDTOList;
    }
}
