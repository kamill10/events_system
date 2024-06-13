import {Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {useTranslation} from "react-i18next";
import {useSessions} from "../hooks/useSessions.ts";
import {useEffect, useState} from "react";
import {Participant} from "../types/Account.ts";
import {AxiosError} from "axios";
import RefreshIcon from "@mui/icons-material/Refresh";

export function SessionParticipantsComponent(
    {id}: {id: string | undefined}
) {

    const {t} = useTranslation();
    const {getSessionParticipants} = useSessions();
    const [participants, setParticipants] = useState<Participant[]>([]);

    const handleGetParticipants = async () => {
        const response = await getSessionParticipants(id ?? "");
        if (!(response instanceof AxiosError)) {
            setParticipants(response as Participant[]);
        }
    }

    useEffect(() => {
        handleGetParticipants();
    }, []);

    return (
        <>
            <Button
                variant="contained"
                onClick={handleGetParticipants}
                startIcon={<RefreshIcon></RefreshIcon>}
                sx={{
                    margin: 1,
                }}
            >
                {t("refreshData")}
            </Button>
            <TableContainer>
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
                                    {t("firstName")}
                                </TableCell>
                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: "bold",
                                        fontSize: "18px",
                                    }}
                                >
                                    {t("lastName")}
                                </TableCell>
                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: "bold",
                                        fontSize: "18px",
                                    }}
                                >
                                    {t("email")}
                                </TableCell>
                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: "bold",
                                        fontSize: "18px",
                                    }}
                                >
                                    {t("username")}
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {participants.map((participant) => (
                                <TableRow key={participant.username}>
                                    <TableCell align="left">{participant.firstName}</TableCell>
                                    <TableCell align="right">{participant.lastName}</TableCell>
                                    <TableCell align="right">{participant.email}</TableCell>
                                    <TableCell align="right">{participant.username}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </TableContainer>
        </>
    );
}