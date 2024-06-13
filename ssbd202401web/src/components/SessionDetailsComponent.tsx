import {
    Box, Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
} from "@mui/material";
import {t} from "i18next";
import {Key, useEffect, useState} from "react";
import parseDate from "../validation/parseDate.ts";
import {SessionDetailedType} from "../types/SessionDetailed.ts";
import RefreshIcon from "@mui/icons-material/Refresh";
import AssignmentIcon from "@mui/icons-material/Assignment";
import {useSessions} from "../hooks/useSessions.ts";
import {AxiosError} from "axios";
import {useParams} from "react-router-dom";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";
import {useAccount} from "../hooks/useAccount.ts";


export function SessionDetailsComponent() {

    const {sid} = useParams();
    const [confirmOpen, setConfirmOpen] = useState(false);
    const {signOnSession, getSession} = useSessions();
    const [session, setSession] = useState<SessionDetailedType | null>(null);
    const {isParticipant} = useAccount();

    useEffect(() => {
        getSessionDetails();
    }, []);

    const getSessionDetails = async () => {
        const response = await getSession(sid ?? "");
        if (!(response instanceof AxiosError)) {
            setSession(response as SessionDetailedType);
        }
    }

    const handleRequest = async () => {
        await signOnSession(session?.id ?? "");
    }

    const address = `${session?.room.location.street} ${session?.room.location.buildingNumber}, ${session?.room.location.postalCode}, ${session?.room.location.city}`;
    const speaker = `${session?.speaker.firstName} ${session?.speaker.lastName}`;

    const data = [
        { [t("name")]: session?.name },
        { [t("description")]: session?.description },
        { [t("startTimeTime")]: parseDate(session?.startTime ?? "") },
        { [t("endTimeTime")]: parseDate(session?.endTime ?? "") },
        { [t("allSeats")]: session?.maxSeats },
        { [t("freeSeats")]: "TODO" },
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
            </Box>
        </>
    );
}