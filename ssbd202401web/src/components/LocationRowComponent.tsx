import { Location } from "../types/Location.ts";
import { useNavigate } from "react-router-dom";
import { TableCell, TableRow } from "@mui/material";

export default function LocationRowComponent({
  location,
}: {
  location: Location;
}) {
  const navigate = useNavigate();
  return (
    <TableRow
      hover
      onClick={() => {
        navigate("/locations/" + location.id);
      }}
    >
      <TableCell>{location.id}</TableCell>
      <TableCell align="right">{location.name}</TableCell>
      <TableCell align="right">{location.city}</TableCell>
      <TableCell align="right">{location.country}</TableCell>
      <TableCell align="right">{location.street}</TableCell>
      <TableCell align="right">{location.buildingNumber}</TableCell>
      <TableCell align="right">{location.postalCode}</TableCell>
    </TableRow>
  );
}
