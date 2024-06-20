import {
  TableContainer,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from "@mui/material";
import { t } from "i18next";
import SessionRowComponent from "./SessionRowComponent";
import { SessionDetailedType } from "../types/SessionDetailed";

export default function EventDetailsComponent({
  sessionsList,
}: {
  sessionsList: SessionDetailedType[] | null;
}) {
  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow hover>
            <TableCell
              align="left"
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
            >
              {t("name")}
            </TableCell>
            <TableCell
              align="right"
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
            >
              {t("startTimeTime")}
            </TableCell>
            <TableCell
              align="right"
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
            >
              {t("endTimeTime")}
            </TableCell>
            <TableCell
              align="right"
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
            >
              {t("locationName")}
            </TableCell>
            <TableCell
              align="right"
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
            >
              {t("speaker")}
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {sessionsList?.map((session) => {
            return <SessionRowComponent key={session.id} session={session} />;
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
