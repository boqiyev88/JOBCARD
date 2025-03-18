package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.model.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    List<TaskDto> list(List<Task> tasks);
    List<Task> toEntitys(List<TaskDto> tasks);
}
