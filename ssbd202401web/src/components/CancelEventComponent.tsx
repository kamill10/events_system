import {useTranslation} from "react-i18next";
import {useEvents} from "../hooks/useEvents.ts";
import {Button, Typography} from "@mui/material";
import {useState} from "react";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";

export function CancelEventComponent({id}: {id: string}) {

    const { cancelEvent } = useEvents();
    const { t } = useTranslation();
    const [open, setOpen] = useState(false);

    return (
        <>
            <Typography variant="h4">{t("cancelEvent")}</Typography>
            <Typography>{t("cancelEventDescription")}</Typography>
            <Button
                onClick={() => setOpen(true)}
                variant="contained"
                color="error"
            >
                {t("cancelEvent")}
            </Button>
            <ConfirmChangeModal open={open} handleClose={() => setOpen(false)} callback={()=>cancelEvent(id)} />
        </>
    );
}