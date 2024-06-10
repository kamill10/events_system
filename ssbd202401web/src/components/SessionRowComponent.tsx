import {
  Box,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { SessionDetailedType } from "../types/SessionDetailed";
import parseDate from "../validation/parseDate";
import { Key, useState } from "react";
import { t } from "i18next";
import ModalComponent from "./ModalComponent";

export default function SessionRowComponent({
  session,
}: {
  session: SessionDetailedType;
}) {
  const [isOpen, setIsOpen] = useState(false);
  const address = `${session.room.location.street} ${session.room.location.buildingNumber}, ${session.room.location.postalCode}, ${session.room.location.city}`;
  const speaker = `${session.speaker.firstName} ${session.speaker.lastName}`;

  const data = [
    { [t("name")]: session?.name },
    { [t("description")]: session?.description },
    { [t("startTimeTime")]: parseDate(session?.startTime) },
    { [t("endTimeTime")]: parseDate(session?.endTime) },
    { [t("allSeats")]: session?.maxSeats },
    { [t("freeSeats")]: "TODO" },
    { [t("buildingAddress")]: address },
    { [t("roomName")]: session?.room.name },
    { [t("speaker")]: speaker },
  ];

  return (
    <>
      <TableRow
        hover
        onClick={() => {
          setIsOpen(true);
        }}
      >
        <TableCell align="left">{session.name}</TableCell>
        <TableCell align="right">{parseDate(session.startTime)}</TableCell>
        <TableCell align="right">{parseDate(session.endTime)}</TableCell>
        <TableCell align="right">{session.room.location.name}</TableCell>
        <TableCell align="right">
          {session.speaker.firstName + " " + session.speaker.lastName}
        </TableCell>
      </TableRow>
      <ModalComponent
        width={780}
        open={isOpen}
        onClose={() => setIsOpen(false)}
      >
        <>
          <Typography variant="h4" component="h2">
            {t("sessionDetails")}
          </Typography>
          <Button
            type="submit"
            variant="contained"
            sx={{
              marginTop: 2,
            }}
          >
            {t("signForSession")}
          </Button>
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <TableContainer
              sx={{
                maxWidth: "75%",
              }}
            >
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell
                      sx={{
                        fontWeight: "bold",
                        fontSize: "18px",
                      }}
                    >
                      {t("tableKey")}
                    </TableCell>
                    <TableCell
                      sx={{
                        fontWeight: "bold",
                        fontSize: "18px",
                      }}
                    >
                      {t("tableValue")}
                    </TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {data.map((_, value) => {
                    return (
                      <TableRow key={value as Key} hover>
                        <TableCell>{Object.keys(data[value])}</TableCell>
                        <TableCell>{Object.values(data[value])}</TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        </>
      </ModalComponent>
    </>
  );
}
