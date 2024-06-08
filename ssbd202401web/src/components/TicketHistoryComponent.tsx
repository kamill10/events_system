import {
  Box,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  Typography,
} from "@mui/material";
import { useTranslation } from "react-i18next";
import { useEffect, useState } from "react";
import {
  PaginationTicketResponse,
  TicketWithNumberOfElements,
} from "../types/Ticket";
import { isInstanceOf } from "../utils";
import RefreshIcon from "@mui/icons-material/Refresh";
import TicketRowComponent from "./TicketRowComponent";
import { useMySessions } from "../hooks/useMySessions.ts";

export default function TicketHistoryComponent() {
  const { t } = useTranslation();
  const { getMyHistoryTickets } = useMySessions();
  const [tickets, setTickets] = useState<TicketWithNumberOfElements | null>();

  async function getTickets() {
    const response = await getMyHistoryTickets();
    if (isInstanceOf<PaginationTicketResponse>(response, "pageable")) {
      setTickets({
        numberOfElements: response.numberOfElements,
        tickets: response.content,
      });
    }
  }

  useEffect(() => {
    getTickets();
  }, []);

  return (
    <Box
      sx={{
        marginTop: 4,
        marginLeft: 5,
      }}
    >
      <Typography variant="h4">{t("history")}</Typography>
      <Button
        onClick={getTickets}
        variant="contained"
        startIcon={<RefreshIcon></RefreshIcon>}
        color="secondary"
        sx={{
          mt: 1,
          mb: 2,
          width: "fit-content",
          alignSelf: "center",
        }}
      >
        {t("refreshData")}
      </Button>
      <TableContainer>
        <Table>
          <TableHead>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="left"
            >
              ID
            </TableCell>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="right"
            >
              {t("sessionName")}
            </TableCell>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="right"
            >
              {t("startTime")}
            </TableCell>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="right"
            >
              {t("endTime")}
            </TableCell>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="right"
            >
              {t("roomName")}
            </TableCell>
            <TableCell
              sx={{
                fontWeight: "bold",
                fontSize: "18px",
              }}
              align="right"
            >
              {t("locationName")}
            </TableCell>
          </TableHead>
          <TableBody>
            {tickets?.tickets.map((ticket) => {
              return (
                <TicketRowComponent
                  key={ticket.id}
                  ticket={ticket}
                ></TicketRowComponent>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
