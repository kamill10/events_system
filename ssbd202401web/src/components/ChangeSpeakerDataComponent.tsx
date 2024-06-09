import { useTranslation } from "react-i18next";
import {useSpeakers} from "../hooks/useSpeakers.ts";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { Speaker, UpdateSpeakerDataType } from "../types/Speaker.ts";
import { ModifySpeakerSchema as modifySpeakerSchema } from "../validation/schemas.ts";
import FormComponent from "./FormComponent.tsx";
import TextFieldComponent from "./TextFieldComponent.tsx";
import { Button, Divider, Typography } from "@mui/material";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";

export default function ChangeSpeakerDataComponent({
                                                        speaker,
                                                        fetchSpeaker,
                                                    }: {
    speaker: Speaker | null;
    fetchSpeaker: () => void;
}) {
    const { t } = useTranslation();
    const { updateSpeakerById } = useSpeakers();
    const [open, setOpen] = useState(false);
    const {
        handleSubmit,
        control,
        formState: { errors },
        trigger,
        setValue,
        getValues,
    } = useForm<UpdateSpeakerDataType>({
        defaultValues: {
            firstName: speaker?.firstName,
            lastName: speaker?.lastName,
        },
        resolver: yupResolver(modifySpeakerSchema),
    });
    const handleRequest = async () => {
        const err = await updateSpeakerById(speaker?.id ?? "", getValues());
        if (!err) {
            fetchSpeaker();
        }
    };
    const onSubmit: SubmitHandler<UpdateSpeakerDataType> = async () => {
        setOpen(true);
    };
    const onError: SubmitErrorHandler<UpdateSpeakerDataType> = (error) => {
        console.error(error);
    };
    useEffect(() => {
        setValue("firstName", speaker?.firstName ?? "");
        setValue("lastName", speaker?.lastName ?? "");
    }, [speaker, setValue, trigger]);
    return (
        <>
            <FormComponent
                handleSubmit={handleSubmit}
                onSubmit={onSubmit}
                onError={onError}
                align="start"
            >
                <Typography variant="h4">{t("changeSpeakerDetails")}</Typography>
                <Typography variant="body1">{t("enterNewSpeakerData")}</Typography>
                <Divider
                    sx={{
                        marginTop: "1rem",
                    }}
                ></Divider>
                <TextFieldComponent
                    control={control}
                    errors={errors}
                    name="firstName"
                    label={t("firstName") + "*"}
                    trigger={trigger}
                    type="text"
                />
                <TextFieldComponent
                    control={control}
                    errors={errors}
                    name="lastName"
                    label={t("lastName") + "*"}
                    trigger={trigger}
                    type="text"
                />
                <Button
                    type="submit"
                    variant="contained"
                    sx={{
                        marginY: 2,
                    }}
                >
                    {t("saveChanges")}
                </Button>
            </FormComponent>
            <ConfirmChangeModal
                callback={handleRequest}
                handleClose={() => setOpen(false)}
                open={open}
            ></ConfirmChangeModal>
        </>
    );
}
