import { TableCell, TableRow } from "@mui/material";
import { Ticket } from "../types/Ticket";
import parseDate from "../validation/parseDate";
import { useAccount } from "../hooks/useAccount";
import { useNavigate } from "react-router-dom";

export default function TicketRowComponent({ ticket }: { ticket: Ticket }) {
  const { account } = useAccount();
  const navigate = useNavigate();
  return (
    <TableRow
      hover
      onClick={() => {
        navigate("/profile/ticket/" + ticket.id);
      }}
    >
      <TableCell>{ticket.id}</TableCell>
      <TableCell align="right">{ticket.name}</TableCell>
      <TableCell align="right">
        {parseDate(ticket.startTime, account?.accountTimeZone)}
      </TableCell>
      <TableCell align="right">
        {parseDate(ticket.endTime, account?.accountTimeZone)}
      </TableCell>
      <TableCell align="right">{ticket.roomName}</TableCell>
      <TableCell align="right">{ticket.locationName}</TableCell>
    </TableRow>
  );
}
