package com.fullmugu.nanumeal.api.service.history;

import com.fullmugu.nanumeal.api.dto.history.HistoryDTO;
import com.fullmugu.nanumeal.api.dto.history.MakeHistoryRequestDto;
import com.fullmugu.nanumeal.api.entity.history.History;
import com.fullmugu.nanumeal.api.entity.history.HistoryRepository;
import com.fullmugu.nanumeal.api.entity.restaurant.Restaurant;
import com.fullmugu.nanumeal.api.entity.restaurant.RestaurantRepository;
import com.fullmugu.nanumeal.api.entity.user.User;
import com.fullmugu.nanumeal.exception.CInvalidHistoryMoneyException;
import com.fullmugu.nanumeal.exception.CRestaurantNotFoundException;
import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import com.fullmugu.nanumeal.exception.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{

    private final RestaurantRepository restaurantRepository;
    private final HistoryRepository historyRepository;

    @Override
    public List<HistoryDTO> findAllHistory(User user) {

        List<History> historyList = historyRepository.findAllByUserId(user);

        List<HistoryDTO> historyDTOList = historyList.stream().map(history -> entityToDTO(history)).collect(Collectors.toList());

        return historyDTOList;
    }

    @Override
    @Transactional
    public History makeHistory(MakeHistoryRequestDto makeHistoryRequestDto, User user) {
        if (user == null) {
            throw new CUserNotFoundException("유효하지 않은 사용자입니다.", ErrorCode.FORBIDDEN);
        }
        Restaurant restaurant = restaurantRepository
                .findByNameAndLocation(makeHistoryRequestDto.getName(), makeHistoryRequestDto.getLocation())
                .orElseThrow(() -> new CRestaurantNotFoundException("식당 이름이나 위치가 올바르지 않습니다.", ErrorCode.BAD_REQUEST));

        restaurant.setRemainDon(restaurant.getRemainDon() - makeHistoryRequestDto.getMoney());

        if (restaurant.getRemainDon() < 0) {
            throw new CInvalidHistoryMoneyException("식당에 남은 후원금이 모자랍니다. 다시 한번 시도해주세요.", ErrorCode.BAD_REQUEST);
        }

        restaurantRepository.save(restaurant);

        return historyRepository.save(
                History.builder()
                        .userId(user)
                        .restaurantId(restaurant)
                        .usePrice(makeHistoryRequestDto.getMoney())
//                        .regDate(LocalDateTime.now())
                        .build()
        );
    }
}
