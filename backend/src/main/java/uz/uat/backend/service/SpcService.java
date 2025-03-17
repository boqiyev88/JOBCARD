package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.CityDto;
import uz.uat.backend.mapper.CityMapper;
import uz.uat.backend.model.City;
import uz.uat.backend.repository.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpcService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public List<CityDto> getCitys() {
        Optional<List<City>> optional = cityRepository.getByIsDeleted();
        if (optional.isEmpty())
            throw new MyNotFoundException("City list empty");
        return cityMapper.toDto(optional.get());
    }


}
