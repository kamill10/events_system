import {
  Box,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { t } from "i18next";
import { Key, useEffect, useState } from "react";
import { parseDateNoOffset } from "../validation/parseDate.ts";
import { SessionDetailedType } from "../types/SessionDetailed.ts";
import RefreshIcon from "@mui/icons-material/Refresh";
import AssignmentIcon from "@mui/icons-material/Assignment";
import { useSessions } from "../hooks/useSessions.ts";
import { AxiosError } from "axios";
import { useParams } from "react-router-dom";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";
import { useAccount } from "../hooks/useAccount.ts";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";

export function SessionDetailsComponent() {
  const { sid } = useParams();
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [confirmDeleteOpen, setConfirmDeleteOpen] = useState(false);
  const { signOnSession, getSession, cancelSession, getSessionForManager } =
    useSessions();
  const [session, setSession] = useState<SessionDetailedType | null>(null);
  const { isParticipant, isManager } = useAccount();

  useEffect(() => {
    getSessionDetails();
  }, []);

  const getSessionDetails = async () => {
    const response = isManager
      ? await getSessionForManager(sid ?? "")
      : await getSession(sid ?? "");
    if (!(response instanceof AxiosError)) {
      setSession(response as SessionDetailedType);
    }
  };

  const handleDelete = async () => {
    const response = await cancelSession(session?.id ?? "");
    if (!response) {
      getSessionDetails();
    }
  };

  const handleRequest = async () => {
    await signOnSession(session?.id ?? "");
    getSessionDetails();
  };

  const address = `${session?.room.location.street} ${session?.room.location.buildingNumber}, ${session?.room.location.postalCode}, ${session?.room.location.city}`;
  const speaker = `${session?.speaker.firstName} ${session?.speaker.lastName}`;

  const data = [
    { [t("name")]: session?.name },
    { [t("description")]: session?.description },
    { [t("startTimeTime")]: parseDateNoOffset(session?.startTime ?? "") },
    { [t("endTimeTime")]: parseDateNoOffset(session?.endTime ?? "") },
    { [t("allSeats")]: session?.maxSeats },
    { [t("freeSeats")]: session?.availableSeats },
    { [t("buildingAddress")]: address },
    { [t("roomName")]: session?.room.name },
    { [t("speaker")]: speaker },
  ];

  return (
    <>
      <Button
        variant="contained"
        onClick={getSessionDetails}
        startIcon={<RefreshIcon></RefreshIcon>}
        sx={{
          margin: 1,
        }}
      >
        {t("refreshData")}
      </Button>
      {isParticipant && (
        <Button
          type="submit"
          variant="contained"
          startIcon={<AssignmentIcon></AssignmentIcon>}
          sx={{
            margin: 1,
          }}
          onClick={() => setConfirmOpen(true)}
        >
          {t("signForSession")}
        </Button>
      )}
      {isManager && session?.isActive && (
        <Button
          variant="contained"
          onClick={() => setConfirmDeleteOpen(true)}
          color="error"
          startIcon={<DeleteForeverIcon></DeleteForeverIcon>}
          sx={{
            margin: 1,
          }}
        >
          {t("cancelSession")}
        </Button>
      )}
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
        <ConfirmChangeModal
          callback={handleRequest}
          handleClose={() => setConfirmOpen(false)}
          open={confirmOpen}
        ></ConfirmChangeModal>
        <ConfirmChangeModal
          callback={handleDelete}
          handleClose={() => setConfirmDeleteOpen(false)}
          open={confirmDeleteOpen}
        ></ConfirmChangeModal>
      </Box>
    </>
  );
}
