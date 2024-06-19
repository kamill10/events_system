import { Event } from "../types/Event.ts";
import { useAccount } from "../hooks/useAccount.ts";
import { TableCell, TableRow } from "@mui/material";
import { parseDateNoOffset } from "../validation/parseDate.ts";
export function HistoricalEventRowComponent({ event }: { event: Event }) {
  const { account } = useAccount();
  return (
    <TableRow hover>
      <TableCell>{event.id}</TableCell>
      <TableCell align="right">{event.name}</TableCell>
      <TableCell align="right">{event.description}</TableCell>
      <TableCell align="right">
        {parseDateNoOffset(event.startDate, account?.accountTimeZone)}
      </TableCell>
      <TableCell align="right">
        {parseDateNoOffset(event.endDate, account?.accountTimeZone)}
      </TableCell>
    </TableRow>
  );
}
