package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.MessageDto;
import uz.uat.backend.model.Message;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
//    List<MessageDto> fromEntity(List<Message> messages);
}
