package uz.uat.backend.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.ResponseJobCardDto;

import java.util.List;

@Component
public class Notifier {


    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public Notifier(@Qualifier("brokerMessagingTemplate") SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public void SpecialistNotifier(List<ResponseJobCardDto> jobCards) {
        messagingTemplate.convertAndSend("/topic/specialist", jobCards);
    }

    public void TechnicianNotifier(List<ResponseJobCardDto> jobCards) {
        messagingTemplate.convertAndSend("/topic/technician", jobCards);
    }

    public void TechnicianMassageNotifier(String massage) {
        messagingTemplate.convertAndSend("/topic/technician", massage);
    }

    public void SpecialistMassageNotifier(String massage) {
        messagingTemplate.convertAndSend("/topic/technician", massage);
    }

    public void EngineerNotifier(Object object) {
        messagingTemplate.convertAndSend("/topic/Engineer", object);
    }

    public void WorkersMassageNotifier(String massage) {
        messagingTemplate.convertAndSend("/topic/WorkerMassages", massage);
    }

    public void WorkerNotifier(Object object) {
        messagingTemplate.convertAndSend("/topic/Worker", object);
    }


}
