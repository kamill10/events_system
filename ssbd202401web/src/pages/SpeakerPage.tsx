import {
    Breadcrumbs,
    Button,
    Divider,
    Tab,
    Tabs,
    Typography,
} from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { SyntheticEvent, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import useNotification from "../hooks/useNotification.tsx";
import ChangeSpeakerDetails from "../components/ChangeSpeakerDetails.tsx";
import { Pathnames } from "../router/Pathnames.ts";
import { useSpeakers } from "../hooks/useSpeakers.ts";
import { Speaker } from "../types/Speaker.ts";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function SpeakerPage() {
    const { t } = useTranslation();
    const { id } = useParams<{ id: string }>();
    const [speaker, setSpeaker] = useState<Speaker | null>(null);
    const { getSpeakerById } = useSpeakers();
    const sendNotification = useNotification();
    const [page, setPage] = useState(0);
    const handleChange = (_: SyntheticEvent, newValue: number) => {
        setPage(newValue);
    };

    async function fetchSpeaker() {
        if (id) {
            getSpeakerById(id).then((speaker) => {
                setSpeaker(speaker);
            });
        } else {
            sendNotification({
                type: "error",
                description: t("noURLParam"),
            });
        }
    }

    useEffect(() => {
        fetchSpeaker();
    }, []);

    return (
        <ContainerComponent>
            <Breadcrumbs
                aria-label="breadcrumb"
                sx={{
                    marginBottom: 3,
                }}
            >
                <Link
                    to={Pathnames.public.home}
                    style={{
                        textDecoration: "none",
                        color: "black",
                    }}
                >
                    {t("home")}
                </Link>
                <Link
                    to={Pathnames.manager.speakers}
                    style={{
                        textDecoration: "none",
                        color: "black",
                    }}
                >
                    {t("speakersLink")}
                </Link>
                <Typography
                    color="text.primary"
                    sx={{
                        fontWeight: "bold",
                    }}
                >
                    {t("speakerDetails")}
                </Typography>
            </Breadcrumbs>
            <Button
                variant="contained"
                onClick={fetchSpeaker}
                startIcon={<RefreshIcon />}
                sx={{
                    margin: 2,
                }}
            >
                {t("refreshData")}
            </Button>
            <Tabs value={page} onChange={handleChange}>
                <Tab label={t("changeSpeakerDetails")}></Tab>
            </Tabs>

            <Divider></Divider>
            {page == 0 && (
                <ChangeSpeakerDetails
                    speaker={speaker}
                    fetchSpeaker={fetchSpeaker}
                ></ChangeSpeakerDetails>
            )}
        </ContainerComponent>
    );
}

