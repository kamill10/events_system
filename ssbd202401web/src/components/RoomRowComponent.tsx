import { TableCell, TableRow } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { RoomType } from "../types/Room.ts";
import { Pathnames } from "../router/Pathnames.ts";

export default function RoomRowComponent({ room }: { room: RoomType }) {
  const navigate = useNavigate();
  return (
    <TableRow
      hover
      onClick={() => {
        navigate(Pathnames.manager.room + room.id);
      }}
    >
      <TableCell>{room.id}</TableCell>
      <TableCell align="right">{room.name}</TableCell>
      <TableCell align="right">{room.maxCapacity}</TableCell>
    </TableRow>
  );
}
