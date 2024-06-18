import { TableCell, TableRow } from "@mui/material";
import { Ticket } from "../types/Ticket";
import { parseDateNoOffset } from "../validation/parseDate";
import { useAccount } from "../hooks/useAccount";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

export default function TicketRowComponent({ ticket }: { ticket: Ticket }) {
  const { account } = useAccount();
  const navigate = useNavigate();
  const { t } = useTranslation();

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
        {parseDateNoOffset(ticket.startTime, account?.accountTimeZone)}
      </TableCell>
      <TableCell align="right">
        {parseDateNoOffset(ticket.endTime, account?.accountTimeZone)}
      </TableCell>
      <TableCell align="right">{ticket.roomName}</TableCell>
      <TableCell align="right">{ticket.locationName}</TableCell>
      <TableCell align="right">
        {ticket.isNotCancelled
          ? t("ticketStatusSigned")
          : t("ticketStatusUnSigned")}
      </TableCell>
    </TableRow>
  );
}
