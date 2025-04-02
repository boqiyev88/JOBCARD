package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.dto.ResponseDto;
import uz.uat.backend.dto.ResponsesDtos;
import uz.uat.backend.mapper.HistoryMapper;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.history_models.History;
import uz.uat.backend.repository.HistoryRepository;
import uz.uat.backend.service.serviceIMPL.HistoryServiceIM;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryServiceIM {

    private final HistoryMapper historyMapper;
    private final HistoryRepository historyRepository;

    @Override
    public void addHistory(HistoryDto specialistHDto) {
        History specialistHistory = historyMapper.toSpecialist_History(specialistHDto);
        historyRepository.save(specialistHistory);
    }

    @Override
    public ResponsesDtos getHistory(LocalDate from, LocalDate to, String search, int page) {
//        boolean isSearchEmpty = (search == null || search.isEmpty());
//        boolean isDateEmpty = (from == null && to == null);
//        int validPage = (page < 1) ? 0 : (page - 1);
//
//        if (isSearchEmpty && isDateEmpty) {
//            return getAlls(validPage);
//        }
//
//        if (!isSearchEmpty && isDateEmpty) {
//            return getBySearch(search, validPage);
//        }
//        if (from != null && to == null) {
//            return getByDate(from, LocalDate.now(), validPage);
//        }
////        if (from == null && to != null) {
////            return getByToDate(to, validPage);
////        }
//
//        return getByDate(from, to, validPage);

//        List<History> list = historyRepository.findAll();
//        if (list.isEmpty())
//            throw new MyNotFoundException("anything history found");
//        return list;
        return null;
    }

    private ResponsesDtos getByDate(LocalDate from, LocalDate now, int validPage) {

        return null;
    }

    private ResponsesDtos getBySearch(String search, int validPage) {
        Page<History> page = historyRepository.searchByTablename(search, PageRequest.of(validPage, 10));
        return ResponsesDtos.builder()
                .page(validPage + 1)
                .total(page.getTotalElements())
                .data(page.getContent())
                .build();
    }

    private ResponsesDtos getAlls(int validPage) {
        Page<History> page1 = historyRepository.getAll(PageRequest.of(validPage, 10));
        return ResponsesDtos.builder()
                .page(validPage + 1)
                .total(page1.getTotalElements())
                .data(page1.getContent())
                .build();
    }
}
