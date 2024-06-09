import { TicketDetailedType } from "../types/TicketDetailed.ts";
import { useTranslation } from "react-i18next";
import {
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import parseDate from "../validation/parseDate.ts";
import { useAccount } from "../hooks/useAccount.ts";

export function TicketDetailsComponent({
  ticket,
}: {
  ticket: TicketDetailedType | null;
}) {
  const { t } = useTranslation();
  const myAccount = useAccount();

  const address = `${ticket?.session.room.location.street} ${ticket?.session.room.location.buildingNumber}, ${ticket?.session.room.location.postalCode}, ${ticket?.session.room.location.city}`;
  const speaker = `${ticket?.session.speaker.firstName} ${ticket?.session.speaker.lastName}`;

  const data = [
    { ID: ticket?.id },
    {
      [t("ticketStatus")]: ticket?.isNotCancelled
        ? t("ticketStatusSigned")
        : t("ticketStatusUnSigned"),
    },
    {
      [t("signDate")]: parseDate(
        ticket?.reservationTime ?? "",
        myAccount.account?.accountTimeZone,
      ),
    },
    { [t("eventName")]: ticket?.session.event.name },
    { [t("sessionName")]: ticket?.session.name },
    {
      [t("sessionStartDate")]: parseDate(
        ticket?.session.startTime ?? "",
        myAccount.account?.accountTimeZone,
      ),
    },
    {
      [t("sessionEndDate")]: parseDate(
        ticket?.session.endTime ?? "",
        myAccount.account?.accountTimeZone,
      ),
    },
    {
      [t("sessionStatus")]: ticket?.session.isActive
        ? t("sessionStatusActive")
        : t("sessionStatusNotActive"),
    },
    { [t("roomName")]: ticket?.session.room.name },
    { [t("locationName")]: ticket?.session.room.location.name },
    { [t("buildingAddress")]: address },
    { [t("speaker")]: speaker },
  ];

  return (
    <>
      <TableContainer>
        <TableHead>
          <TableCell
            sx={{
              fontWeight: "bold",
              fontSize: "18px",
            }}
          >
            {[t("tableKey")]}
          </TableCell>
          <TableCell
            sx={{
              fontWeight: "bold",
              fontSize: "18px",
            }}
          >
            {[t("tableValue")]}
          </TableCell>
        </TableHead>
        <TableBody>
          {data.map((_, value) => {
            return (
              <TableRow hover key={value}>
                <TableCell>{Object.keys(data[value])}</TableCell>
                <TableCell>{Object.values(data[value])}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </TableContainer>
    </>
  );
}
