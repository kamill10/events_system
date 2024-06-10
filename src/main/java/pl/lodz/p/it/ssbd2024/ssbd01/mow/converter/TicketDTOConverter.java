package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import org.springframework.data.domain.Page;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetTicketDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetTicketDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;


public class TicketDTOConverter {

    public static GetTicketDTO toTicketDTO(Ticket ticket) {
        return new GetTicketDTO(
                ticket.getId(),
                ticket.getSession().getName(),
                ticket.getSession().getStartTime(),
                ticket.getSession().getEndTime(),
                ticket.getSession().getRoom().getName(),
                ticket.getSession().getRoom().getLocation().getName(),
                ticket.getIsNotCancelled()
        );
    }

    public static GetTicketDetailedDTO toTicketDetailedDTO(Ticket ticket) {
        return new GetTicketDetailedDTO(
                ticket.getId(),
                SessionDTOConverter.toSessionPlDetailedDTO(ticket.getSession()),
                ticket.getReservationTime(),
                ticket.getIsNotCancelled()
        );
    }

    public static Page<GetTicketDTO> ticketDTOPage(Page<Ticket> ticketPage) {
        return ticketPage.map(TicketDTOConverter::toTicketDTO);
    }
}