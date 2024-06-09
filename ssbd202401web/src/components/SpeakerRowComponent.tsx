import { Speaker } from "../types/Speaker.ts";
import { useNavigate } from "react-router-dom";
import { TableCell, TableRow } from "@mui/material";

export default function LocationRowComponent({
  speaker,
}: {
  speaker: Speaker;
}) {
  const navigate = useNavigate();
  return (
    <TableRow
      hover
      onClick={() => {
        navigate("/speakers/" + speaker.id);
      }}
    >
      <TableCell>{speaker.id}</TableCell>
      <TableCell align="right">{speaker.firstName}</TableCell>
      <TableCell align="right">{speaker.lastName}</TableCell>
    </TableRow>
  );
}
