package uz.uat.backend.dto;

import java.util.List;

public record WorkListDto(Object workType, List<String> taskList) {
}
