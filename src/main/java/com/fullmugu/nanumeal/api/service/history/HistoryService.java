package com.fullmugu.nanumeal.api.service.history;

import com.fullmugu.nanumeal.api.dto.history.HistoryDTO;
import com.fullmugu.nanumeal.api.dto.history.MakeHistoryRequestDto;
import com.fullmugu.nanumeal.api.entity.history.History;
import com.fullmugu.nanumeal.api.entity.user.User;

import java.util.List;

public interface HistoryService {

    List<HistoryDTO> findAllHistory(User user);

    History makeHistory(MakeHistoryRequestDto makeHistoryRequestDto, User user);

    default HistoryDTO entityToDTO(History history) {
        HistoryDTO historyDTO = HistoryDTO.builder()
                .restaurantName(history.getRestaurantId().getName())
                .price(history.getUsePrice())
                .build();

        return historyDTO;
    }
}
