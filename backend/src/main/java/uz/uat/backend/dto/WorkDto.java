package uz.uat.backend.dto;

import lombok.Builder;
import uz.uat.backend.model.Task;

import java.util.List;
@Builder
public record WorkDto(String id, String workType, List<Task> taskList) {
}
