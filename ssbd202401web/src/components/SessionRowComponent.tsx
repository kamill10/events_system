import { TableCell, TableRow } from "@mui/material";
import { SessionDetailedType } from "../types/SessionDetailed";
import { parseDateNoOffset } from "../validation/parseDate";
import { useLocation, useNavigate } from "react-router-dom";

export default function SessionRowComponent({
  session,
}: {
  session: SessionDetailedType;
}) {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <>
      <TableRow
        hover
        onClick={() => navigate(`${location.pathname}/session/${session.id}`)}
      >
        <TableCell align="left">{session.name}</TableCell>
        <TableCell align="right">
          {parseDateNoOffset(session.startTime)}
        </TableCell>
        <TableCell align="right">
          {parseDateNoOffset(session.endTime)}
        </TableCell>
        <TableCell align="right">{session.room.location.name}</TableCell>
        <TableCell align="right">
          {session.speaker.firstName + " " + session.speaker.lastName}
        </TableCell>
      </TableRow>
    </>
  );
}
