import {useTranslation} from "react-i18next";
import {SpeakerChanges} from "../types/SpeakerChanges.ts";
import {Paper, Table, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {useAccount} from "../hooks/useAccount.ts";
import parseDate from "../validation/parseDate.ts";


export default function SpeakerHistory(
    {speakerHistory}
        : { speakerHistory: SpeakerChanges[] | null }
) {
    const {t} = useTranslation();
    const {account } = useAccount();
    return (
        <div>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>{t("firstName")}</TableCell>
                            <TableCell>{t("lastName")}</TableCell>
                            <TableCell>{t("createdAt")}</TableCell>
                            <TableCell>{t("updatedAt")}</TableCell>
                            <TableCell>{t("createdBy")}</TableCell>
                            <TableCell>{t("updatedBy")}</TableCell>
                            <TableCell>{t("actionType")}</TableCell>
                        </TableRow>
                    </TableHead>
                    {speakerHistory?.map((change) => (
                        <TableRow key={change.id}>
                            <TableCell>{change.firstName}</TableCell>
                            <TableCell>{change.lastName}</TableCell>
                            <TableCell>
                                {change.createdAt? parseDate(change.createdAt, account?.accountTimeZone) : ""}
                            </TableCell>
                            <TableCell>
                                {change.updatedAt ? parseDate(change.updatedAt, account?.accountTimeZone) : ""}
                            </TableCell>
                            <TableCell>{change.createdBy}</TableCell>
                            <TableCell>{change.updatedBy}</TableCell>
                            <TableCell>{t(change.actionType)}</TableCell>
                        </TableRow>
                    ))}
                </Table>
            </TableContainer>
        </div>
    );
}